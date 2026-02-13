package protocol;

public class ChangeUsernameRequest implements Message{
	
	private String type;
	private String old_username;
	private String new_username;
	
	public ChangeUsernameRequest() {}
	
	public ChangeUsernameRequest(String old_username, String new_username) {
		this.type = "change_username_request";
		this.old_username = old_username;
		this.new_username = new_username;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	public String getOldUsername() {
		return this.old_username;
	}
	
	public String getNewUsername() {
		return this.new_username;
	}
	
	public void setType() {
		this.type = "change_username_request";
	}
	
	public void setOldUsername(String old_username) {
		this.old_username = old_username;
	}
	
	public void setNewUsername(String new_username) {
		this.new_username = new_username;
	}
}
