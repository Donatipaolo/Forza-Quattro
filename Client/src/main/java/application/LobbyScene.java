package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

import application.Controllers.LobbyController;

public class LobbyScene {
    
    private Scene scene;
    private LobbyController controller;

    public LobbyScene() {
        try {
            // Carica il file FXML dalle risorse
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/models/lobby.fxml"));
            Parent root = loader.load();
            
            // Crea la scena con le dimensioni specificate
            this.scene = new Scene(root, 1000, 750);
            this.controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }

	public LobbyController getController() {
		return controller;
	}
}