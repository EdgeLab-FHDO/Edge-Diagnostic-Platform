package Application.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ch.qos.logback.core.joran.conditional.ElseAction;

public class LatencyReporter {
   private Semaphore reportLock = new Semaphore(1);
   private List<String> reportQueue = new ArrayList<>();;
   private boolean debugMode = true; //TODO make this available when starting application


   //TODO move this to a latency reporter utility
   public void queueLatencyReport(String location, boolean serverUsage, long startTime, long endTime, String step, String ip, Semaphore ipLock) throws JsonProcessingException, InterruptedException {
      String timestamp = new SimpleDateFormat("YYYY-MM-dd_HH:mm:ss").format(new Date());
      long latency = (endTime-startTime)/1000000;
      String reportedIp="";
      if(debugMode) {
            //Display the time to read image locally
            if(step == "readImage") {
               System.out.println("[" + timestamp + "] Read image locally in " + location + " Execution Time: " + latency + "ms");
            }
            //Display the time to send image to server
            else if(step == "sendImage") {
               System.out.println("[" + timestamp + "] Sent image to " + location + " Execution Time: " + latency + "ms");
            }
            //Display the time to read detection result from client/server
            else if(step == "readResult") {
               System.out.println("[" + timestamp + "] Read result from " + location + " Execution Time: " + latency + "ms");
            }
            //Display the time to analyze detection result
            else if(step == "analyzeResult") {
               System.out.println("[" + timestamp + "] Analyze result from " + location + " Execution Time: " + latency + "ms");
            }
            //Display the time to write detection result
            else if(step == "writeResult") {
               System.out.println("[" + timestamp + "] Wrote image to file in " + location + " Execution Time: " + latency + "ms");
            }
            //Display the time to detect by client
            else if(step == "clientDetect") {
               System.out.println("[" + timestamp + "] Detected in " + location + " Execution Time: " + latency + "ms");
            }
            //Total time to complete the whole detection process
            else {
               System.out.println("[" + timestamp + "] Total processing time in " + location + " Execution Time: " + latency + "ms");
            }
      }
      try {
            ipLock.acquire();
            reportedIp = ip;
      } finally {
            ipLock.release();
      }

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode resultObject = mapper.createObjectNode();
      resultObject.put("timestamp", timestamp);
      resultObject.put("latency", latency);
      resultObject.put("use_server", serverUsage);
      resultObject.put("server_ip", reportedIp);
      resultObject.put("step", step);

      String result = mapper.writeValueAsString(resultObject);

      try {
            reportLock.acquire();
            reportQueue.add(result);
      } finally {
            reportLock.release();
      }
   }

   public void queueLatencyReport(String location, boolean serverUsage, long startTime, long endTime, String step) throws JsonProcessingException, InterruptedException {
      String timestamp = new SimpleDateFormat("YYYY-MM-dd_HH:mm:ss").format(new Date());
      long latency = (endTime-startTime)/1000000;
      String reportedIp="";
      if(debugMode) {
            //Display the time to detect by server
            if(step == "serverDetect") {
               System.out.println("[" + timestamp + "] Detected in " + location + " Execution Time: " + latency + "ms");
            }
            //Display the time to send detection result to client
            else if (step == "sendResult") {
               System.out.println("[" + timestamp + "] Sent result in " + location + " Execution Time: " + latency + "ms");
            }
            //Display the time to create marker
            else if(step == "createMarker") {
               System.out.println("[" + timestamp + "] Created marker in " + location + " Execution Time: " + latency + "ms");
            }
            //Total time to complete the whole detection process
            else {
               System.out.println("[" + timestamp + "] Total processing time in " + location + " Execution Time: " + latency + "ms");
            }
      }

      ObjectMapper mapper = new ObjectMapper();
      ObjectNode resultObject = mapper.createObjectNode();
      resultObject.put("timestamp", timestamp);
      resultObject.put("latency", latency);
      resultObject.put("use_server", serverUsage);
      resultObject.put("server_ip", reportedIp);
      resultObject.put("step", step);

      String result = mapper.writeValueAsString(resultObject);

      try {
            reportLock.acquire();
            reportQueue.add(result);
      } finally {
            reportLock.release();
      }
   }

   public String getReportBody(String clientId) {
      String returnedResult = "";
      try {
          reportLock.acquire();
          if(!reportQueue.isEmpty()) {
              ObjectMapper mapper = new ObjectMapper();
              ObjectNode result = mapper.createObjectNode();
              String reportFileName = "report_" + clientId + ".txt";

              result.put("path", reportFileName);
              result.put("content", String.join("", reportQueue));
              returnedResult = mapper.writeValueAsString(result);
              reportQueue.clear();
          }
      } catch (InterruptedException | JsonProcessingException e) {
          e.printStackTrace();
      } finally {
          reportLock.release();
      }
      return returnedResult;
  }
   
}