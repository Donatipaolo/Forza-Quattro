package data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class ClientList implements Iterable<Client>{
	
	private List<Client> listOfClient;
	
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
	
	public boolean isUsernameUnique(String username) {
		for(Client c : listOfClient) {
			if(c.getUsername().equals(username)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
    public Iterator<Client> iterator() {
        return new ClientIterator(this.listOfClient);
    }
	
}

class ClientIterator implements Iterator<Client>{
	private List<Client> data;
    private int index = 0;
    
    public ClientIterator(List<Client> data) {
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
