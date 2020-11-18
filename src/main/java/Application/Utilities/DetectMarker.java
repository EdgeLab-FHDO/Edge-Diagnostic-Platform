package Application.Utilities;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.aruco.*;

import java.util.List;
import java.util.ArrayList;

public class DetectMarker {
   private List<Mat> corners;
   private Mat ids;

   public DetectMarker() {
      this.corners = new ArrayList<>();
      this.ids = new Mat();
   }

   public void detect(Mat subject) {
      Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);

      Aruco.detectMarkers(subject, dictionary, corners, ids);
   }

   public Mat drawDetectedMarkers(Mat subject) {
      Aruco.drawDetectedMarkers(subject, corners, ids);

      return subject;
   }

   public List<Mat> getCorners() {
      return corners;
   }

   public void setCorners(List<Mat> corners) {
      this.corners = corners;
   }

   public Mat getIds() {
      return ids;
   }

   public void setIds(Mat ids) {
      this.ids = ids;
   }
}