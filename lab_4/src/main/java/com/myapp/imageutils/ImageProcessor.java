package com.myapp.imageutils;

import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageProcessor {

    private int limit;

    /**
     * original image
     */
    private BufferedImage firstImage;

    /**
     * processed image
     */
    private BufferedImage secondImage;

    /**
     * Constructor
     *
     * @throws IOException if an error occurs during reading the image file
     */
    public ImageProcessor() {

    }

    public void setFirstImage(File firstImage) throws IOException {
        this.firstImage = ImageIO.read(firstImage);
    }

    /**
     * Returns the processed image as a WritableImage after converting from a BufferedImage.
     *
     * @return the processed image as a WritableImage
     */
    public WritableImage getSecondImage() {
        return BufferedImageUtils.convertToWritableImage(this.secondImage);
    }

    public void setSecondImage(File secondImage) throws IOException {
        this.secondImage = ImageIO.read(secondImage);
    }

    public List<int[]> computeMotion(int blockSize, int searchZoneSize) {
        WritableRaster firstRaster = this.firstImage.getRaster();
        WritableRaster secondRaster = this.secondImage.getRaster();
        this.limit = blockSize * searchZoneSize * searchZoneSize - blockSize * searchZoneSize;
        List<int[]> resultedList = new ArrayList<>();
        int width = firstRaster.getWidth();
        int height = firstRaster.getHeight();
        for (int y1 = 0; y1 < height; y1 += blockSize) {
            for (int x1 = 0; x1 < width; x1 += blockSize) {

                int[][] block1 = getBlock(firstRaster, x1, y1, blockSize);
                if (block1 == null) {
                    continue;
                }

                int startX = Math.max(0, x1 - searchZoneSize);
                int startY = Math.max(0, y1 - searchZoneSize);
                int endX = Math.min(width - blockSize, x1 + searchZoneSize - blockSize);
                int endY = Math.min(height - blockSize, y1 + searchZoneSize - blockSize);

                int[] currentCoordinates = null;
                int min = Integer.MAX_VALUE;
                for (int y2 = startY; y2 < endY; y2 += blockSize) {
                    for (int x2 = startX; x2 < endX; x2 += blockSize) {
//                        if(y1==y2 && x2==x1){
//                            continue;
//                        }
                        int[][] block2 = getBlock(secondRaster, x2, y2, blockSize);
                        if (block2 != null) {
                            int currentValue = computeSAD(block1, block2);
                            if (currentValue <= min) {
                                min = currentValue;
                                int centerX1 = x1 + blockSize / 2;
                                int centerY1 = y1 + blockSize / 2;
                                int centerX2 = x2 + blockSize / 2;
                                int centerY2 = y2 + blockSize / 2;
                                if ((x1 == x2 && y1 == y2)) {
                                    currentCoordinates = null;
                                    break;
                                } else {
                                    if (min < limit) {
                                        currentCoordinates = null;
                                    } else {
                                        currentCoordinates = new int[]{centerX1, centerY1, centerX2, centerY2, min};
                                    }
                                }

                            }
                        }
                    }
                }
                if (currentCoordinates != null) {
                    resultedList.add(currentCoordinates);
                }
            }
        }

        for (int[] motion : resultedList) {
            System.out.printf("Block1 Center: (%d, %d) -> Block2 Center: (%d, %d) %d %n", motion[0], motion[1], motion[2], motion[3], motion[4]);
        }
        return resultedList;
    }

    private int[][] getBlock(WritableRaster raster, int x, int y, int blockSize) {
        int[][] block = new int[blockSize][blockSize];
        if (x + blockSize > raster.getWidth() || y + blockSize > raster.getHeight()) {
            return null;
        }
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
//
                block[i][j] = raster.getSample(x + i, y + j, 0);
//                System.out.print(block[i][j] + " ");
            }
//            System.out.println();
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
