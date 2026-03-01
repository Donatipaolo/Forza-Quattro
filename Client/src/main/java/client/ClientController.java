package client;

import protocol.*;
import enums.ChallengeResultStatus;
import enums.ChangeUsernameResult;
import enums.MessageType;
import enums.MoveResultStatus;
import enums.Status;
import javafx.application.Platform;
import application.CurrentController;
import application.MainApplication;
import application.Controllers.GameController;
import application.Controllers.LobbyController;
import data.Queue;

// ClientController coordina stato del client e comunicazione con NetworkService
public class ClientController extends Thread implements Runnable{

    private NetworkService networkService;						// Gestisce la comunicazione di rete
    private Queue sendQueue;									// Coda messaggi da inviare
    private Queue readQueue;									// Coda messaggi ricevuti

    private boolean connected;
    //TODO da capire l' utilità di questa variabile
    //Non si è ancora deciso che cosa fare in caso di disconnessione del server queste sono le possibilità:
    // - Chiudere direttamente il gioco
    // - Provare a riconnettersi all' infinito o fino ad un intervallo di tempo prestabilito 
    //   (più bellino ma inutilmente complesso visto il nostro obbiettivo)
    // - Mostrare a schermo che il server si è disconnesso e aspettare che il client prema qualcosa per chiudere il gioco
    //   (Secondo me è la meglio ma sentiamo gli altri)
    
    private Status status;


    public ClientController(){
    	super("Client Controller");
    	
    	//Il service network viene creato nel main insieme a tutti gli altri thread quindi non è corretto crearlo qui
    	
        sendQueue = networkService.getSendQueue();
        readQueue = networkService.getReadQueue();

        connected = true;
        status = Status.free;
    }

    @Override
    public void run(){
    	
        while(connected){
            Message message = readQueue.remove();			// Estrae messaggio ricevuto
            
             if(message==null){
                //TODO Controllare se questo caso è effettivamente possibile
            	 continue;
             }
             
             //Controllo il tipo
             if(status == Status.free)
            	  handleMessageLobby(message);
             else 
            	 handleMessageGame(message);
        }
    }

    private void handleMessageLobby(Message message){

        MessageType type = message.getType();
        switch(type) {
        
            case change_username_response:
                handleChangeUsernameResponse(message);
            break;

            case play_list_response:
            	handlePlayListResponse(message);
            break;
            
            case challenge_request:
                handleChallengeRequest(message);
            break;

            case challenge_result:
                handleChallengeResult(message);
            break;

            default:
                System.out.println("Messaggio non gestito: " + type);
            break;
        }
    }
    
   

	private void handleMessageGame(Message message) {
    	MessageType type = message.getType();
    	
    	switch(type){

    	case move:
    		handleMove(message);
    	break;
    	
        case move_response:
        	handleMoveResult(message);
        break;

        case game_end:
            handleGameEnd(message);
        break;

        default:
            System.out.println("Messaggio non gestito: " + type);
        break;
    }
    }
	
	 private void handleChallengeResult(Message message) {
	    	ChallengeResult challengeRequest = (ChallengeResult) message;
			
	    	if(challengeRequest.getStatus() == ChallengeResultStatus.ok) {
	    		this.status = Status.in_game;
	    		
	    		//TODO Modificare la funzione per stampare il nome dell'avversario
	    		MainApplication.showGame();
	    		
	    		//rifiuto tutte le altre richieste
	    		//TODO rifiutare tutte le richieste in arrivo ed eliminare quella che è stata accettata
	    	}
	    	
	    	Platform.runLater(() -> {
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).removeIncomingRequest(challengeRequest.getUsername());
			    }
			});
	    	
	    	if(challengeRequest.getStatus() == ChallengeResultStatus.refused) {
	    		Platform.runLater(() -> {
	    		    if (CurrentController.controller != null) {
	    		    	((LobbyController) CurrentController.controller).handleResultDeclined(challengeRequest.getUsername());
	    		    }
	    		});
	    		return;
	    	}
	    	
	    	if(challengeRequest.getStatus() == ChallengeResultStatus.client_not_found) {
	    		Platform.runLater(() -> {
	    		    if (CurrentController.controller != null) {
	    		    	((LobbyController) CurrentController.controller).handleClientAcceptedNotFound(challengeRequest.getUsername());
	    		    }
	    		});
	    		return;
	    	}
		}

		private void handleChallengeRequest(Message message) {
			ChallengeRequest challengeRequest = (ChallengeRequest) message;
			
			Platform.runLater(() -> {
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).addIncomingRequest(challengeRequest.getUsername());
			    }
			});

			//Li aggiungo alla lista delle richieste in attesa
			//TODO Creare la lista delle richieste in attesa
		}

		private void handlePlayListResponse(Message message) {
			PlayListResponse playListResponse = (PlayListResponse) message;
			
	    	
	    	Platform.runLater(() -> {
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).addPlayers(playListResponse.getListOfPlayer());
			    }
			});
			
		}

		private void handleChangeUsernameResponse(Message message) {
			ChangeUsernameResponse response = (ChangeUsernameResponse) message;
			
			if(response.getStatus() == ChangeUsernameResult.ok) {
				Platform.runLater(() -> {
				    if (CurrentController.controller != null) {
				    	((LobbyController) CurrentController.controller).updateUsername();
				    }
				});
				
				return;
			}
			
			Platform.runLater(() -> {
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).handleUsernameTaken();
			    }
			});
			
			
		}
    
    private void handleGameEnd(Message message) {
    	GameEnd gameEnd = (GameEnd) message;
    	
    	//Imposto lo status del client a free
    	this.status = Status.free;
    	
    	//Mostro la scena della fine del gioco
    	MainApplication.showGameEnd(gameEnd.getResult(),gameEnd.getInfo());
	}

	private void handleMove(Message message) {
    	Move move = (Move) message;
    	
    	Platform.runLater(() -> {
		    if (CurrentController.controller != null) {
		    	((GameController) CurrentController.controller).placeEnemyMove(move.getColumn());
		    }
		});
		
	}

	private void handleMoveResult(Message message) {
		MoveResult moveResult = (MoveResult) message;
		
		
		//Se la mossa è valida
		if(moveResult.getStatus() == MoveResultStatus.ok ) {
			
			Platform.runLater(() -> {
			    if (CurrentController.controller != null) {
			    	((GameController) CurrentController.controller).placeYourMove();
			    }
			});
			
			return;
		}
		
		//Mossa non valida
		if(moveResult.getStatus() == MoveResultStatus.invalid_move ) {
			
			Platform.runLater(() -> {
			    if (CurrentController.controller != null) {
			    	((GameController) CurrentController.controller).handleInvalidMove();
			    }
			});
			
			return;
		}
		
		//Non è il tuo turno
		if(moveResult.getStatus() == MoveResultStatus.not_your_turn ) {
			
			Platform.runLater(() -> {
			    if (CurrentController.controller != null) {
			    	((GameController) CurrentController.controller).handleNotYourTurn();
			    }
			});
			
			return;
		}		
		
		
	}

	

    public boolean isConnected(){								// Permette controllo stato connessione
        return connected;
    }

    public boolean isInGame(){									// Permette controllo stato partita
        return status == Status.in_game;
    }

	public void setSendQueue(Queue sendQueue) {
		this.sendQueue = sendQueue;
		
	}

	public void setReadQueue(Queue readQueue) {
		this.readQueue = readQueue;
		
	}
}