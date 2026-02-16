package protocol;

public class ChallengeRequest implements Message{
	private String type;
	private String username;
	
	public ChallengeRequest() {}
	
	public ChallengeRequest(String username) {
		this.type = "challenge_request";
		this.username = username;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setType() {
		this.type = "challenge_request";
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}
