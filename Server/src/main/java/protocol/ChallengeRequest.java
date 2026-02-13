package protocol;

public class ChallengeRequest implements Message{
	private String type;
	private String username;
	
	public ChallengeRequest(String username) {
		this.type = "challenge_request";
		this.username = username;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
}
