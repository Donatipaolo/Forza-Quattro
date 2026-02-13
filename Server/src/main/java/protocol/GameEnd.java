package protocol;

enum GameEndResult{
	won,
	tie,
	defeat
}

enum GameEndInfo{
	game_ended,
	enemy_disconnected
}

public class GameEnd implements Message{
	private String type;
	private GameEndResult result;
	private GameEndInfo info;
	
	public GameEnd() {}
	
	public GameEnd(GameEndResult result, GameEndInfo info) {
		this.type = "game_end";
		this.result = result;
		this.info = info;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	public GameEndResult getResult() {
		return this.result;
	}
	
	public GameEndInfo getInfo() {
		return this.info;
	}
}
