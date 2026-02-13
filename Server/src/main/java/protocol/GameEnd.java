package protocol;

public class GameEnd implements Message{
	private String type;
	private String result;
	private String info;
	
	public GameEnd(String result, String info) {
		this.type = "game_end";
		this.result = result;
		this.info = info;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
}
