import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class Main {

    public static void main(String[] args) {

        new ImageProcessor().grayScale();
        new ImageProcessor().negative();
    }
}
