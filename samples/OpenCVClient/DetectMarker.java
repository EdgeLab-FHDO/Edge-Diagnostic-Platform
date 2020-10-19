import org.opencv.core.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.aruco.*;
import org.opencv.aruco.Dictionary.*;

import java.util.List;
import java.util.ArrayList;

public class DetectMarker {
   private Mat subject;
   private List<Mat> corners;
   private Mat ids;

   public DetectMarker(Mat subject) {
      this.subject = subject;
      this.corners = new ArrayList();
      this.ids = new Mat();
   }

   public void detect() {
      Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);

      Aruco.detectMarkers(subject, dictionary, corners, ids);
   }

   public Mat drawDetectedMarkers() {
      Aruco.drawDetectedMarkers(subject, corners, ids);

      return subject;
   }

   public static void initOpenCVSharedLibrary() {
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
   }
}