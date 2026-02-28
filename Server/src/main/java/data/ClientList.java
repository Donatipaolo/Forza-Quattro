package data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class ClientList implements Iterable<Client>{
	
	private ArrayList<Client> listOfClient;
	
	public ClientList() {
		listOfClient = new ArrayList<Client>();
	}
	
	public synchronized void addClient(Socket socket, String username) {
		listOfClient.add(new Client(socket, username));
	}
	
	public synchronized void removeClient(String username) {
		for(int i = 0; i < listOfClient.size(); i++) {
			if(listOfClient.get(i).getUsername().equals(username)) {
				listOfClient.remove(i);
				return;
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
	
	public Client getLastClient() {
		return listOfClient.getLast();
	}
	
	public boolean isUsernameUnique(String username) {
		for(Client c : listOfClient) {
			if(c.getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isClientInList(String username) {
		for(Client c : listOfClient) {
			if(c.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
    public Iterator<Client> iterator() {
        return new ClientIterator(this.listOfClient);
    }
	
    public ArrayList<Player> getPlayerList()  {
		ArrayList<Player> listOfPlayer = new ArrayList<Player>();
		
		for(Client client : this.listOfClient) {
			listOfPlayer.add(new Player(client.getUsername(),client.getStatus()));	
		
		}
		return listOfPlayer;
    }
}

class ClientIterator implements Iterator<Client>{
	private ArrayList<Client> data;
    private int index = 0;
    
    public ClientIterator(ArrayList<Client> data) {
    	this.data = data;
    }
    
    @Override
    public boolean hasNext() {
        return index < data.size();
    }

    @Override
    public Client next() {
        return data.get(index++);
    }
    
    

} 
