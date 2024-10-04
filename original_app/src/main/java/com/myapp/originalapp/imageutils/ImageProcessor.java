package com.myapp.originalapp.imageutils;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

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
        return this.convertToWritableImage(this.processedImage);
    }

    /**
     * Applies a grayscale filter to the processed image.
     */
    public void applyGrayScale() {
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
     * Converts a BufferedImage to a WritableImage for use with JavaFX.
     *
     * @param bufferedImage the BufferedImage to convert
     * @return a WritableImage representation of the given BufferedImage
     */
    private WritableImage convertToWritableImage(BufferedImage bufferedImage) {
        WritableImage writableImage = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int argb = bufferedImage.getRGB(x, y);
                int alpha = (argb >> 24) & 0xff;
                int red = (argb >> 16) & 0xff;
                int green = (argb >> 8) & 0xff;
                int blue = argb & 0xff;
                pixelWriter.setColor(x, y, Color.rgb(red, green, blue, alpha / 255.0));
            }
        }

        return writableImage;
    }

}
