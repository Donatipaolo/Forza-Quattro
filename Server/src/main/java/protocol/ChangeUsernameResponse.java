package protocol;

public class ChangeUsernameResponse implements Message{
	
	private String type;
	private String status;
	
	public ChangeUsernameResponse(String status) {
		this.type = "change_username_response";
		this.status = status;
	}

	public String getType() {
		return this.type;
	}
}
