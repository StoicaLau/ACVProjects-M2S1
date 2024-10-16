package com.myapp.originalapp.imageutils;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class BufferedImageUtils {

    /**
     * Converts a BufferedImage to a WritableImage for use with JavaFX.
     *
     * @param bufferedImage the BufferedImage to convert
     * @return a WritableImage representation of the given BufferedImage
     */
    public static WritableImage convertToWritableImage(BufferedImage bufferedImage) {
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

    /**
     * Creates a copy of the given {@link BufferedImage}.
     *
     * @param bufferImage the {@link BufferedImage} to be cloned
     * @return a new {@link BufferedImage} that is a copy of the input image
     * @throws NullPointerException if the input image is null
     */
    public static BufferedImage cloneBufferedImage(BufferedImage bufferImage) {
        ColorModel colorModel = bufferImage.getColorModel();
        WritableRaster raster = bufferImage.copyData(null);
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }
}
