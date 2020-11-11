package Application.Utilities;

import org.opencv.core.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.aruco.*;
import org.opencv.aruco.Dictionary.*;

public class ImageProcessor {
    public static Mat getImageMat(String filename) {
        Mat input = Imgcodecs.imread(filename);

        return input;
    }

    public static void writeImage(String filename, Mat output) {
        Imgcodecs.imwrite(filename, output);
    }
 }