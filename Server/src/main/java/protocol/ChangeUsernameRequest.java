package protocol;

public class ChangeUsernameRequest implements Message{
	
	private String type;
	private String old_username;
	private String new_username;
	
	public ChangeUsernameRequest(String old_username, String new_username) {
		this.type = "change_username_request";
		this.old_username = old_username;
		this.new_username = new_username;
	}
	
	public String getType() {
		return this.type;
	}
}
