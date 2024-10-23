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
    private double startX, startY, endX, endY;

    private double initialX, initialY;


    /**
     * Used for preset some components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


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
            this.iwOriginalImage.setOnMouseDragged(this::onMouseDragged);
            this.iwOriginalImage.setOnMouseReleased(this::onMouseReleased);
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
        selectionRectangle.setVisible(true);
        selectionRectangle.setOnMousePressed(this::onRectanglePressed);
        selectionRectangle.setOnMouseDragged(this::onRectangleDragged);
        selectionRectangle.setOnMouseReleased(this::onRectangleReleased);

    }

    /**
     * Initializes initial coordinates when the rectangle is pressed.
     *
     * @param event the mouse event
     */
    private void onRectanglePressed(MouseEvent event) {
        initialX = selectionRectangle.getX() - event.getSceneX();
        initialY = selectionRectangle.getY() - event.getSceneY();
    }

    /**
     * Updates the rectangle's coordinates while dragging.
     *
     * @param event the mouse event
     */
    private void onRectangleDragged(MouseEvent event) {
        selectionRectangle.setX(event.getSceneX() + initialX);
        selectionRectangle.setY(event.getSceneY() + initialY);
    }

    /**
     * Calculates and sets new coordinates when the rectangle is released.
     *
     * @param event the mouse event
     */
    private void onRectangleReleased(MouseEvent event) {
        double sideLength = selectionRectangle.getHeight();
        startX = event.getSceneX() + initialX;
        endX = startX + sideLength;
        startY = event.getSceneY() + initialY;
        endY = startY + sideLength;

        selectionRectangle.setX(startX);
        selectionRectangle.setY(startY);

        cropImage();
    }

    /**
     * Handles the mouse pressed event.
     *
     * @param event the MouseEvent triggered when the mouse is pressed
     */
    private void onMousePressed(MouseEvent event) {
        initSelectionRectangle();
        startX = event.getX();
        startY = event.getY();
        selectionRectangle.setX(startX);
        selectionRectangle.setY(startY);
        selectionRectangle.setWidth(0);
        selectionRectangle.setHeight(0);
    }

    /**
     * Handles the mouse dragged event.
     *
     * @param event the MouseEvent triggered when the mouse is dragged
     */
    private void onMouseDragged(MouseEvent event) {
        double currentX = event.getX();
        double currentY = event.getY();

        double deltaX = currentX - startX;
        double deltaY = currentY - startY;
        double sideLength = Math.min(Math.abs(deltaX), Math.abs(deltaY));

        selectionRectangle.setX(startX);
        selectionRectangle.setY(startY);
        selectionRectangle.setWidth(sideLength);
        selectionRectangle.setHeight(sideLength);

        if (deltaX < 0) {
            selectionRectangle.setX(startX - sideLength);
        }
        if (deltaY < 0) {
            selectionRectangle.setY(startY - sideLength);
        }
    }

    /**
     * Handles the mouse dragged event.
     *
     * @param event the MouseEvent triggered when the mouse is dragged
     */
    private void onMouseReleased(MouseEvent event) {
        endX = event.getX();
        endY = event.getY();
        double width = Math.abs(endX - startX);
        double height = Math.abs(endY - startY);
        if (width < height) {
            endY = startY + width;
            height = width;
        } else {
            endX = height + startX;
            width = height;
        }

        selectionRectangle.setWidth(width);
        selectionRectangle.setHeight(height);
        cropImage();
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