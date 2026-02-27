

/*package data;

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
	
	public synchronized void removeGameSession() {
		for(int i = 0; i < listOfGameSession .size(); i++) {
			//TODO Bisogna definire la condizione per cui rimuovere il client
		}
	}
	
	public GameSession getGameSession() {
		for(int i = 0; i < listOfGameSession .size(); i++) {
			//TODO Bisogna definire la condizione per cui ottenere il client
		}
		return null;
	}
	
	public GameSession getLastClient() {
		return listOfGameSession .getLast();
	}
	
	
	public boolean isGameSessionInList(String username) {
		for(GameSession s : listOfGameSession) {
			//TODO Bisogna definire la condizione per cui controllare il GameSession
		}
		return false;
	}
	
	@Override
    public Iterator<GameSession> iterator() {
        return new ClientIterator(this.listOfGameSession );
    }
	
    public ArrayList<Player> getPlayerList()  {
		ArrayList<Player> listOfPlayer = new ArrayList<Player>();
		
		for(Client client : this.listOfGameSession ) {
			listOfPlayer.add(new Player(client.getUsername(),client.getStatus()));	
		
		}
		return listOfPlayer;
    }
}

class GameSessionIterator implements Iterator<GameSession>{
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
    public GameSession next() {
        return data.get(index++);
    }
    
    

} 
*/