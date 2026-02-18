package protocol;

import enums.MessageType;

public class Disconnect implements Message{
	private MessageType type;
	
	
	public Disconnect() {
		this.type = MessageType.disconnect;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public void setType() {
		this.type = MessageType.disconnect;
	}
	
}
