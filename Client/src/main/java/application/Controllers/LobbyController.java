package application.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class LobbyController {

	@FXML private Label currentNameLabel; // Nuova label per il nome corrente
    @FXML private TextField nameField;
    @FXML private VBox playersList;
    @FXML private VBox incomingRequests;
    @FXML private VBox sentRequests;

    @FXML
    public void initialize() {
        // Qui caricheresti i dati reali. Esempio di placeholder:
        addPlayerToUI("Mario", "FREE");
        addPlayerToUI("Luigi", "IN GAME");
    }

    private void addPlayerToUI(String name, String status) {
        HBox row = new HBox(15);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 10; -fx-background-color: rgba(255,255,255,0.05); -fx-background-radius: 10;");

        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80;");

        Label statusLbl = new Label(status);
        statusLbl.getStyleClass().add(status.equals("FREE") ? "label-status-free" : "label-status-ingame");
        statusLbl.setStyle("-fx-min-width: 80;");

        // Questo componente occupa tutto lo spazio possibile, spingendo il bottone a destra
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button inviteBtn = new Button("Invite");
        inviteBtn.getStyleClass().add("button-primary");
        inviteBtn.setMinWidth(80); // Larghezza fissa per uniformità
        inviteBtn.setDisable(!status.equals("FREE"));
        inviteBtn.setOnAction(e -> sendRequest(name));

        row.getChildren().addAll(nameLbl, statusLbl, spacer, inviteBtn);
        playersList.getChildren().add(row);
    }

    private void sendRequest(String playerName) {
        // Creiamo una card per la richiesta inviata
        HBox requestCard = new HBox(15);
        requestCard.getStyleClass().add("request-card");
        requestCard.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Testo informativo
        Text prefix = new Text("Sent to: ");
        prefix.setStyle("-fx-fill: #888888;");
        
        Text name = new Text(playerName);
        name.getStyleClass().add("text-player-name");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Text status = new Text("Waiting...");
        status.getStyleClass().add("text-waiting");

        // Pulsante per annullare la richiesta (opzionale ma consigliato per la UX)
        Button cancelBtn = new Button("✕");
        cancelBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ff5252; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> sentRequests.getChildren().remove(requestCard));

        requestCard.getChildren().addAll(prefix, name, spacer, status, cancelBtn);
        
        // Aggiungiamo la card al contenitore FXML
        sentRequests.getChildren().add(requestCard);
    }

    private void addIncomingRequest(String fromPlayer) {
        HBox card = new HBox(15);
        card.getStyleClass().add("request-card");
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text msg = new Text(fromPlayer + " wants to play!");
        msg.getStyleClass().add("text-player-name");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button acceptBtn = new Button("Accept");
        acceptBtn.getStyleClass().add("button-accept");
        
        Button declineBtn = new Button("Decline");
        declineBtn.getStyleClass().add("button-reject");

        card.getChildren().addAll(msg, spacer, acceptBtn, declineBtn);
        incomingRequests.getChildren().add(card);
    }
    
    @FXML
    private void handleUpdateName() {
        String newName = nameField.getText().trim();
        
        if (!newName.isEmpty()) {
            // Aggiorna la label del profilo
            currentNameLabel.setText(newName);
            
            // Pulizia campo input
            nameField.clear();
            
            // Logica opzionale: feedback visivo o invio al server
            System.out.println("Username changed to: " + newName);
        } else {
            // Alert o stile rosso se il campo è vuoto
            nameField.setStyle("-fx-border-color: #ff5252;");
        }
    }
}