import Application.Utilities.ImageProcessor;
import org.opencv.core.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.aruco.*;
import org.opencv.aruco.Dictionary.*;

public class CreateMarker {
   public static void main(String[] args) {
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

      Mat markerImage = new Mat();
      Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
      Aruco.drawMarker(dictionary, 23, 200, markerImage);

      ImageProcessor.writeImage("marker.png", markerImage);
   }
}