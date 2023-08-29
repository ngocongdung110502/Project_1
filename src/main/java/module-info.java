module com.example.project_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens project1 to javafx.fxml;
    exports project1;
}