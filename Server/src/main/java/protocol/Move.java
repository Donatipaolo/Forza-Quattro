package protocol;

public class Move implements Message{
	private String type;
	private int column;
	
	public Move(int column) {
		this.type = "move_response";
		this.column = column;
	}
	
	public String getType() {
		return this.type;
	}
}
