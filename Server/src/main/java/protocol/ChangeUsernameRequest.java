package protocol;

import enums.MessageType;

public class ChangeUsernameRequest implements Message{
	
	private MessageType type;
	private String new_username;
	
	public ChangeUsernameRequest() {}
	
	public ChangeUsernameRequest(String new_username) {
		this.type = MessageType.change_username_request;
		this.new_username = new_username;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}

	public String getNewUsername() {
		return this.new_username;
	}
	
	public void setType() {
		this.type = MessageType.change_username_request;
	}
	
	public void setNewUsername(String new_username) {
		this.new_username = new_username;
	}
}
