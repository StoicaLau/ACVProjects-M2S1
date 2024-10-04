module com.example.lab_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;



    opens com.myapp.originalapp to javafx.fxml;
    exports com.myapp.originalapp;
    exports com.myapp.originalapp.controller;
    opens com.myapp.originalapp.controller to javafx.fxml;
}