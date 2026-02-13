package protocol;

enum ChallengeResponseStatus{
	ok,
	refused,
	client_not_found
}

public class ChallengeResponse implements Message{
	private String type;
	private ChallengeResponseStatus status;
	private String first_move;
	
	public ChallengeResponse(ChallengeResponseStatus status,String first_move) {
		this.status = status;
		this.first_move = first_move;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
}
