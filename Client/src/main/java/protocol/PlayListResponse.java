package protocol;

import java.util.ArrayList;
import java.util.List;

import data.*;

public class PlayListResponse implements Message{
	
	private String type;
	private List<Player> listOfPlayer;
	
	public PlayListResponse() {}
	
	
	@Override
	public String getType() {
		return this.type;
	}

	public List<Player> getListOfPlayer(){
		return this.listOfPlayer;
	}
	
	public void setType() {
		this.type = "play_list_response";
	}
	
	public void setListOfPlayer(List<Player> listOfPlayer) {
		this.listOfPlayer = listOfPlayer;
		
	}
}
