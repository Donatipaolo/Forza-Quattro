package protocol;

import enums.*;

public class ChallengeResponse implements Message{
	private MessageType type;
	private ChallengeResponseStatus status;
	private MoveValue first_move;
	
	public ChallengeResponse() {}
	
	public ChallengeResponse(ChallengeResponseStatus status, MoveValue first_move) {
		this.type = MessageType.challenge_response;
		this.status = status;
		this.first_move = first_move;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public ChallengeResponseStatus getStatus() {
		return this.status;
	}
	
	public MoveValue getFirstMove() {
		return this.first_move;
	}
	
	public void setType() {
		this.type = MessageType.challenge_response;
	}
	
	public void setStatus(ChallengeResponseStatus status) {
		this.status = status;
	}
	
	public void setFirstMove(MoveValue first_move) {
		this.first_move = first_move;
	}
}
