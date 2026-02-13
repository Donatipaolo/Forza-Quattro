package protocol;

import java.util.ArrayList;
import java.util.List;

import data.*;

public class PlayListResponse implements Message{
	
	private String type;
	private List<Player> listOfPlayer;
	
	public PlayListResponse() {
		this.type = "play_list_response";
	}
	
	public String getType() {
		return this.type;
	}
	
	public String toJson(ClientList list) {
		listOfPlayer = new ArrayList<Player>();
		for(Client p : list.getClient(type)) {
			
		}
	}
	
}
