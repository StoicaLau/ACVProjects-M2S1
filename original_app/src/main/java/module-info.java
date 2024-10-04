module com.example.lab_1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;



    opens com.example.lab_1 to javafx.fxml;
    exports com.example.lab_1;
}