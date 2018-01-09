import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.Buffer;
import java.util.*;
import java.util.List;


public class ImageProcessor {

    private int maxGrayScaleValue;
    private int minGrayScaleValue;
    private int summaryGrayScaleValue;
    private int averageGrayScaleValue;


    public void grayScale(){

        List<Integer> pixelArray = new ArrayList<>();
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
                    pixelArray.add(grayValue);
                    int newPixel = colorToRGB(255, grayValue, grayValue, grayValue);
                    grayScaleImage.setRGB(i, j, newPixel);

                }

            }

            Collections.sort(pixelArray);
            this.maxGrayScaleValue = pixelArray.get(pixelArray.size() - 1);
            this.minGrayScaleValue = pixelArray.get(0);
            this.summaryGrayScaleValue = pixelArray.stream().mapToInt(Integer::intValue).sum();
            this.averageGrayScaleValue = (int) (summaryGrayScaleValue / pixelArray.size());

            System.out.println("min " + minGrayScaleValue + " max " + maxGrayScaleValue);

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

    public void setGamma(double gammaValue) throws IOException {

        double gamma = gammaValue;
        File grayScaleImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\grayScaleChou.jpg");

        BufferedImage bufferedImage = null;


        try{

            bufferedImage = ImageIO.read(grayScaleImage);

        }catch (IOException e){
            e.printStackTrace();
        }

        //Set the gamma parameter into each pixel
        // Gamma function -> output = input ^ gamma
        for (int y = 0; y < bufferedImage.getHeight(); y++){
            for (int x = 0; x < bufferedImage.getWidth(); x++){

                int pixel = bufferedImage.getRGB(x, y);

                float floatRed = ((pixel & 0xff0000)>> 16) / 255f;
                float floatGreen = ((pixel & 0xff00)>> 8) / 255f;
                float floatBlue = ((pixel & 0xff)) / 255f;

                int gammaRed = (int) Math.round(Math.pow(floatRed, gamma) * 255);
                int gammaGreen = (int) Math.round(Math.pow(floatGreen, gamma) * 255);
                int gammaBlue = (int) Math.round(Math.pow(floatBlue, gamma) * 255);

                if (gammaRed > 255) gammaRed = 255;
                if (gammaGreen > 255) gammaGreen = 255;
                if (gammaBlue > 255) gammaBlue = 255;

                int gammaPixel = colorToRGB(255, gammaRed, gammaGreen, gammaBlue);
                bufferedImage.setRGB(x, y, gammaPixel);
            }
        }

        if (gamma < 1){

            ImageIO.write(bufferedImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\lowGammaChou.jpg"));
        }else{

            ImageIO.write(bufferedImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\highGammaChou.jpg"));
        }

    }

    public void contrast(double gammaValue) throws IOException {

        double gamma = gammaValue;
        File grayScaleImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\grayScaleChou.jpg");

        BufferedImage bufferedImage = null;


        try{

            bufferedImage = ImageIO.read(grayScaleImage);

        }catch (IOException e){
            e.printStackTrace();
        }

        //Set the gamma parameter into each pixel
        // Gamma function -> output = input ^ gamma
        for (int y = 0; y < bufferedImage.getHeight(); y++){
            for (int x = 0; x < bufferedImage.getWidth(); x++){

                int pixel = bufferedImage.getRGB(x, y);

                float floatRed = (((pixel & 0xff0000)>> 16) - minGrayScaleValue) / (float)(maxGrayScaleValue - minGrayScaleValue);
                float floatGreen = (((pixel & 0xff00)>> 8) - minGrayScaleValue) / (float)(maxGrayScaleValue - minGrayScaleValue);
                float floatBlue = (((pixel & 0xff)) - minGrayScaleValue) / (float)(maxGrayScaleValue - minGrayScaleValue);

                int gammaPixel = colorToRGB(255, (int) Math.round(Math.pow(floatRed, gamma) * 255), (int) Math.round(Math.pow(floatGreen, gamma) * 255), (int) Math.round(Math.pow(floatBlue, gamma) * 255));
                bufferedImage.setRGB(x, y, gammaPixel);
            }
        }

        ImageIO.write(bufferedImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\contrastChou.jpg"));


    }

    public void saltAndPepper(float rate) throws IOException{

        File lowGammaScaleImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\lowGammaChou.jpg");
        BufferedImage bufferedImage = null;
        Random rand = new Random();

        try{
            bufferedImage = ImageIO.read(lowGammaScaleImage);
        }catch (IOException e){
            e.printStackTrace();
        }

        BufferedImage processImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        for (int x = 0; x < bufferedImage.getWidth(); x++){
            for (int y = 0; y < bufferedImage.getHeight(); y++){

                int pixel = bufferedImage.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                if (rand.nextFloat() > rate){

                    if (rand.nextFloat() > 0.5f){

                        int newPixel = colorToRGB(255, 255, 255, 255);
                        processImage.setRGB(x, y, newPixel);
                    }else{
                        int newPixel = colorToRGB(255, 0, 0, 0);
                        processImage.setRGB(x, y, newPixel);
                    }

                }else{

                    int newPixel = colorToRGB(255, red, green, blue);
                    processImage.setRGB(x, y, newPixel);
                }

            }
        }

        ImageIO.write(processImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\saltAndPepperChou.jpg"));



    }

    public void binary() throws IOException{

        File highGammaImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\highGammaChou.jpg");
        BufferedImage bufferedImage = null;

        try{
            bufferedImage = ImageIO.read(highGammaImage);
        }catch (IOException e){
            e.printStackTrace();
        }

        BufferedImage processImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());

        for (int x = 0; x < bufferedImage.getWidth(); x++){
            for (int y = 0; y < bufferedImage.getHeight(); y++){

                int pixel = bufferedImage.getRGB(x, y);
                int blue = pixel & 0xff;

                if (blue > averageGrayScaleValue){
                    int newPixel = colorToRGB(255, 255, 255, 255);
                    processImage.setRGB(x, y, newPixel);
                }else {
                    int newPixel = colorToRGB(255, 0, 0, 0);
                    processImage.setRGB(x, y, newPixel);
                }

            }
        }

        ImageIO.write(processImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\binaryChou.jpg"));

    }

    public void Filter(String filterType) throws IOException{

        File importImage = null;

        if (filterType == "Median"){
            importImage = new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\saltAndPepperChou.jpg");
        }

        BufferedImage bufferedImage = null;

        try{
            bufferedImage = ImageIO.read(importImage);
        }catch (IOException e){
            e.printStackTrace();
        }

        BufferedImage processImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        for (int x = 0; x < bufferedImage.getWidth() - 2; x++){
            for (int y = 0; y < bufferedImage.getHeight() - 2; y++){

                int blue[] = new int[9];

                for (int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++){
                        int pixel = bufferedImage.getRGB(i + x, j + y);
                        blue[i + 3 * j] = (pixel & 0xff);
                    }
                }

                Arrays.sort(blue);
                if (filterType == "Median"){
                    int newPixel = colorToRGB(255, blue[4], blue[4], blue[4]);
                    processImage.setRGB(x + 1, y + 1, newPixel);
                }else{
                    int newPixel = colorToRGB(255, blue[8], blue[8], blue[8]);
                    processImage.setRGB(x + 1, y + 1, newPixel);
                }

            }
        }

        if (filterType == "Median"){
            ImageIO.write(processImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\medianFilter.jpg"));
        }else{
            ImageIO.write(processImage, "jpeg", new File("C:\\Users\\Fang Wei Hsu\\IdeaProjects\\ImageProcess\\src\\OutputImage\\maxFilter.jpg"));
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
