package client;

import protocol.*;
import enums.MessageType;
import data.Queue;

// ClientController coordina stato del client e comunicazione con NetworkService
public class ClientController {

    private NetworkService networkService;						// Gestisce la comunicazione di rete
    private Queue sendQueue;									// Coda messaggi da inviare
    private Queue readQueue;									// Coda messaggi ricevuti

    private boolean connected;									// Indica se il client è connesso
    private boolean inGame;										// Indica se il client è in partita
    private boolean gameOver;									// Indica se la partita è terminata

    private Thread messageHandlerThread;						// Thread che processa i messaggi ricevuti

    public ClientController(){

        networkService = new NetworkService();					// Inizializza il servizio di rete
        sendQueue = networkService.getSendQueue();
        readQueue = networkService.getReadQueue();

        connected = true;

        startMessageHandler();									// Avvia gestione messaggi
    }

    private void startMessageHandler(){

        messageHandlerThread = new Thread(() -> {				// Thread che interpreta i messaggi

            while(connected){
                Message message = readQueue.remove();			// Estrae messaggio ricevuto

                if(message!=null){
                    handleMessage(message);						// Logica decisionale
                }
            }

        });

        messageHandlerThread.start();
    }

    private void handleMessage(Message message){

        MessageType type = message.getType();					// Determina tipo evento

        switch(type){

            case move_response:
                System.out.println("Mossa ricevuta dal server.");
                inGame = true;
            break;

            case game_end:
                System.out.println("Partita terminata.");
                inGame = false;
                gameOver = true;
            break;

            case disconnect:
                System.out.println("Server disconnesso.");
                connected = false;
            break;

            default:
                System.out.println("Messaggio non gestito: " + type);
            break;
        }
    }

    public void sendMove(Move move){							// Invia una mossa richiesta dall'utente
        sendQueue.insert(move);
    }

    public void sendChallenge(ChallengeRequest request){		// Invia richiesta di sfida
        sendQueue.insert(request);
    }

    public boolean isConnected(){								// Permette controllo stato connessione
        return connected;
    }

    public boolean isInGame(){									// Permette controllo stato partita
        return inGame;
    }

    public boolean isGameOver(){								// Permette controllo fine partita
        return gameOver;
    }
}