package application;

import java.io.IOException;

import application.Controllers.GameController;
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
    	
    	stage.getIcons().add(new javafx.scene.image.Image(
    	        getClass().getResourceAsStream("/ForzaQuattroIcon.png")
    	    ));
    	
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
        primaryStage.setTitle("ForzaQuattro - Lobby");
        CurrentController.controller = lobby.getController();
        primaryStage.setHeight(750);
        primaryStage.setWidth(1000);
        primaryStage.setMinHeight(750);
        primaryStage.setMinWidth(1000);
        primaryStage.show();
    }
    
    public static void showGameEnd(GameEndResult result, GameEndInfo extraInfo,String username, Queue sendQueue) {
    	GameEndScene gameEndScene = new GameEndScene(result,extraInfo,username,sendQueue);
        primaryStage.setScene(gameEndScene.getScene());
        primaryStage.setTitle("ForzaQuattro - Game");
        CurrentController.controller = gameEndScene.getController();
        primaryStage.setHeight(700);
        primaryStage.setWidth(700);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }
    
    public static void showGame(String myUsername, String enemyUsername,Queue sendQueue){
    	GameScene gameScene = new GameScene(myUsername,enemyUsername,sendQueue);
        primaryStage.setScene(gameScene.getScene());
        primaryStage.setTitle("ForzaQuattro - Game");
        CurrentController.controller = gameScene.getController();
        primaryStage.setHeight(800);
        primaryStage.setWidth(800);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }
    
    public static void showServerErrorScene() {
    	ServerErrorScene serverErrorScene = new ServerErrorScene();
    	primaryStage.setScene(serverErrorScene.getScene());
        primaryStage.setTitle("ForzaQuattro - Game");
        CurrentController.controller = serverErrorScene.getController();
        primaryStage.setHeight(700);
        primaryStage.setWidth(700);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }
    
    public static Stage getStage() {
    	return primaryStage;
    }
}