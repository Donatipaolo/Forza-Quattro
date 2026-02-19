package protocol;

import enums.*;

public class ChallengeResponse implements Message{
	private MessageType type;
	private ChallengeResponseStatus status;
	private String username;
	
	public ChallengeResponse() {}
	
	public ChallengeResponse(ChallengeResponseStatus status, String username) {
		this.type = MessageType.challenge_response;
		this.status = status;
		this.username = username;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public ChallengeResponseStatus getStatus() {
		return this.status;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setType() {
		this.type = MessageType.challenge_response;
	}
	
	public void setStatus(ChallengeResponseStatus status) {
		this.status = status;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}
