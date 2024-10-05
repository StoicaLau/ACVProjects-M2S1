module com.example.lab_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;



    opens com.myapp.lab_2 to javafx.fxml;
    exports com.myapp.lab_2;
    exports com.myapp.lab_2.controller;
    opens com.myapp.lab_2.controller to javafx.fxml;
}