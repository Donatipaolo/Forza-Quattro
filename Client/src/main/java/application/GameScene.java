package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameScene{

	private Stage stage;
	private Scene scene;
	
	public GameScene(Stage stage) throws IOException {
		
		this.stage = stage;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("connect4.fxml"));
        scene = new Scene(loader.load());

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Forza Quattro - Modern UI");
        stage.setScene(scene);
	}
	
	public void show() {
        stage.show();
	}

}
