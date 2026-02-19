package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import data.*;
import enums.*;
import protocol.*;

public class ClientHandler extends Thread implements Runnable{

	//Static attribute
	public static ClientList clientList = null;
	public static PendingRequestList pendingRequestList = null;
	
	private Client client;
	private Socket clientSocket;
	private GameSession gameSession = null;
	private boolean status = false;		//se false vuol dire che è in lobby, true che è in partita
	
	public ClientHandler(Client client) {
		super();
		this.client = client;
		this.clientSocket = client.getSocket();
		
	}
	
	@Override
	public void run() {
		try {
			clientHandler();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void clientHandler() throws IOException{
		// invio il messaggio di inizializzazione della connessione
		client.getOut().print(MessageFormatterParser.toJson(new ServerConnectionResult(client.getUsername())));
		
		// ciclo lettura richiesta ed elaborazione risposta
		while(true) {
			String inputJson = client.getIn().readLine();
			Message msgReply = MessageFormatterParser.fromJson(inputJson);
			
			Message msgResponse = handleMessageGame(msgReply);
			
			if(msgResponse != null) {
				client.getOut().print(MessageFormatterParser.toJson(msgResponse));
			}
			

		}
	}
	
	public Message handleMessageGame(Message msg) {
			
			MessageType msgType = msg.getType();
	
	        switch (msgType) {
	
	            case change_username_request:
	                return handleChangeUsernameRequest(msg, client);
	
	            case play_list_request:
	                return new PlayListResponse(clientList.getPlayerList());
	
	            case challenge_request:
	                return handleChallengeRequest(msg);
	
	            case challenge_response:
	                return handleChallengeResponse(msg, client);
	
	            default:
	                System.out.println("Unhandled message type: " + msgType);
	                return null;
	        }
	}
	
	public Message handleMessageLobby(Message msg) {
		
		MessageType msgType = msg.getType();

        switch (msgType) {

            case move:
                return handleMove(msg);

            case disconnect:
                return handleDisconnect(msg);

            default:
                System.out.println("Unhandled message type: " + msgType);
                return null;
        }
}
	
	private ChangeUsernameResponse handleChangeUsernameRequest(Message generalMsg) {
		ChangeUsernameRequest msg = (ChangeUsernameRequest) generalMsg;
		String new_username = msg.getNewUsername();
		
		if(clientList.isClientInList(new_username)) {
			return (new ChangeUsernameResponse(ChangeUsernameResult.taken));
		}
		
		client.setUsername(new_username);
		return (new ChangeUsernameResponse(ChangeUsernameResult.ok));
	}

	
	private ChallengeResult handleChallengeRequest(Message generalMsg) {
		ChallengeRequest msg = (ChallengeRequest) generalMsg;
		String username = msg.getUsername();
		
		Client enemy = clientList.getClient(username);
		if(enemy == null) {
			return (new ChallengeResult(ChallengeResultStatus.client_not_found, MoveValue.none, username));
		}
		
		enemy.getOut().print(msg);

		return null;		
	}
	
	private Message handleChallengeResponse(Message generalMsg, Client c) {
		ChallengeResponse msg = (ChallengeResponse) generalMsg;
		
		Client enemy = clientList.getClient(msg.getUsername());
		
		if(enemy == null) {
			return new ChallengeResult(ChallengeResultStatus.client_not_found,MoveValue.none,enemy.getUsername());
		}
		
		Client starter = (msg.getFirstMove() == MoveValue.you) ? c : enemy;

		if(c.getStatus().equals(Status.free) && enemy.getStatus().equals(Status.free) ) {
			GameSession gameSession = new GameSession(c, enemy, starter);
		}
		
		return null;
	}

	private Message handleMove(Message msg) {
		// TODO Auto-generated method stub
		return null;
	}

	private Message handleDisconnect(Message msg) {
		return null;
	}
	
}
