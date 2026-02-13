package protocol;

import java.util.ArrayList;
import java.util.List;

import data.*;

public class PlayListResponse implements Message{
	
	private String type;
	private List<Player> listOfPlayer;
	
	public PlayListResponse(ClientList list) {
		this.type = "play_list_response";
		listOfPlayer = new ArrayList<Player>();
		
		for(Client client : list) {
			listOfPlayer.add(new Player(client.getUsername(),client.getStatus()));	
		
		}
	}
	
	@Override
	public String getType() {
		return this.type;
	}

	
}
