import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Main {

    public static void main(String[] args) throws IOException {

        ImageProcessor imageProcess = new ImageProcessor();

        imageProcess.grayScale();
        imageProcess.negative();
        imageProcess.setGamma(0.2);
        imageProcess.setGamma(2);
        imageProcess.contrast(2);
        imageProcess.saltAndPepper(0.9f);
        imageProcess.binary();
    }
}
