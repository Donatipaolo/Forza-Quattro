package application.Controllers;

import application.MainApplication;
import enums.GameEndInfo;
import enums.GameEndResult;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class GameEndController {

    @FXML private Text resultTitle;
    @FXML private Text additionalInfo;

    /**
     * Metodo per configurare la schermata dinamicamente
     * @param result "WIN", "LOSS", "DRAW"
     * @param message Informazioni extra (es. "Enemy disconnected")
     */
    public void setResults(GameEndResult result, GameEndInfo extraInfo) {
        // Pulisci classi precedenti per sicurezza
        resultTitle.getStyleClass().removeAll("title-win", "title-loss", "title-draw");
        additionalInfo.getStyleClass().add("info-text");

        // Imposta il messaggio extra
        

        switch (result) {
            case won:
                resultTitle.setText("VICTORY");
                resultTitle.getStyleClass().add("title-win");
                
                if(extraInfo == GameEndInfo.game_ended) {
                	additionalInfo.setText("Congratulations!");;
                }
                else if(extraInfo == GameEndInfo.enemy_disconnected) {
                	additionalInfo.setText("Enemy disconnected");
                }
                
                
                break;
            case defeat:
                resultTitle.setText("DEFEAT");
                resultTitle.getStyleClass().add("title-loss");
                additionalInfo.setText("Try again!");
                break;
            case tie:
                resultTitle.setText("DRAW");
                resultTitle.getStyleClass().add("title-draw");
                additionalInfo.setText("Try again!");
                break;
        }
    }

    @FXML
    private void handleBackToLobby() {
        // Qui chiameresti il metodo nel Main per cambiare scena
        MainApplication.showLobby();
    }
}