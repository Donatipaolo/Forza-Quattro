package client;

import protocol.*;
import enums.ChallengeResultStatus;
import enums.ChangeUsernameResult;
import enums.MessageType;
import enums.MoveResultStatus;
import enums.MoveValue;
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

    private Status status;
    private boolean exit = false;

    public ClientController(Queue sendQueue, Queue readQueue){
    	super("Client Controller");
    
        this.sendQueue = sendQueue;
        this.readQueue = readQueue;
    
        status = Status.not_connected;
    }

    @Override
    public void run(){
    	
        while(!exit){
            Message message = readQueue.remove();			// Estrae messaggio ricevuto
            
             if(message==null){
            	 continue;
             }
             
             //Controllo il tipo
             if(status == Status.free)
            	  handleMessageLobby(message);
             else if(status == Status.in_game)
            	 handleMessageGame(message);
             else
            	 handleMessageNotConnected(message);
        }
    }

    private void handleMessageNotConnected(Message message) {
    	MessageType type = message.getType();
    	
        switch(type) {
            case server_connection_result:
                handleServerConnectionResult(message);
                break;
            default:
			System.out.println("Messaggio non gestito " + type);
			break;
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
	
	
	
	private void handleServerConnectionResult(Message message) {
		ServerConnectionResult connectionResult = (ServerConnectionResult) message;
		Platform.runLater(()->{
			
			if(!(CurrentController.controller instanceof LobbyController)) {
				System.out.println("Errore instanza del controller sbagliata");
				return;
			}
				
			
			((LobbyController)CurrentController.controller).setUsername(connectionResult.getUsername());
			status = Status.free;
		});

	}
	
	 private void handleChallengeResult(Message message) {
	    	ChallengeResult challengeRequest = (ChallengeResult) message; 
			
	    	Boolean turn = challengeRequest.getFirstMove() == MoveValue.you? true: false;
	    	
	    	if(challengeRequest.getStatus() == ChallengeResultStatus.ok) {
	    		this.status = Status.in_game;
	    		
	    		Platform.runLater(()->{
	    			
	    			if(!(CurrentController.controller instanceof LobbyController)) {
	    				System.out.println("Errore instanza del controller sbagliata");
	    				return;
	    			}
	    			
	    			//TODO Modificare la scena per stampare il nome dell'avversario e il proprio
		    		MainApplication.showGame(((LobbyController)CurrentController.controller).getUsername(),challengeRequest.getUsername(),sendQueue);
		    		
		    		if(!(CurrentController.controller instanceof GameController)) {
	    				System.out.println("Errore instanza del controller sbagliata");
	    				return;
	    			}
		    		
		    		((GameController)CurrentController.controller).setYourMove(turn);
		    		
	    		});
	    		
	    		return;
	    		//Il server si occupa di rifiutare tutte le altre richieste
	    	}
	    	
	    	//se viene rifiutata prima elimino la richiesta dalla lista di richieste
	    	Platform.runLater(() -> {
	    		
	    		if(!(CurrentController.controller instanceof LobbyController)) {
    				System.out.println("Errore instanza del controller sbagliata");
    				return;
    			}
	    		
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).removeIncomingRequest(challengeRequest.getUsername());
			    }
			});
	    	
	    	if(challengeRequest.getStatus() == ChallengeResultStatus.refused) {
	    		Platform.runLater(() -> {
	    			
	    			if(!(CurrentController.controller instanceof LobbyController)) {
	    				System.out.println("Errore instanza del controller sbagliata");
	    				return;
	    			}
	    			
	    		    if (CurrentController.controller != null) {
	    		    	((LobbyController) CurrentController.controller).handleResultDeclined(challengeRequest.getUsername());
	    		    }
	    		});
	    		return;
	    	}
	    	
	    	if(challengeRequest.getStatus() == ChallengeResultStatus.client_not_found) {
	    		Platform.runLater(() -> {
	    			
	    			if(!(CurrentController.controller instanceof LobbyController)) {
	    				System.out.println("Errore instanza del controller sbagliata");
	    				return;
	    			}
	    			
	    		    if (CurrentController.controller != null) {
	    		    	((LobbyController) CurrentController.controller).handleClientRequestedNotFound(challengeRequest.getUsername());
	    		    }
	    		});
	    		return;
	    	}
		}

		private void handleChallengeRequest(Message message) {
			ChallengeRequest challengeRequest = (ChallengeRequest) message;
			
			Platform.runLater(() -> {
				
				if(!(CurrentController.controller instanceof LobbyController)) {
					System.out.println("Errore instanza del controller sbagliata");
					return;
				}
				
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).addIncomingRequest(challengeRequest.getUsername());
			    }
			});
		}

		private void handlePlayListResponse(Message message) {
			PlayListResponse playListResponse = (PlayListResponse) message;
			
	    	
	    	Platform.runLater(() -> {
	    		
	    		if(!(CurrentController.controller instanceof LobbyController)) {
					System.out.println("Errore instanza del controller sbagliata");
					return;
				}
	    		
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).addPlayers(playListResponse.getListOfPlayer());
			    }
			});
			
		}

		private void handleChangeUsernameResponse(Message message) {
			ChangeUsernameResponse response = (ChangeUsernameResponse) message;
			
			if(response.getStatus() == ChangeUsernameResult.ok) {
				Platform.runLater(() -> {
					
					if(!(CurrentController.controller instanceof LobbyController)) {
						System.out.println("Errore instanza del controller sbagliata");
						return;
					}
					
				    if (CurrentController.controller != null) {
				    	((LobbyController) CurrentController.controller).updateUsername();
				    }
				});
				
				return;
			}
			
			Platform.runLater(() -> {
				
				if(!(CurrentController.controller instanceof LobbyController)) {
					System.out.println("Errore instanza del controller sbagliata");
					return;
				}
				
			    if (CurrentController.controller != null) {
			    	((LobbyController) CurrentController.controller).handleUsernameTaken();
			    }
			});
			
			
		}
    
    private void handleGameEnd(Message message) {
    	GameEnd gameEnd = (GameEnd) message;
    	
    	//Imposto lo status del client a free
    	this.status = Status.free;
    	
    	Platform.runLater(() -> {
    		
    		if(!(CurrentController.controller instanceof GameController)) {
				System.out.println("Errore instanza del controller sbagliata");
				return;
			}
    		
    		//Mostro la scena della fine del gioco
        	MainApplication.showGameEnd(gameEnd.getResult(),gameEnd.getInfo(),
        			((GameController)CurrentController.controller).getMyUsername(),sendQueue);
    	});
    	
	}

	private void handleMove(Message message) {
    	Move move = (Move) message;
    	
    	Platform.runLater(() -> {
    		
    		if(!(CurrentController.controller instanceof GameController)) {
				System.out.println("Errore instanza del controller sbagliata");
				return;
			}
    		
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
				
				if(!(CurrentController.controller instanceof GameController)) {
					System.out.println("Errore instanza del controller sbagliata");
					return;
				}
				
			    if (CurrentController.controller != null) {
			    	((GameController) CurrentController.controller).placeYourMove();
			    }
			});
			
			return;
		}
		
		//Mossa non valida
		if(moveResult.getStatus() == MoveResultStatus.invalid_move ) {
			
			Platform.runLater(() -> {
				
				if(!(CurrentController.controller instanceof GameController)) {
					System.out.println("Errore instanza del controller sbagliata");
					return;
				}
				
			    if (CurrentController.controller != null) {
			    	((GameController) CurrentController.controller).handleInvalidMove();
			    }
			});
			
			return;
		}
		
		//Non è il tuo turno
		if(moveResult.getStatus() == MoveResultStatus.not_your_turn ) {
			
			Platform.runLater(() -> {
				
				if(!(CurrentController.controller instanceof GameController)) {
					System.out.println("Errore instanza del controller sbagliata");
					return;
				}
				
			    if (CurrentController.controller != null) {
			    	((GameController) CurrentController.controller).handleNotYourTurn();
			    }
			});
			
			return;
		}		
		
		
	}

	

    public void exit() {
    	exit = true;

    	readQueue.insert(null);
    	try {
			this.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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