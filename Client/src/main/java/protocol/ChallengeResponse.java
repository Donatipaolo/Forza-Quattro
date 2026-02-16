package protocol;

enum ChallengeResponseStatus{
	ok,
	refused,
	client_not_found
}

enum MoveValue{
	you,
	other
}

public class ChallengeResponse implements Message{
	private String type;
	private ChallengeResponseStatus status;
	private MoveValue first_move;
	
	public ChallengeResponse() {}
	
	public ChallengeResponse(ChallengeResponseStatus status, MoveValue first_move) {
		this.type = "challenge_response";
		this.status = status;
		this.first_move = first_move;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	public ChallengeResponseStatus getStatus() {
		return this.status;
	}
	
	public MoveValue getFirstMove() {
		return this.first_move;
	}
	
	public void setType() {
		this.type = "challenge_response";
	}
	
	public void setStatus(ChallengeResponseStatus status) {
		this.status = status;
	}
	
	public void setFirstMove(MoveValue first_move) {
		this.first_move = first_move;
	}
}
