package data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ClientList {
	
	List<Client> listOfClient;
	
	public ClientList() {
		listOfClient = new ArrayList<Client>();
	}
	
	public synchronized void addClient(Socket socket) {
		listOfClient.add(new Client(socket, generateUsername()));
	}
	
	public synchronized void removeClient(String username) {
		for(int i = 0; i < listOfClient.size(); i++) {
			if(listOfClient.get(i).getUsername().equals(username)) {
				listOfClient.remove(i);
			}
		}
	}
	
	public Client getClient(String username) {
		for(int i = 0; i < listOfClient.size(); i++) {
			if(listOfClient.get(i).getUsername().equals(username)) {
				return listOfClient.get(i);
			}
		}
		return null;
	}
	

	private String generateUsername() {
	    Random random = new Random();
	    String username;
	    do {
	        username = "username" + String.format("%05d", random.nextInt(100000));
	    } while (!isUsernameUnique(username));
	    return username;
	}

	
	public boolean isUsernameUnique(String username) {
		for(int i = 0; i < listOfClient.size(); i++) {
			if(listOfClient.get(i).getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}
	
	
}
