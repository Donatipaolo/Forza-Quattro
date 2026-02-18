package protocol;

import enums.MessageType;

public class Move implements Message{
	private MessageType type;
	private int column;
	
	public Move() {}
	
	public Move(int column) {
		this.type = MessageType.move;
		this.column = column;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setType() {
		this.type = MessageType.move;
	}
	
	public void setColumn(int column) {
		this.column = column;
	}
}
