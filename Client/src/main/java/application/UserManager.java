package application;

import client.ClientController;
import protocol.*;

//TODO NON PENSO CHE QUESTO SIA IL SUO VERO SCOPO DOBBIAMO RIPARLARNE
//Da quanto avevamo discusso in classe l' ultima volta questa componente doveva occuparsi di gestire l' interfaccia graficaù
//La connessione del client controller e del service network non è gestita da questo thread quindi li ho tolti
//Per il resto riparliamone che non mi torna

// UserManager gestisce l'interazione utente e aggiorna l'interfaccia senza occuparsi della rete
public class UserManager {

   

    public UserManager(){
   				
    }

    // ===== INPUT UTENTE =====

    public void onConnect(String host,int port){				// Chiamato quando l'utente preme "Connetti"
        clientController.connect(host,port);
    }

    public void onDisconnect(){									// Chiamato quando l'utente preme "Disconnetti"
        clientController.disconnect();
    }

    public void onMoveSelected(int column){						// Chiamato quando l'utente seleziona una colonna
        Move move = new Move(column);
        clientController.sendMove(move);						// Notifica il controller
    }

    public void onChallengeRequest(String opponent){			// Chiamato quando l'utente sfida qualcuno
        ChallengeRequest request = new ChallengeRequest(opponent);
        clientController.sendChallenge(request);
    }

    // ===== AGGIORNAMENTO INTERFACCIA =====

    public void updateBoard(Message message){					// Aggiorna la grafica della partita
        System.out.println("Aggiornamento grafico della board.");
        // Qui andrà la logica GUI (es. ridisegnare la board)
    }

    public void showGameEnd(Message message){					// Mostra il risultato finale
        System.out.println("Mostrare schermata di fine partita.");
    }

    public void showDisconnect(){								// Mostra perdita connessione
        System.out.println("Mostrare messaggio di disconnessione.");
    }
}