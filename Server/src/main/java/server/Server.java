package server;

import data.ClientList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/*
 * Thread che rimarrà in ascolto sulla porta:
 * PORTA : 20`000 
 *
 */

public class Server extends Thread implements Runnable{
	
	private ServerSocket serverSocket;
	private ClientList clientList;
	
	public Server(ClientList clientlist){
		
		super("Server");
		
		this.clientList = clientlist;
		
		ConfigLoader configLoader = new ConfigLoader("src/main/resources/configuration.xml");
		
		
		try {
			this.serverSocket = new ServerSocket(configLoader.getPort());
			this.serverSocket.setReuseAddress(true);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		
		try {
			server();
		}catch(IOException e) {
			
		}
	}

	private void server() throws IOException{
		
		//Ascolto sulla porta 20000 l'arrivo di nuovi client e delego la gestione ad un altro componente
		while(true) {
			//Aspetto l'arrivo di un nuovo client dalla socket
			Socket clientSocket = this.serverSocket.accept();
			
			handleNewClient(clientSocket);
		}
	}
	
	private void handleNewClient(Socket clientSocket) {
		//Creo il client e il thread che lo gestisce
		this.clientList.addClient(clientSocket, createUsername());
		ClientHandler clientHandler = new ClientHandler(clientList.getLastClient());
		clientHandler.start();
	}
	
	private String createUsername() {
		Random random = new Random();
	    String username;
	    do {
	        username = "username" + String.format("%05d", random.nextInt(100000));
	    } while (!this.clientList.isUsernameUnique(username));
	    return username;
	}
	
	public synchronized void exit() {
		//Chiudo la socket per risvegliare il thread
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
}
