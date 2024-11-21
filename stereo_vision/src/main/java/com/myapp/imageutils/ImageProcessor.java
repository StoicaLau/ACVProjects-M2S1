package com.myapp.imageutils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * vad cu cat sa deplasat
 * matrice cu deplasare
 * vad min si care e maximul
 * unde e maximul acolo e deplepasre in alb
 * iar minimul cu negru ,tot restul se face o nuanta
 * imaginea generata poata fi in nivele de gri
 * matricea o sa aiba dimensiune imagini
 * /15
 */

public class ImageProcessor {

    private int[][] matrix;
    /**
     * original image
     */
    private BufferedImage leftImage;
    /**
     * processed image
     */
    private BufferedImage rightImage;

    /**
     * Constructor
     *
     * @throws IOException if an error occurs during reading the image file
     */
    public ImageProcessor() {

    }

    public void setLeftImage(File leftImage) throws IOException {
        this.leftImage = ImageIO.read(leftImage);

        System.out.println(this.leftImage.getWidth());
        System.out.println(this.leftImage.getHeight());
        this.matrix = new int[this.leftImage.getHeight()][this.leftImage.getWidth()];
    }

    /**
     * Returns the processed image as a WritableImage after converting from a BufferedImage.
     *
     * @return the processed image as a WritableImage
     */
    public WritableImage getRightImage() {
        return BufferedImageUtils.convertToWritableImage(this.rightImage);
    }


    public void setRightImage(File rightImage) throws IOException {
        this.rightImage = ImageIO.read(rightImage);
        boolean isGrayscale = this.rightImage.getType() == BufferedImage.TYPE_BYTE_GRAY;
        System.out.println(isGrayscale);
        this.matrix = new int[this.rightImage.getHeight()][this.rightImage.getWidth()];
    }

    public void computeMotion(int blockSize, int searchZoneSize) {
        Instant start = Instant.now();
        this.matrix = new int[this.rightImage.getHeight()][this.rightImage.getWidth()];

        int width = this.leftImage.getWidth();
        int height = this.leftImage.getHeight();

        for (int y1 = 0; y1 < height-blockSize; y1 += 1) {
            for (int x1 = 0; x1 < width-blockSize; x1 += 1) {

                int[][] block1 = getBlock(this.leftImage, x1, y1, blockSize);
                if (block1 == null) {
                    continue;
                }

                int startX = Math.max(0, x1 - searchZoneSize);
                int min = Integer.MAX_VALUE;
                int resultedX = x1;

                for (int x2 = x1; x2 >= startX; x2 -= 1) {
//
                    int[][] block2 = getBlock(this.rightImage, x2, y1, blockSize);
                    if (block2 != null) {
                        int currentValue = computeSAD(block1, block2);
                        if (currentValue < min) {
                            min = currentValue;
                            resultedX = x2;
                        }
                        
                    }
                    if(min==0){
                        x2=startX-1;
                    }
                }

                this.matrix[y1][x1] = x1 - resultedX;
            }

        }
        Instant end = Instant.now(); 
        Duration timeElapsed = Duration.between(start, end); 
        System.out.println("Timpul de executie: " + timeElapsed.getSeconds() + " secunde");
    
    } 
    
 
    

    public Image getImage() {
        int width = this.leftImage.getWidth();
        int height = this.leftImage.getHeight();

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix[y][x] < min) {
                    min = matrix[y][x];
                }
                if (matrix[y][x] > max) {
                    max = matrix[y][x];
                }
            }
        }
        System.out.println(min);
        System.out.println(max);


        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();


        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = matrix[y][x];
                Color color;

                if (value == min) {
                    color = Color.BLACK;
                } else if (value == max) {
                    color = Color.WHITE;
                } else {
                    double grayScale = (double) (value - min) / (max - min);
                    color = new Color(grayScale,grayScale,grayScale,1);
                }
                pixelWriter.setColor(x, y, color);
            }
        }

        return image;
    }

    private int[][] getBlockGIF(BufferedImage image, int x, int y, int blockSize) {
        WritableRaster raster = image.getRaster();
        int[][] block = new int[blockSize][blockSize];
        if (x + blockSize > raster.getWidth() || y + blockSize > raster.getHeight()) {
            return null; // Ensure block doesn't exceed image bounds
        }

        IndexColorModel colorModel = (IndexColorModel) image.getColorModel();
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {

                int currentX = x + j;
                int currentY = y + i;
                int index = raster.getSample(currentX, currentY, 0);

                // Retrieve the RGB values from the color palette using the index
                int red = colorModel.getRed(index);
                int green = colorModel.getGreen(index);
                int blue = colorModel.getBlue(index);

                // Convert the RGB values to a grayscale value
                int gray = (red + green + blue) / 3;
                block[i][j] = gray;

            }
        }
        return block;
    }

    private int[][] getBlock(BufferedImage image, int x, int y, int blockSize) {
        WritableRaster raster = image.getRaster();
        int[][] block = new int[blockSize][blockSize];
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {

                int currentX = x + j;
                int currentY = y + i;

                int[] rgb = raster.getPixel(currentX, currentY, (int[]) null);
                int gray= (rgb[0] + rgb[1] + rgb[2]) / 3;
                block[i][j] = gray;

            }
        }
        return block;
    }


    private int computeSAD(int[][] block1, int[][] block2) {
        int sum = 0;
        for (int i = 0; i < block1.length; i++) {
            for (int j = 0; j < block1[0].length; j++) {
                sum += Math.abs(block1[i][j] - block2[i][j]);
            }
        }
        return sum;
    }


}
