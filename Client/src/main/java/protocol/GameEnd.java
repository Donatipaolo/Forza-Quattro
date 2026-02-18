package protocol;

import enums.*;

public class GameEnd implements Message{
	private MessageType type;
	private GameEndResult result;
	private GameEndInfo info;
	
	public GameEnd() {}
	
	public GameEnd(GameEndResult result, GameEndInfo info) {
		this.type = MessageType.game_end;
		this.result = result;
		this.info = info;
	}
	
	@Override
	public MessageType getType() {
		return this.type;
	}
	
	public GameEndResult getResult() {
		return this.result;
	}
	
	public GameEndInfo getInfo() {
		return this.info;
	}
	
	public void setType() {
		this.type = MessageType.game_end;
	}
	
	public void setResult(GameEndResult result) {
		this.result = result;
	}
	
	public void setInfor(GameEndInfo info) {
		this.info = info;
	}
}
