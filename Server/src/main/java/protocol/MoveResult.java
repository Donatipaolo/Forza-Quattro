package protocol;

enum MoveResultStatus{
	ok,
	invalid_move,
	not_your_turn
}

public class MoveResult implements Message{
	private String type;
	private MoveResultStatus status;
	
	public MoveResult() {}
	
	public MoveResult(MoveResultStatus status) {
		this.type = "move_response";
		this.status = status;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	public MoveResultStatus getStatus() {
		return this.status;
	}
	
	public void setType() {
		this.type = "move_response";
	}
	
	public void setStatus(MoveResultStatus status) {
		this.status = status;
	}
}
