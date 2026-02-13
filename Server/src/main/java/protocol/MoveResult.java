package protocol;

enum MoveResultStatus{
	ok,
	invalid_move,
	not_your_turn
}

public class MoveResult implements Message{
	private String type;
	private MoveResultStatus status;
	
	public MoveResult(MoveResultStatus status) {
		this.type = "move_response";
		this.status = status;
	}
	
	public String getType() {
		return this.type;
	}
}
