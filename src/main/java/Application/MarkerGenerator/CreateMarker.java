package Application.MarkerGenerator;

import Application.Utilities.ImageProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.aruco.*;

public class CreateMarker {
   public static void main(String[] args) {
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

      Mat markerImage = new Mat();
      //TODO allow changing or loading of other Dictionaries
      Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
      Aruco.drawMarker(dictionary, 23, 200, markerImage);

      ImageProcessor.writeImage("marker.png", markerImage);
   }
}