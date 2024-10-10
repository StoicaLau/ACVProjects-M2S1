package com.myapp.lab_2.imageutils;

import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;


public class ImageProcessor {

    /**
     * original image
     */
    private final BufferedImage originalImage;

    /**
     * processed image
     */
    private BufferedImage processedImage;


    /**
     * Constructor
     *
     * @param imageFile the image file to be loaded and processed
     * @throws IOException if an error occurs during reading the image file
     */
    public ImageProcessor(File imageFile) throws IOException {
        this.originalImage = ImageIO.read(imageFile);
        this.processedImage = ImageIO.read(imageFile);
    }

    /**
     * Returns the processed image as a WritableImage after converting from a BufferedImage.
     *
     * @return the processed image as a WritableImage
     */
    public WritableImage getProcessedImage() {
        return BufferedImageUtils.convertToWritableImage(this.processedImage);
    }

    /**
     * Applies a grayscale filter to the processed image.
     */
    public void applyGrayScale() {
        this.processedImage = BufferedImageUtils.cloneBufferedImage(this.originalImage);
        WritableRaster raster = this.processedImage.getRaster();
        for (int i = 0; i < this.processedImage.getWidth(); i++) {
            for (int j = 0; j < this.processedImage.getHeight(); j++) {
                int[] rgb = raster.getPixel(i, j, (int[]) null);
                int intensity = (rgb[0] + rgb[1] + rgb[2]) / 3;
                raster.setPixel(i, j, new int[]{intensity, intensity,
                        intensity});
            }
        }
    }

    /**
     * Crops a portion of the original image defined by the rectangle formed between the given start (X, Y) and end (X, Y) coordinates.
     *
     * @param startX the X-coordinate of the starting point of the selection
     * @param startY the Y-coordinate of the starting point of the selection
     * @param endX   the X-coordinate of the ending point of the selection
     * @param endY   the Y-coordinate of the ending point of the selection
     */
    public void cropImage(double startX, double startY, double endX, double endY) {
        int width = (int) Math.abs(endX - startX);
        int height = (int) Math.abs(endY - startY);
        int x = (int) Math.min(startX, endX);
        int y = (int) Math.min(startY, endY);

        BufferedImage croppedImage = originalImage.getSubimage(x, y, width, height);
        this.processedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );
        this.processedImage.createGraphics().drawImage(croppedImage, x, y, null);

    }


}
