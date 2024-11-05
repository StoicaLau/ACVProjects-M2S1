package com.myapp.imageutils;

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


}
