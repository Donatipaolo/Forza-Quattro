package application;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class GameController {

    @FXML
    private GridPane grid;

    private static final int ROWS = 6;
    private static final int COLS = 7;

    private StackPane[][] cells = new StackPane[ROWS][COLS];
    private int currentPlayer = 1;

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
                cell.setOnMouseClicked(e -> dropDisc(column));
                

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

    private void dropDisc(int col) {

        for (int row = ROWS - 1; row >= 0; row--) {

            Circle hole = (Circle) cells[row][col].getChildren().get(0);

            if (hole.getStyleClass().contains("hole")) {

                hole.getStyleClass().remove("hole");

                if (currentPlayer == 1) {
                    hole.getStyleClass().add("player1");
                } else {
                    hole.getStyleClass().add("player2");
                }

                currentPlayer = (currentPlayer == 1) ? 2 : 1;
                break;
            }
        }
    }
}

