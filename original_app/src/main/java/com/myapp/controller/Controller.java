package com.myapp.controller;


import com.myapp.imageutils.ImageProcessor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


/**
 * Controller class
 */
public class Controller {

    /**
     * image folder path
     */
    public static final String IMAGE_FOLDER_PATH = "C:\\Users\\stoic\\Desktop\\master\\An2\\Sem1\\Advances in Computer Vision\\ACVProjects\\Images";

    @FXML
    private Button btnLoad;

    @FXML
    private ImageView iwOriginalImage;

    @FXML
    private ImageView iwResultedImage;

    /**
     * image processor
     */
    private ImageProcessor imageProcessor;

    /**
     * load original image button event
     *
     * @throws IOException if an error occurs during reading the file
     */
    @FXML
    protected void onBtnLoadOriginalImageClick() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an bmp image");

        File defaultDirectory = new File(IMAGE_FOLDER_PATH);
        fileChooser.setInitialDirectory(defaultDirectory);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("BMP Image (*.bmp)", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) this.btnLoad.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            this.imageProcessor = new ImageProcessor(selectedFile);
            this.iwOriginalImage.setImage(image);
        }

    }

    /**
     * gray scale button event
     *
     * @throws IOException if an error occurs during reading the file
     */
    @FXML
    protected void onBtnGrayScaleClick() {
        this.imageProcessor.applyGrayScale();
        Image fxImage = this.imageProcessor.getProcessedImage();
        iwResultedImage.setImage(fxImage);
    }


}