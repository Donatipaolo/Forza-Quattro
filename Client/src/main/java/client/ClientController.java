package client;

import protocol.*;
import enums.MessageType;
import enums.Status;
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

    private void handleMessageGame(Message message) {
    	MessageType type = message.getType();
    	
    	switch(type){

    	case move:
    		//In questo caso dobbiamo mostrare a schermo la mossa dell' avversario e aggiornare la nostra griglia
    	break;
    	
        case move_response:
            /*
             * Nel caso che la risposta sia positiva allora dobbiamo solo aspettare la risposta dell' altro client
             * Nel caso che sia negativa (move not valid oppure not your turn) Dobbiamo rileggere la mossa*/
        break;

        case game_end:
            //In questo caso dobbiamo interrompere la partita e stampare a schermo il risultato 
        	//Modificando lo stato in free
        break;

        default:
            System.out.println("Messaggio non gestito: " + type);
        break;
    }
    }
    
    private void handleMessageLobby(Message message){

        MessageType type = message.getType();
        switch(type) {
        
            case change_username_response:
                //Controllare il risultato della risposta e cambiare il nome di conseguenza
            break;

            case play_list_response:
            	//Aggiornare la propria struttura dati che contiene i giocatori e ristamparli a schermo
            break;
            
            case challenge_request:
                //Aggiungere la richiesta ad una lista di richieste e mostrarla a schermo in una parte dedicata
            	//TODO Una volta che una richiesta viene accettata bisogna rifiutare tutte le altre presenti
            	//in modo da evitare delle partite indesiderate
            break;

            case challenge_result:
                //In caso affermativo allora è necessario che venga avviata la partita:
            	//Mostrare a schermo la griglia vuota
            	//Capire se siamo noi ad eseguire la prima mossa
            	//Modificare lo stato del client in : in_game
            break;

            default:
                System.out.println("Messaggio non gestito: " + type);
            break;
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