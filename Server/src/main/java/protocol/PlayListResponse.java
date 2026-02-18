package protocol;

import java.util.ArrayList;
import java.util.List;

import data.*;
import enums.MessageType;

public class PlayListResponse implements Message{
	
	private MessageType type;
	private List<Player> listOfPlayer;
	
	public PlayListResponse() {}
	
	public PlayListResponse(ClientList list) {
		this.type = MessageType.play_list_response;
		listOfPlayer = new ArrayList<Player>();
		
		for(Client client : list) {
			listOfPlayer.add(new Player(client.getUsername(),client.getStatus()));	
		
		}
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}

	public List<Player> getListOfPlayer(){
		return this.listOfPlayer;
	}
	
	public void setType() {
		this.type = MessageType.play_list_response;
	}
	
	public void setListOfPlayer(List<Player> listOfPlayer) {
		this.listOfPlayer = listOfPlayer;
		
	}
}
