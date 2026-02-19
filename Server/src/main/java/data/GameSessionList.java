package data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class GameSessionList {
private ArrayList<GameSession> listOfGameSession;
	
	public GameSessionList() {
		listOfGameSession = new ArrayList<GameSession>();
	}
	
	public synchronized void addClient(Client client1,Client client2,Client currentClient) {
		listOfGameSession.add(new GameSession());
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
