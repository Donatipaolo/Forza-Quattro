package application.Controllers;

import java.util.ArrayList;

import application.MainApplication;
import application.Notification;
import data.Player;
import data.Queue;
import enums.ChallengeResponseStatus;
import enums.Status;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import protocol.ChallengeRequest;
import protocol.ChallengeResponse;
import protocol.ChangeUsernameRequest;
import protocol.PlayListRequest;

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

    public void setUsername(String username) {
    	this.username = username;
    	currentNameLabel.setText(username);
    }
    
    public void setSendQueue(Queue sendQueue) {
    	this.sendQueue = sendQueue;
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    /// GESTIONE DELLA LISTA DEI CLIENT CONNESSI
    
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

    @FXML
    public void handleRefreshPlayers() {
    	sendQueue.insert(new PlayListRequest());
    }
    
    public void addPlayers(ArrayList<Player> players) {
    	
    	clearPlayersUI();
    	
    	for(Player player : players) {
    		
    		if(player.getUsername().equals(username))
    			continue;
    		
    		String status = player.getStatus() == Status.free? "FREE" : "IN GAME";
    		addPlayerToUI(player.getUsername(),status);
    	}
    }
    
    private void clearPlayersUI() {
        playersList.getChildren().clear();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    /// GESTIONE DELLA LISTA DELLE RICHIESTE INVIATE
    
	private void sendRequest(String playerName) {
		
		boolean alreadySent = sentRequests.getChildren().stream()
                .anyMatch(node -> playerName.equals(node.getUserData()));

        if (alreadySent) {
        	Notification.showPopUp(MainApplication.getStage().getScene(), "Hai già inviato una richiesta a questo giocatore!");
            return;
        }

        // Controllo di sicurezza: non posso sfidare me stesso
        if (playerName.equals(this.username)) {
        	Notification.showPopUp(MainApplication.getStage().getScene(), "Non puoi sfidare te stesso!");
            return;
        }
		
		
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
        
        requestCard.setUserData(playerName);
        
        sentRequests.getChildren().add(requestCard);
        
        //Invio la richiesta
        sendQueue.insert(new ChallengeRequest(playerName));
    }
	
	//Funzione che viene eseguita quando il client a cui abbiamo inviato una richiesta non viene trovato
	public void handleClientRequestedNotFound(String username) {
		//Rimuove solo la richiesta inviata con quell'username
		sentRequests.getChildren().removeIf(node -> username.equals(node.getUserData()));
		Notification.showPopUp(MainApplication.getStage().getScene(), "Il client non è stato trovato prova a riaggiornare la lista dei player connessi");
	}
	
	public void removeAllSentRequest() {
		sentRequests.getChildren().clear();
	}
	
	//Funzione che viene eseguita quando il server invia una risposta di declino ad una nostra richiesta precedentemente fatta
	public void handleResultDeclined(String opponentName) {
		sentRequests.getChildren().removeIf(node -> opponentName.equals(node.getUserData()));
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////////////
	/// GESTIONE DELLA LISTA DELLE RICHIESTE RICEVUTE
	///
	public void addIncomingRequest(String opponentName) { // Cambiato nome per chiarezza
	    HBox card = new HBox(15);
	    card.getStyleClass().add("request-card");
	    card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

	    Text msg = new Text(opponentName + " wants to play!");
	    msg.getStyleClass().add("text-player-name");

	    Region spacer = new Region();
	    HBox.setHgrow(spacer, Priority.ALWAYS);

	    Button acceptBtn = new Button("Accept");
	    acceptBtn.getStyleClass().add("button-accept");
	    
	    // Forza la lambda a usare opponentName specifico di questa card
	    acceptBtn.setOnAction(e -> handleAcceptRequest(opponentName)); 
	    
	    Button declineBtn = new Button("Decline");
	    declineBtn.getStyleClass().add("button-reject");
	    declineBtn.setOnAction(e -> handleDeclineRequest(opponentName));

	    card.getChildren().addAll(msg, spacer, acceptBtn, declineBtn);
	    
	    // Salviamo l'opponentName anche nei UserData come backup
	    card.setUserData(opponentName);
	    
	    incomingRequests.getChildren().add(card);
	}
	
	public void removeIncomingRequest(String opponentName) {
		//Rimuove solo la richiesta inviata con quell'username
		incomingRequests.getChildren().removeIf(node -> opponentName.equals(node.getUserData()));
	}
	
	public void removeAllIncomingRequest() {
		incomingRequests.getChildren().clear();
	}
	

	//Declino di una richiesta
	private void handleDeclineRequest(String opponentName) {
		sendQueue.insert(new ChallengeResponse(ChallengeResponseStatus.refused, opponentName));
		incomingRequests.getChildren().removeIf(node -> opponentName.equals(node.getUserData()));
	}
	
	//Accettamento di una richiesta
	private void handleAcceptRequest(String opponentName) {
		if(gameAccepted) {
			handleGameJustAccepted();
			return;
		}
		
		
		sendQueue.insert(new ChallengeResponse(ChallengeResponseStatus.ok, opponentName));
		gameAccepted = true;
	}
    
	
	private void handleGameJustAccepted() {
		Notification.showPopUp(MainApplication.getStage().getScene(), "Gioco appena accettato non eseguire questa operazione");
		
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	/// GESTIONE DEL CAMBIO NOME
    
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
    	//Pulisco qualsiasi richiesta mostrata a schermo
    	removeAllSentRequest();
    	removeAllIncomingRequest();
    	currentNameLabel.setText(newUsername);
    	username = newUsername;
    	nameSended = false;
    	
    }
    
    public void handleUsernameTaken() {
    	Notification.showPopUp(MainApplication.getStage().getScene(), "L'username inserito è già stato preso");
    	nameSended = false;
    }

	private void handleNameSended() {
		Notification.showPopUp(MainApplication.getStage().getScene(), "Il server sta ancora elaborando la precedente risposta aspetta un attimo");
		
	}
	
	public String getUsername() {
		return this.username;
	}
	
}