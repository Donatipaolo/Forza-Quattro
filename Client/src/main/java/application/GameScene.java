package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScene{

	private Scene scene;
	private int width;
	private int height;
	
	public GameScene(){
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("models/connect4.fxml"));
			Parent root = loader.load();
			
			this.scene = new Scene(root);
			
	        scene.getStylesheets().add(getClass().getResource("css/connect4.css").toExternalForm());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Scene getScene() {
		return this.scene;
	}

}
