package protocol;

public class GameEnd implements Message{
	private String type;
	private String result;
	private String info;
	
	public GameEnd(String result, String info) {
		this.result = result;
		this.info = info;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
}
