package application.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import protocol.Move;
import enums.GameEndResult;


import application.MainApplication;
import application.Notification;
import client.NetworkService;
import data.Queue;
import enums.GameEndInfo;

enum Color{
red, //Style giocatore 1
	yellow //Style giocatore 2
}

public class GameController {

    @FXML
    private GridPane grid;
    
    @FXML
    private Label opponentNameLabel;
    
    @FXML
    private Label turnStatusLabel;
    
    private Queue sendQueue;
    private int lastMove;

    //La dimensione della griglia
    private static final int ROWS = 6;
    private static final int COLS = 7;

    //La griglia da stampare
    private StackPane[][] cells = new StackPane[ROWS][COLS];

    private boolean gameFinished = false;
    private boolean myTurn;
    private Color myColor;
    private Color otherColor;
	private String myUsername;
	private String enemyUsername;
    
    @FXML
    public void initialize() {
        createGrid();
    }

    public void updateGameStatus(boolean isMyTurn) {
        Platform.runLater(() -> {
            opponentNameLabel.setText(enemyUsername);
            
            if (isMyTurn) {
                turnStatusLabel.setText("YOUR TURN");
                turnStatusLabel.setStyle("-fx-text-fill: #00ff00;"); // Verde per il tuo turno
            } else {
                turnStatusLabel.setText("OPPONENT'S TURN");
                turnStatusLabel.setStyle("-fx-text-fill: #ff4444;"); // Rosso per l'avversario
            }
        });
    }
    
    private void createGrid() {

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                StackPane cell = new StackPane();
                cell.getStyleClass().add("cell");

                Circle hole = new Circle();
                hole.getStyleClass().add("hole");

                hole.radiusProperty().bind(cell.widthProperty().divide(2.5));

                cell.getChildren().add(hole);

                final int column = col;

                
                cell.setOnMouseEntered(e -> highlightColumn(column, true));
                cell.setOnMouseExited(e -> highlightColumn(column, false));
                cell.setOnMouseClicked(e -> newMove(column));
                

                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }
    }

    private void highlightColumn(int col, boolean highlight) {
        for (int row = 0; row < ROWS; row++) {
            if (highlight) {
                cells[row][col].getStyleClass().add("cell-hover");
            } else {
                cells[row][col].getStyleClass().remove("cell-hover");
            }
        }
    }

    private void setQueue(Queue sendQueue) {
    	this.sendQueue = sendQueue;
    }
    
    private void newMove(int column) {
    	
    	if(gameFinished) {
    		return;
    	}
    	
    	//Controlla se è il tuo turno
    	if(!myTurn) {
    		handleNotYourTurn();
    		return;
    	}
    	
    	//Controllo se la mossa non è valida
    	if(!isColumnFree(column)) {
    		handleInvalidMove();
    		return;
    	}
    	
    	
		
    	
    	//Invio la risposta al NetworkService 
    	sendQueue.insert(new Move(column));
    	//Cambio il turno
    	myTurn = false;
    	updateGameStatus(false);
    	
    	lastMove = column;
    }
    
    public void handleInvalidMove() {
		//Bisogna mostrare a schermo che la mossa non è valida per un 2-3 secondi
    	Notification.showPopUp(MainApplication.getStage().getScene(), "Mossa non valida inserirne un altra");
		myTurn = true;
		updateGameStatus(true);
	}
    
    public void handleNotYourTurn() {
    	
    	Notification.showPopUp(MainApplication.getStage().getScene(), "Non è il tuo turno aspetta!");
    }

	private boolean isColumnFree(int column) {
		Circle hole = (Circle) cells[0][column].getChildren().get(0);
		
		if(hole.getStyleClass().contains("hole")) {
			return true;
		}
		
		return false;
	}

	public void placeYourMove() {
		dropDisc(lastMove,myColor);
	}
	
	public void placeEnemyMove(int column) {
		dropDisc(column,otherColor);
		myTurn = true;
		updateGameStatus(true);
	}
	
	//Mostra a schermo la mossa
	private void dropDisc(int col, Color color) {

        for (int row = ROWS - 1; row >= 0; row--) {

            Circle hole = (Circle) cells[row][col].getChildren().get(0);

            if (hole.getStyleClass().contains("hole")) {

                hole.getStyleClass().remove("hole");

                if (color == Color.red) {
                    hole.getStyleClass().add("player1");
                } else {
                    hole.getStyleClass().add("player2");
                }
                
                return;
            }
        }
    }

	public void setMyUsername(String myUsername) {
		this.myUsername = myUsername;
		
	}

	public void setEnemyUsername(String enemyUsername) {
		this.enemyUsername = enemyUsername;
	}
	
	public String getMyUsername() {
		return this.myUsername;
	}

	public void setSendQueue(Queue sendQueue) {
		this.sendQueue = sendQueue;
		
	}
	
	public void setYourMove(Boolean move) {
		this.myTurn = move;
		updateGameStatus(move);
		
		myColor = move == true? Color.red:Color.yellow;
		otherColor = myColor == Color.red?Color.yellow:Color.red;
	}

	public void showGameEnded() {
		gameFinished = true;
		turnStatusLabel.setText("GAME ENDED");
        turnStatusLabel.setStyle("-fx-text-fill: #ffff00;");
	}
	
	
	
}

