package protocol;

public class PlayListRequest implements Message{
	
	private String type;
	
	public PlayListRequest() {
		this.type = "play_list_request";
	}
	
	public String getType() {
		return this.type;
	}

}
