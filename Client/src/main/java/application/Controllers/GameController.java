package application.Controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import enums.GameEndResult;
import enums.GameEndInfo;

enum Color{
red, //Style giocatore 1
	yellow //Style giocatore 2
}

public class GameController {

    @FXML
    private GridPane grid;

    //La dimensione della griglia
    private static final int ROWS = 6;
    private static final int COLS = 7;

    //La griglia da stampare
    private StackPane[][] cells = new StackPane[ROWS][COLS];

    private boolean myTurn;
    private Color myColor;
    private Color otherColor;
    
    @FXML
    public void initialize() {
        createGrid();
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

    private void newMove(int column) {
    	//Controllo se è il tuo turno
    	if(!myTurn)
    		return;
    	
    	//Controllo se la mossa non è valida
    	if(!isColumnFree(column)) {
    		handleInvalidMove();
    	}
    	
    	//TODO Invio la richiesta al ClientController/NetworkService
    	//Aspetto la risposta
    	
    	//NEL CASO POSITIVO
    	dropDisc(column,myColor);
    	myTurn = !myTurn;
    	
    	//NEL CASO NEGATIVO
    	//SE È INVALID MOVE
    	handleInvalidMove();
    	
    	//SE È NOT YOUR TURN
    	//o inviamo una disconnect
    	//o semplicemente invertiamo il turno (sperando che non siano sfalsati i due client e server)
    }
    
    private void handleInvalidMove() {
		//Bisogna mostrare a schermo che la mossa non è valida per un 2-3 secondi
		
	}
    
    private void gameEnd(GameEndResult gameEndResult, GameEndInfo gameEndInfo) {
    	//cambiare la scena a quella di fine gioco e mostrare i diversi messaggi di info
    }

	private boolean isColumnFree(int column) {
		Circle hole = (Circle) cells[ROWS-1][column].getChildren().get(0);
		
		if(hole.getStyleClass().contains("hole")) {
			return false;
		}
		
		return true;
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
            }
        }
    }
}

