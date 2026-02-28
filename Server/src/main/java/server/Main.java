package server;

import data.ClientList;
import data.PendingRequestList;

public class Main {

	public static void main(String[] args) {
		
		startServer();
		
		while(true) {
			
		}
	}
	
	public static void startServer() {
		//Creo la lista dei client
		ClientHandler.clientList = new ClientList();
				
		//Creo la pending list
		ClientHandler.pendingRequestList = new PendingRequestList();
				
		//Creo il Server
		Server server = new Server(ClientHandler.clientList);
		server.start();
				
		System.out.println("Server avviato in ascolto sulla porta 20000 ...");
	}

}
