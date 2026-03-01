package application.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class ServerErrorController {

    @FXML
    private void handleCloseApp() {
        // Chiude l'intera applicazione in modo pulito
        Platform.exit();
        System.exit(0);
    }
}