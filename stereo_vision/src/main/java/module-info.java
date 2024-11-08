module com.example.lab_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;



    opens com.myapp to javafx.fxml;
    exports com.myapp;
    exports com.myapp.controller;
    opens com.myapp.controller to javafx.fxml;
}