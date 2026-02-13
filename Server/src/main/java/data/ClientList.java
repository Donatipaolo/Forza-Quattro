package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ClientList {
	
	List<Client> listOfClient;
	
	public ClientList() {
		listOfClient = new ArrayList<Client>();
	}
	
	public void addClient(Socket socket) {
		listOfClient.add(new Client(socket, generateUsername()));

	private String generateUsername() {
		
		String username = "username";
		
		do {
			Random random = new Random(1000);
			username += String.format("%05d", random.nextInt());
		
		}while();
			
		return username;
	}
	
	public boolean isUsernameUnique() {
		
		while(listOf)
	}
	
}
