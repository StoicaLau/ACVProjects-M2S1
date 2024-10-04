package com.example.lab_1;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;


public class Controller {

    private BufferedImage bi, biFiltered;

    @FXML
    private Button btnLoad;

    @FXML
    private ImageView iwOriginalImage;

    @FXML
    private ImageView iwResultedImage;

    @FXML
    protected void onBtnLoadOriginalImageClick() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an bmp image");

        File defaultDirectory = new File("C:\\Users\\stoic\\Desktop\\master\\An2\\Sem1\\Advances in Computer Vision\\lab\\lab_1\\Images");
        fileChooser.setInitialDirectory(defaultDirectory);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP Image (*.bmp)", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) this.btnLoad.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            bi = ImageIO.read(selectedFile);
            biFiltered=bi;

            this.iwOriginalImage.setImage(image);
        }

    }

    @FXML
    protected void onBtnGrayScaleImageClick() throws IOException {

        WritableRaster raster = biFiltered.getRaster();
        for (int i = 0; i < biFiltered.getWidth(); i++) {
            for (int j = 0; j < biFiltered.getHeight(); j++) {
                int[] rgb = raster.getPixel(i, j, (int[]) null);
                int intensity = (rgb[0] + rgb[1] + rgb[2]) / 3;
                raster.setPixel(i, j, new int[]{intensity, intensity,
                        intensity});
            }
        }

        BufferedImage bufferedImage = biFiltered;
        Image fxImage = convertToFxImage(bufferedImage);

        iwResultedImage.setImage(fxImage);

    }

    public WritableImage convertToFxImage(BufferedImage bufferedImage) {
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

    public BufferedImage convertToBufferedImage(Image fxImage) {
        int width = (int) fxImage.getWidth();
        int height = (int) fxImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        WritableRaster raster = bufferedImage.getRaster();
        PixelReader pixelReader = fxImage.getPixelReader();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                javafx.scene.paint.Color color = pixelReader.getColor(x, y);
                int argb = ((int) (color.getOpacity() * 255) << 24) |
                        ((int) (color.getRed() * 255) << 16) |
                        ((int) (color.getGreen() * 255) << 8) |
                        ((int) (color.getBlue() * 255));
                raster.setPixel(x, y, new int[]{argb});
            }
        }

        return bufferedImage;
    }


}