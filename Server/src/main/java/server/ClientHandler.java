package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import data.*;
import enums.*;
import protocol.*;

public class ClientHandler extends Thread implements Runnable{

	//Static attribute
	public static ClientList clientList = null;
	public static PendingRequestList pendingRequestList = null;
	
	private Client client;
	private Status status = Status.free;
	
	public ClientHandler(Client client) {
		super("Client Handler");
		this.client = client;
		
	}
	
	@Override
	public void run() {
		try {
			clientHandler();
		} catch (IOException e) {
			 handleClientCrash();
		}
	}
	
	private void handleClientCrash() {
	
		//Elimino il client dalla lista dei client
		clientList.removeClient(client.getUsername());
	
		
		//Controllo le richieste pendenti riguardanti quel client e le elimino
		for(PendingRequest p : pendingRequestList.getPendingRequestlist(this.client)) {
			pendingRequestList.removePendingRequest(p);
		}
		
	    GameSession session = client.getGameSession();

	    //Controllo se il client è in una sessione
	    if (session != null) {
	        session.disconnection(client);
	    } else {
		    client.setStatus(Status.free);
		    client.setGameSession(null);
	    }

	}
	

	public void clientHandler() throws IOException{
		// invio il messaggio di inizializzazione della connessione
		client.write(new ServerConnectionResult(client.getUsername()));
		
		for(Client c : clientList) {
			c.write(new PlayListResponse(clientList.getPlayerList()));
		}
		
		// ciclo lettura richiesta ed elaborazione risposta
		while(true) {
			
			Message msgRequest = client.read();
			Message msgResponse;
			
			if(this.client.getStatus() == Status.free) {
				msgResponse = handleMessageLobby(msgRequest);
			}
			else {
				msgResponse = handleMessageGame(msgRequest);
			}
			
			if(msgResponse != null) {
				client.write(msgResponse);
			}
			

		}
	}
	
	public Message handleMessageLobby(Message msg) {

		
			MessageType msgType = msg.getType();
	
	        switch (msgType) {
	
	            case change_username_request:
	                return handleChangeUsernameRequest(msg);
	
	            case play_list_request:
	                return new PlayListResponse(clientList.getPlayerList());
	
	            case challenge_request:
	                return handleChallengeRequest(msg);
	
	            case challenge_response:
	                return handleChallengeResponse(msg);

	
	            default:
	                System.out.println("Unhandled message type in lobby: " + msgType);
	                return null;
	        }
	}
	
	

	public Message handleMessageGame(Message msg) {
		
		MessageType msgType = msg.getType();

        switch (msgType) {

            case move:
                return handleMove(msg);

            case disconnect:
                return handleDisconnect(msg);

            default:
                System.out.println("Unhandled message type in game: " + msgType);
                return null;
        }
}
	
	//
	// Metodi per la gestione nella lobby
	//
	
	private ChangeUsernameResponse handleChangeUsernameRequest(Message generalMsg) {
		ChangeUsernameRequest msg = (ChangeUsernameRequest) generalMsg;
		
		synchronized(clientList) {
			//Controllo che l'username non è stato già preso
			if(clientList.isClientInList(msg.getNewUsername())) {
				return (new ChangeUsernameResponse(ChangeUsernameResult.taken));
			}
			
			//Rifiuto tutte le richieste collegate a quel nome
			for(PendingRequest p : pendingRequestList.popPendingRequests(client))
				refusePendingRequest(p,client);
			
			client.setUsername(msg.getNewUsername());
			
			for(Client c : clientList) {
				if(c == client) {
					continue;
				}
				c.write(new PlayListResponse(clientList.getPlayerList()));
			}
			
			return (new ChangeUsernameResponse(ChangeUsernameResult.ok));
		}
		
	}

	
	private ChallengeResult handleChallengeRequest(Message generalMsg) {
		ChallengeRequest msg = (ChallengeRequest) generalMsg;
		
		Client enemy = clientList.getClient(msg.getUsername());
		
		//Controllo che l'avversario esista
		if(enemy == null) {
			return (new ChallengeResult(ChallengeResultStatus.client_not_found, MoveValue.none, msg.getUsername()));
		}
		
		//Controllo che la richiesta non esista di già all'interno delle lista di pending list
		if(pendingRequestList.isInPendingRequestList(client, enemy)) {
			return null;
		}
		
		//Modifico la richiesta con il mio username
		msg.setUsername(client.getUsername());
		
		pendingRequestList.push(client, enemy);
		enemy.write(msg);

		return null;		
	}
	
	
	private Message handleChallengeResponse(Message generalMsg) {
		ChallengeResponse msg = (ChallengeResponse) generalMsg;
		
		Client enemy = clientList.getClient(msg.getUsername());
		
		// Controllo se il client è ancora online e se c'è un effettiva richiesta
		if(enemy == null || !pendingRequestList.isInPendingRequestList(enemy, client)) {
			pendingRequestList.remove(enemy,client);
			return new ChallengeResult(ChallengeResultStatus.client_not_found,MoveValue.none,enemy.getUsername());
		}
		
		if(msg.getStatus() == ChallengeResponseStatus.refused) {
			
			//Elimino la richiesta
			pendingRequestList.remove(enemy, client);
				
			enemy.write(new ChallengeResult(ChallengeResultStatus.refused,MoveValue.none,client.getUsername()));
			return null;
		
		}
		
		//Se viene accetta:
		// Controllo gli stati dei due giocatori coinvolti e li aggiorno
		synchronized(clientList) {
			
			if(!(client.getStatus().equals(Status.free) && enemy.getStatus().equals(Status.free)) ) {
				pendingRequestList.remove(enemy, client); //Rimuovo la richiesta
				return new ChallengeResult(ChallengeResultStatus.refused,MoveValue.none,enemy.getUsername());
			} 
			
			client.setStatus(Status.in_game);
			enemy.setStatus(Status.in_game);
			pendingRequestList.remove(enemy, client);
			
			//Rifiuto tutte le richieste che riguardano quei due client
			
			for(PendingRequest p : pendingRequestList.popPendingRequests(client))
				refusePendingRequest(p,client);
			
			for(PendingRequest p : pendingRequestList.popPendingRequests(enemy))
				refusePendingRequest(p,enemy);
			
		}
		
		//Qui non è necessario inserire un blocco sincronizzato visto che una volta arrivato a questo punto i client sono 
		//già nella modalità "in_game" e quindi non è possibile che una altra richiesta venga accettata
		
		//Estraggo il primo giocatore
		Random random = new Random();
		Client first = Math.abs(random.nextInt() % 2) == 0? client : enemy;
		MoveValue clientMoveVale = first == client? MoveValue.you:MoveValue.other ;
		MoveValue enemyMoveValue = first == enemy? MoveValue.you:MoveValue.other;
		
		GameSession gameSession = new GameSession(client, enemy, first);
			
		client.setGameSession(gameSession);
		enemy.setGameSession(gameSession);

		// Invio i messaggi di inizio    
		enemy.write( new ChallengeResult(ChallengeResultStatus.ok,enemyMoveValue,client.getUsername()));
		
		
		
		return new ChallengeResult(ChallengeResultStatus.ok,clientMoveVale,enemy.getUsername());
	}
	
	//TODO CONTROLLA SE È CORRETTO
	private void refusePendingRequest(PendingRequest pendingRequest,Client client) {
		
		Client enemy = client == pendingRequest.getSender() ? pendingRequest.getDestination() : pendingRequest.getSender();
		
		if(client == pendingRequest.getSender()) {
			client.write(new ChallengeResult(ChallengeResultStatus.refused,MoveValue.none,pendingRequest.getSender().getUsername()));
			enemy.write(new ChallengeResult(ChallengeResultStatus.client_not_found,MoveValue.none,pendingRequest.getSender().getUsername()));
			return;
		}
		
		enemy.write(new ChallengeResult(ChallengeResultStatus.refused,MoveValue.none,client.getUsername()));
		client.write(new ChallengeResult(ChallengeResultStatus.client_not_found,MoveValue.none,enemy.getUsername()));
		
	}
	
	
	
	//
	// Metodi per la gestione in partita
	//

	private Message handleMove(Message generalMsg) {
		Move msg = (Move) generalMsg;
		GameSession session = client.getGameSession();
		
		// Il metodo insert gestisce gia tute le casistiche della partita (controllo validità, vittoria e messaggio da ritornare)
		return session.insert(client,msg.getColumn());
	}

	private Message handleDisconnect(Message generalMsg) {
		Disconnect msg = (Disconnect) generalMsg;
		GameSession session = client.getGameSession();
		
		if(session != null) {
	        session.disconnection(client);
	    }
		
		return null;
	}
	
}
