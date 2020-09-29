import org.opencv.core.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.aruco.*;
import org.opencv.aruco.Dictionary.*;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CreateMarker {
   public static void main(String[] args) {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
                Size size = new Size(20.0, 20.0);
                Board board = new Board();

                Mat markerImage = new Mat();
                Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_6X6_250);
                Aruco.drawMarker(dictionary, 23, 200, markerImage);

                Imgcodecs.imwrite("marker.png", markerImage);
   }
}