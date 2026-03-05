module forza_quattro_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    exports application;
    opens application to javafx.fxml;
}