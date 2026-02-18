package protocol;

import enums.*;

public class ChangeUsernameResponse implements Message{
	
	private MessageType type;
	private ChangeUsernameResult status;
	
	public ChangeUsernameResponse() {}
	
	public ChangeUsernameResponse(ChangeUsernameResult status) {
		this.type = MessageType.change_username_response;
		this.status = status;
	}

	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public ChangeUsernameResult getStatus() {
		return this.status;
	}
	
	public void setType() {
		this.type = MessageType.change_username_response;
	}
	
	public void setStatus(ChangeUsernameResult status) {
		this.status = status;
	}
}
