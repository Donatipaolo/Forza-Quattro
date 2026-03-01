package application;

import java.io.IOException;

import enums.GameEndInfo;
import enums.GameEndResult;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

	private static Stage primaryStage;
	
    @Override
    public void start(Stage stage) throws Exception {
    	primaryStage = stage;
    	showServerErrorScene();
        
    }

    public static void main(String[] args) {
    	
    	//Inizializzazione del client
    	initClient();
    	
        // Avvia l'intero ciclo di vita dell'applicazione JavaFX
        launch(args);
    }
    
    public static void initClient() {
    	//Avvio
    }

    public static void showLobby() {
        LobbyScene lobby = new LobbyScene();
        primaryStage.setScene(lobby.getScene());
        primaryStage.setTitle("Game Lobby - Modern ");
        primaryStage.show();
    }
    
    public static void showGameEnd(GameEndResult result, GameEndInfo extraInfo) {
        GameEndScene gameEndScene = new GameEndScene(result,extraInfo);
        primaryStage.setScene(gameEndScene.getScene());
        primaryStage.setTitle("Game End");
        primaryStage.show();
    }
    
    public static void showGame(){
    	GameScene gameScene = new GameScene();
        primaryStage.setScene(gameScene.getScene());
        primaryStage.setTitle("Game");
        primaryStage.show();
    }
    
    public static void showServerErrorScene() {
    	ServerErrorScene serverErrorScene = new ServerErrorScene();
    	primaryStage.setScene(serverErrorScene.getScene());
        primaryStage.setTitle("Game");
        primaryStage.show();
    }
}