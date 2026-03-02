package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

import application.Controllers.LobbyController;
import data.Queue;

public class LobbyScene {
    
    private Scene scene;
    private LobbyController controller;

    public LobbyScene(String username,Queue sendQueue) {
        try {
            // Carica il file FXML dalle risorse
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/models/lobby.fxml"));
            Parent root = loader.load();
            
            // Crea la scena con le dimensioni specificate
            this.scene = new Scene(root, 1000, 750);
            this.controller = loader.getController();
            this.controller.setUsername(username);
            this.controller.setSendQueue(sendQueue);
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