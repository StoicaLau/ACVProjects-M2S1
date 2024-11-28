module com.myapp.inpainting {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;



    opens com.myapp.inpainting to javafx.fxml;
    exports com.myapp.inpainting;
    exports com.myapp.inpainting.controller;
    opens com.myapp.inpainting.controller to javafx.fxml;
}