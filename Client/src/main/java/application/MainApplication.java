package application;

import java.io.IOException;

import client.ClientController;
import client.NetworkService;
import data.Queue;
import enums.GameEndInfo;
import enums.GameEndResult;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

	private static Stage primaryStage;
	
	private static NetworkService networkService;
	private static ClientController clientController;
	
    @Override
    public void start(Stage stage) throws Exception {
    	primaryStage = stage;
    	
    	if(networkService.isFailed()) {
    		showServerErrorScene();
    		return;
    	}
    	
    	showLobby("Waiting for server...",networkService.getSendQueue());
        
    	networkService.start();
    	clientController.start();
    }

    @Override
    public void stop() {  	
    	networkService.exit();
    	clientController.exit();
    }
    
    public static void main(String[] args) {
    	
    	//Inizializzazione del client
    	initClient();
    	
        // Avvia l'intero ciclo di vita dell'applicazione JavaFX
        launch(args);
    }
    
    public static void initClient() {
    	networkService = new NetworkService();
    	clientController = new ClientController(networkService.getSendQueue(),networkService.getReadQueue());
    }

    public static void showLobby(String username,Queue sendQueue) {
        LobbyScene lobby = new LobbyScene(username,sendQueue);
        primaryStage.setScene(lobby.getScene());
        primaryStage.setTitle("Game Lobby - Modern ");
        CurrentController.controller = lobby.getController();
        primaryStage.show();
    }
    
    public static void showGameEnd(GameEndResult result, GameEndInfo extraInfo,String username, Queue sendQueue) {
        GameEndScene gameEndScene = new GameEndScene(result,extraInfo,username,sendQueue);
        primaryStage.setScene(gameEndScene.getScene());
        primaryStage.setTitle("Game End");
        CurrentController.controller = gameEndScene.getController();
        primaryStage.show();
    }
    
    public static void showGame(String myUsername, String enemyUsername,Queue sendQueue){
    	GameScene gameScene = new GameScene(myUsername,enemyUsername,sendQueue);
        primaryStage.setScene(gameScene.getScene());
        primaryStage.setTitle("Game");
        CurrentController.controller = gameScene.getController();
        primaryStage.show();
    }
    
    public static void showServerErrorScene() {
    	ServerErrorScene serverErrorScene = new ServerErrorScene();
    	primaryStage.setScene(serverErrorScene.getScene());
        primaryStage.setTitle("Game");
        CurrentController.controller = serverErrorScene.getController();
        primaryStage.show();
    }
    
    public static Stage getStage() {
    	return primaryStage;
    }
}