module project1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens project1 to javafx.fxml;
    exports project1;
}