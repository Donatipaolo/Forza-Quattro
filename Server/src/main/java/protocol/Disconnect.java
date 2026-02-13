package protocol;

public class Disconnect implements Message{
	private String type;
	
	public Disconnect() {
		type = "disconnect";
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
}
