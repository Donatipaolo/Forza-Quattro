package protocol;

import enums.MessageType;

public class PlayListRequest implements Message{
	
	private MessageType type;
	
	public PlayListRequest() {
		this.type = MessageType.play_list_request;
	}
	
	public MessageType getType() {
		return this.type;
	}
	public void setType() {
		this.type = MessageType.play_list_request;
	}
}
