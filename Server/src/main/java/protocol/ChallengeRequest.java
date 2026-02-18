package protocol;

import enums.MessageType;

public class ChallengeRequest implements Message{
	private MessageType type;
	private String username;
	
	public ChallengeRequest() {}
	
	public ChallengeRequest(String username) {
		this.type = MessageType.challenge_request;
		this.username = username;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setType() {
		this.type = MessageType.challenge_request;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}
