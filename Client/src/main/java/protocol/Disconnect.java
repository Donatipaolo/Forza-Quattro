package protocol;

public class Disconnect implements Message{
	private String type;
	
	
	public Disconnect() {
		this.type = "disconnect";
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	public void setType() {
		this.type = "disconnect";
	}
	
}
