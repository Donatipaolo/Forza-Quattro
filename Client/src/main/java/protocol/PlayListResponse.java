package protocol;

import java.util.ArrayList;
import java.util.List;

import data.*;
import enums.MessageType;

public class PlayListResponse implements Message{
	
	private MessageType type;
	private ArrayList<Player> listOfPlayer;
	
	public PlayListResponse() {}
	
	public PlayListResponse(ArrayList<Player> list) {
		this.type = MessageType.play_list_response;
		this.listOfPlayer = list;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}

	public ArrayList<Player> getListOfPlayer(){
		return this.listOfPlayer;
	}
	
	public void setType() {
		this.type = MessageType.play_list_response;
	}
	
	public void setListOfPlayer(ArrayList<Player> listOfPlayer) {
		this.listOfPlayer = listOfPlayer;
		
	}
}
