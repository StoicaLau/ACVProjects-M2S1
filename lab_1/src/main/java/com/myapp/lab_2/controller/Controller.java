package com.myapp.lab_2.controller;


import com.myapp.lab_2.imageutils.ImageProcessor;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller class
 */
public class Controller implements Initializable {

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

    @FXML
    private Pane pOriginalImage;

    /**
     * image processor
     */
    private ImageProcessor imageProcessor;

    /**
     * crop border
     */
    private Rectangle selectionRectangle;

    /**
     * rectangle coordinates
     */
    private Double startX, startY, endX, endY;

    /**
     * if is second click
     */
    private boolean isSecondClick;


    /**
     * Used for preset some components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.isSecondClick = false;

    }


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

            this.initSelectionRectangle();
            this.iwOriginalImage.setOnMousePressed(this::onMousePressed);
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
        this.iwResultedImage.setImage(fxImage);
    }

    /**
     * Initializes the selection rectangle used for selecting an area on the original image.
     */
    private void initSelectionRectangle() {
        if (this.selectionRectangle != null) {
            this.selectionRectangle.setVisible(false);
        }
        selectionRectangle = new Rectangle();
        selectionRectangle.setStroke(Color.BLUE);
        selectionRectangle.setFill(Color.TRANSPARENT);
        selectionRectangle.setStrokeWidth(2);
        pOriginalImage.getChildren().add(selectionRectangle);
        selectionRectangle.setWidth(0);
        selectionRectangle.setHeight(0);
        selectionRectangle.setVisible(true);

    }

    /**
     * Handles the mouse pressed event.
     *
     * @param event the MouseEvent triggered when the mouse is pressed
     */
    private void onMousePressed(MouseEvent event) {

        if (!this.isSecondClick) {
            this.startX = event.getX();
            this.startY = event.getY();
            if (this.endX != null && this.endY != null) {
                this.selectionRectangle.setX(this.startX);
                this.selectionRectangle.setY(this.startY);
            }
        }

        if (this.isSecondClick) {
            this.endX = event.getX();
            this.endY = event.getY();
        }

        if (this.startX != null && this.startY != null && this.endX != null && this.endY != null) {
            this.selectionRectangle.setVisible(false);
            this.selectionRectangle.setX(Math.min(this.startX, this.endX));
            this.selectionRectangle.setY(Math.min(this.startY, this.endY));
            this.selectionRectangle.setWidth(Math.abs(this.endX - this.startX));
            this.selectionRectangle.setHeight(Math.abs(this.endY - this.startY));
            this.selectionRectangle.setVisible(true);

            cropImage();
        }

        this.isSecondClick = !this.isSecondClick;
    }

    /**
     * Crops the original image based on the selection rectangle defined by
     * the starting and ending (X and Y) coordinates.
     */
    private void cropImage() {
        if (imageProcessor == null) {
            return;
        }

        imageProcessor.cropImage(startX, startY, endX, endY);
        Image fxImage = this.imageProcessor.getProcessedImage();

        iwResultedImage.setImage(fxImage);
    }


}