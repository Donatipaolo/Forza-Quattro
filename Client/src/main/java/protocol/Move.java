package protocol;

public class Move implements Message{
	private String type;
	private int column;
	
	public Move() {}
	
	public Move(int column) {
		this.type = "move";
		this.column = column;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setType() {
		this.type = "move";
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
}
