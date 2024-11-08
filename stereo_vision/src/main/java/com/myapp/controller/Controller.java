package com.myapp.controller;


import com.myapp.imageutils.ImageProcessor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.shape.Line;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Controller class
 */
public class Controller implements Initializable {

    /**
     * image folder path
     */
    public static final String IMAGE_FOLDER_PATH = "C:\\Users\\stoic\\Desktop\\master\\An2\\Sem1\\Advances in Computer Vision\\ACVProjects\\stereo_vision\\input";

    @FXML
    private Button btnLoadFirstFrame;

    @FXML
    private ImageView iwLeftImage;

    @FXML
    private ImageView iwRightImage;

    @FXML
    private ImageView iwResultedImage;



    @FXML
    private TextField tfBlockSize;
    @FXML
    private TextField tfZoneSize;

    @FXML
    private Pane pFirstImage;

    /**
     * image processor
     */
    private ImageProcessor imageProcessor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.imageProcessor = new ImageProcessor();
        tfBlockSize.setOnAction(event -> this.computeMotion());
        tfZoneSize.setOnAction(event -> this.computeMotion());
    }

    /**
     * load original image button event
     *
     * @throws IOException if an error occurs during reading the file
     */
    @FXML
    protected void onBtnLoadFirstFrameClick() throws IOException {

        this.clearMotionLines();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an  gif image");

        File defaultDirectory = new File(IMAGE_FOLDER_PATH);
        fileChooser.setInitialDirectory(defaultDirectory);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GIF Image (*.gif)", "*.gif");
        ;
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) this.btnLoadFirstFrame.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            this.imageProcessor.setLeftImage(selectedFile);
            this.iwLeftImage.setImage(image);
        }

    }

    /**
     * load original image button event
     *
     * @throws IOException if an error occurs during reading the file
     */
    @FXML
    protected void onBtnLoadSecondFrameClick() throws IOException {
        this.clearMotionLines();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an  gif image");

        File defaultDirectory = new File(IMAGE_FOLDER_PATH);
        fileChooser.setInitialDirectory(defaultDirectory);

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("GIF Image (*.gif)", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = (Stage) this.btnLoadFirstFrame.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            this.imageProcessor.setRightImage(selectedFile);
            this.iwRightImage.setImage(image);
        }

    }

    @FXML
    protected void computeMotion() {
        int blockSize = Integer.parseInt(this.tfBlockSize.getText());
        int zoneSize = Integer.parseInt(this.tfZoneSize.getText());
        this.imageProcessor.computeMotion(blockSize, zoneSize);
        this.iwResultedImage.setImage(this.imageProcessor.getImage());

    }

    public void drawMotionLines(List<int[]> coordinatesList) {
        this.clearMotionLines();
        for (int[] coordinates : coordinatesList) {
            int x1 = coordinates[0];
            int y1 = coordinates[1];
            int x2 = coordinates[2];
            int y2 = coordinates[3];


            Line line = new Line(x1, y1, x2, y2);


            line.setStroke(Color.RED);
            line.setStrokeWidth(2);


            this.pFirstImage.getChildren().add(line);
        }
    }

    private void clearMotionLines() {
        pFirstImage.getChildren().removeIf(node -> node instanceof Line);
    }


}