package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

import application.Controllers.GameController;
import application.Controllers.GameEndController;
import data.Queue;
import enums.GameEndResult;
import enums.GameEndInfo;

public class GameEndScene {
    
    private Scene scene;
    GameEndController controller;

    public GameEndScene(GameEndResult result, GameEndInfo extraInfo,String username,Queue sendQueue) {
        try {
            // Carica il file FXML dalle risorse
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/models/game_end.fxml"));
            Parent root = loader.load();
            
            // Crea la scena con le dimensioni specificate
            this.scene = new Scene(root);
            
            // Otteniamo il controller per passare i dati
            controller = loader.getController();
            controller.setResults(result, extraInfo);
            controller.setUsername(username);
            controller.setSendQueue(sendQueue);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }

	public GameEndController getController() {
		
		return controller;
	}
}