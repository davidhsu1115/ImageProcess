import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;

public class ImageProcessor {

    public void grayScale(){

        File originalImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\chou1.jpg");

        BufferedImage bufferedImage = null;
        try{

            bufferedImage = ImageIO.read(originalImage);

            // Image for gray scale image result
            BufferedImage grayScaleImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());

            //Get RGB on each pixel
            for (int i = 0; i < bufferedImage.getWidth(); i++){
                for (int j = 0; j < bufferedImage.getHeight(); j++){
                    int color = bufferedImage.getRGB(i, j); // j,i
                    int r = (color >> 16) & 0xff;
                    int g = (color >> 8) & 0xff;
                    int b = color & 0xff;

                    //change original pixel to gray scale value
                    int grayValue = (int)(r * 0.299 + g * 0.587 + b * 0.114);
                    int newPixel = colorToRGB(255, grayValue, grayValue, grayValue);
                    grayScaleImage.setRGB(i, j, newPixel);
                }

            }

            //Write to file
            ImageIO.write(grayScaleImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\grayScaleChou.jpg"));

        }catch (IOException e){

            e.printStackTrace();
        }

    }

    public void negative(){

        BufferedImage bufferedImage = null;
        File grayImage = null;

        try{
            grayImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\grayScaleChou.jpg");
            bufferedImage = ImageIO.read(grayImage);
        }catch (IOException e){
            e.printStackTrace();
        }

        //Get image height and weight
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        //Transform to negative image
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x ++){

                int pixel = bufferedImage.getRGB(x, y);

                int alpha = (pixel >> 24)&0xff;
                int red = (pixel >> 16)&0xff;
                int green = (pixel >> 8)&0xff;
                int blue = pixel&0xff;

                red = 255 - red;
                green = 255 - green;
                blue = 255 - blue;

                //set new RGB value;
                pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;

                bufferedImage.setRGB(x, y, pixel);

            }
        }

        //write Image
        try{
            grayImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\nagativeChou.jpg");
            ImageIO.write(bufferedImage, "jpg", grayImage);
        }catch (IOException e){
            e.printStackTrace();
        }


    }

    private int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }

}
