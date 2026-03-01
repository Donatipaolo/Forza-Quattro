package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application{

    @Override
    public void start(Stage stage) throws Exception {

    	GameScene gameScene = new GameScene(stage);
        gameScene.show();
    }

    public static void main(String[] args) {
    	//Creazione delle altre componenti
    	
        launch();
    }
}
