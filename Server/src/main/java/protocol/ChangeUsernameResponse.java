package protocol;

enum ChangeUsernameResult{
	ok,
	invalid,
	taken
}


public class ChangeUsernameResponse implements Message{
	
	private String type;
	private ChangeUsernameResult status;
	
	public ChangeUsernameResponse() {}
	
	public ChangeUsernameResponse(ChangeUsernameResult status) {
		this.type = "change_username_response";
		this.status = status;
	}

	@Override
	public String getType() {
		return this.type;
	}
	
	public ChangeUsernameResult getStatus() {
		return this.status;
	}
}
