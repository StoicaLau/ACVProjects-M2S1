package com.myapp.inpainting.controller;


import javafx.collections.ObservableList;
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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.myapp.inpainting.imageutils.ImageProcessor;

/**
 * Controller class
 */
public class Controller implements Initializable {

    /**
     * image folder path
     */
    public static final String IMAGE_FOLDER_PATH = "C:\\Users\\PC\\Documents\\GitHub\\ACVProjects-M2S1\\Images";

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

    private Polygon polygon;
    private List<int[]> points;

    /**
     * Used for preset some components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.polygon = new Polygon(); 
        this.polygon.setStroke(Color.RED); 
        this.polygon.setFill(Color.TRANSPARENT); 
        this.polygon.setStrokeWidth(2); 
        this.points = new ArrayList<>(); 
        this.pOriginalImage.getChildren().add(this.polygon);
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
     * Handles the mouse pressed event.
     *
     * @param event the MouseEvent triggered when the mouse is pressed
     */
    private void onMousePressed(MouseEvent event) {
        
        int x = (int) event.getX(); 
        int y = (int) event.getY(); 
        this.points.add(new int[]{x, y});
        redrawPolygon();
    }

    private void redrawPolygon() { 
       
            this.polygon.setVisible(false);
            this.polygon.getPoints().clear(); 
            for (int[] point : points) 
            { 
                this.polygon.getPoints().addAll((double) point[0], (double) point[1]); 
                System.out.println();
            }
            this.polygon.setVisible(true);
            System.out.println();
        
             // Close the polygon by connecting the last point to the first 
            //  polyline.getPoints().add(points.get(0)); 
            //  polyline.getPoints().add(points.get(1)); 
            
        
    
    }

} 

