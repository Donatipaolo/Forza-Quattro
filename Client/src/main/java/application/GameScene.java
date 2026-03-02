package application;

import java.io.IOException;

import application.Controllers.GameController;
import data.Queue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScene{

	private Scene scene;
	private GameController controller;
	
	public GameScene(String myUsername, String enemyUsername, Queue sendQueue){
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("models/connect4.fxml"));
			Parent root = loader.load();
			
			this.scene = new Scene(root);
			this.controller = loader.getController();
			this.controller.setMyUsername(myUsername);
			this.controller.setEnemyUsername(enemyUsername);
			this.controller.setSendQueue(sendQueue);
	        scene.getStylesheets().add(getClass().getResource("css/connect4.css").toExternalForm());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Scene getScene() {
		return this.scene;
	}

	public GameController getController() {
		return controller;
	}

}
