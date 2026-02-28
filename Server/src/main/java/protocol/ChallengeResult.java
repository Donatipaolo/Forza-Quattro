package protocol;

import enums.*;

public class ChallengeResult implements Message{
	private MessageType type;
	private ChallengeResultStatus status;
	private MoveValue firstMove;
	private String username;
	
	public ChallengeResult() {}
	
	public ChallengeResult(ChallengeResultStatus status, MoveValue firstMove, String username) {
		this.type = MessageType.challenge_result;
		this.status = status;
		this.username = username;
		this.firstMove =firstMove;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public ChallengeResultStatus getStatus() {
		return this.status;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public MoveValue getFirstMove() {
		return this.firstMove;
	}
	
	public void setType() {
		this.type = MessageType.challenge_response;
	}
	
	public void setStatus(ChallengeResultStatus status) {
		this.status = status;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setFirtstMove(MoveValue firstMove) {
		this.firstMove = firstMove;
	}
}
