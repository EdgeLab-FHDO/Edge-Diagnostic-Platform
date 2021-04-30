package Application.MarkerGenerator;

import Application.Utilities.ImageProcessor;
import Application.Utilities.LatencyReporter;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.opencv.aruco.*;

public class CreateMarker {
   private static LatencyReporter latencyReport = new LatencyReporter();
   public static void main(String[] args) {
      //Start measuring the time to create marker
      long startTime = System.nanoTime();
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

      Mat markerImage = new Mat();
      //TODO allow changing or loading of other Dictionaries
      Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
      Aruco.drawMarker(dictionary, 23, 200, markerImage);

      ImageProcessor.writeImage("marker.png", markerImage);
      
      //End measuring the time to create marker
      long endTime = System.nanoTime();
      //Store the time to create marker
      try {
         latencyReport.queueLatencyReport("client", false, startTime, endTime, "createMarker");
      } catch (JsonProcessingException | InterruptedException e) {
         e.printStackTrace();
      }
   }
}