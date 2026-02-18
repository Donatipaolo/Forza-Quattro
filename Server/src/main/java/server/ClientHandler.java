package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import data.Client;
import protocol.Message;
import protocol.MessageFormatterParser;
import protocol.ServerConnectionResult;

public class ClientHandler extends Thread implements Runnable{

	private Client client;
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private GameSession gameSession = null;
	
	public ClientHandler(Client client) {
		super();
		this.client = client;
		this.clientSocket = client.getSocket();
		
		try {
			this.out = new PrintWriter(clientSocket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
		out.print(MessageFormatterParser.toJson(new ServerConnectionResult(client.getUsername())));
		
		// ciclo lettura richiesta ed elaborazione risposta
		while(true) {
			String inputJson = in.readLine();
			Message msgReply = MessageFormatterParser.fromJson(inputJson);
			
			Message msgResponse = LobbyManager.handleMessage(msgReply, client, gameSession);
			
			if(msgResponse != null) {
				out.print(MessageFormatterParser.toJson(msgResponse));
			}
			

		}
	}
}
