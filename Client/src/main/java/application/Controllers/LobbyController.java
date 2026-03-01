package application.Controllers;

import java.util.ArrayList;

import data.Player;
import data.Queue;
import enums.ChallengeResponseStatus;
import enums.Status;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import protocol.ChallengeResponse;
import protocol.ChangeUsernameRequest;

public class LobbyController {

	@FXML private Label currentNameLabel; // Nuova label per il nome corrente
    @FXML private TextField nameField;
    @FXML private VBox playersList;
    @FXML private VBox incomingRequests;
    @FXML private VBox sentRequests;

    private Queue sendQueue;
    private boolean nameSended = false;
    private boolean gameAccepted = false;
    private String username;
    private String newUsername;
    
    @FXML
    public void initialize() {
    	
    }

    private void setUsername(String username) {
    	this.username = username;
    }
    
    private void setSendQueue(Queue sendQueue) {
    	this.sendQueue = sendQueue;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    
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

    //TODO Aggiungere il bottone di aggiornamento della lista dei player
    
    public void addPlayers(ArrayList<Player> players) {
    	
    	clearPlayersUI();
    	
    	for(Player player : players) {
    		String status = player.getStatus() == Status.free? "FREE" : "IN GAME";
    		addPlayerToUI(player.getUsername(),status);
    	}
    }
    
    private void clearPlayersUI() {
        playersList.getChildren().clear();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    
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

        requestCard.getChildren().addAll(prefix, name, spacer, status);
        
        requestCard.setUserData(name);
        
        sentRequests.getChildren().add(requestCard);
    }
	
	public void handleClientRequestedNotFound(String username) {
		//Rimuove solo la richiesta inviata con quell'username
		sentRequests.getChildren().removeIf(node -> username.equals(node.getUserData()));
		System.out.println("Il client non è stato trovato prova a riaggiornare la lista dei player connessi");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	//Funzione che viene eseguita quando il server invia una risposta di declino ad una nostra richiesta precedentemente fatta
	public void handleResultDeclined(String username) {
		System.out.println("richiesta declinata");
		incomingRequests.getChildren().removeIf(node -> username.equals(node.getUserData()));
	}
	
	public void handleClientAcceptedNotFound(String username) {
		System.out.println("La partita accettata è stata rifiutata, ricarica la lista dei client");
		incomingRequests.getChildren().removeIf(node -> username.equals(node.getUserData()));
		gameAccepted = false;
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////////////
	/// 
	public void addIncomingRequest(String username) {
        HBox card = new HBox(15);
        card.getStyleClass().add("request-card");
        card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Text msg = new Text(username + " wants to play!");
        msg.getStyleClass().add("text-player-name");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button acceptBtn = new Button("Accept");
        acceptBtn.getStyleClass().add("button-accept");
        acceptBtn.setOnAction(e -> handleAcceptRequest(username));
        
        Button declineBtn = new Button("Decline");
        declineBtn.getStyleClass().add("button-reject");
        declineBtn.setOnAction(e -> handleDeclineRequest(username));

        card.getChildren().addAll(msg, spacer, acceptBtn, declineBtn);
        
        card.setUserData(username);
        
        incomingRequests.getChildren().add(card);
    }
	
	public void removeIncomingRequest(String username) {
		//Rimuove solo la richiesta inviata con quell'username
		incomingRequests.getChildren().removeIf(node -> username.equals(node.getUserData()));
	}
	
	private void handleDeclineRequest(String username) {
		sendQueue.insert(new ChallengeResponse(ChallengeResponseStatus.refused, username));
		incomingRequests.getChildren().removeIf(node -> username.equals(node.getUserData()));
	}
	
	private void handleAcceptRequest(String username) {
		if(gameAccepted) {
			handleGameJustAccepted();
			return;
		}
		
		sendQueue.insert(new ChallengeResponse(ChallengeResponseStatus.ok, username));
		gameAccepted = true;
	}
    
	private void handleGameJustAccepted() {
		System.out.println("Gioco appena accettato non eseguire questa operazione");
		
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
    
	@FXML
    private void handleUpdateName() {
    	
    	String newName = nameField.getText().trim();
    	
    	if (newName.isEmpty()) {
    		// Alert o stile rosso se il campo è vuoto
            nameField.setStyle("-fx-border-color: #ff5252;");
    		return;
    	}
    	
    	// Pulizia campo input
        nameField.clear();
    	
    	if(gameAccepted) {
    		handleGameJustAccepted();
    	}
    	
		if(nameSended) {
			
    		handleNameSended();
    		return;
    	}
    	
		 nameSended = true;
        
       
        //Invio la richiesta
        sendQueue.insert(new ChangeUsernameRequest(newName));
        this.newUsername = newName;
    }
    
    public void updateUsername() {
    	refuseAllIncomingRequest();
    	currentNameLabel.setText(newUsername);
    	
    }
    
    public void handleUsernameTaken() {
    	System.out.println("L'username inserito è già stato preso");
    }

	private void handleNameSended() {
		System.out.println("Il server sta ancora elaborando la precedente risposta aspetta un attimo");
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////

	public void refuseAllIncomingRequest() {
		
	}
}