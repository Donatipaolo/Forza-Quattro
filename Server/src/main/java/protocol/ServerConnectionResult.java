package protocol;

public class ServerConnectionResult implements Message {

    private String type;
    private String username;

    public ServerConnectionResult(String username) {
    	this.type = "server_connection_result";
        this.username = username;
    }
    
    public String getType() {
		return this.type;
	}
    
    /*private String generateUsername() {
    	Random random = new Random();
	    String username;
	    do {
	        username = "username" + String.format("%05d", random.nextInt(100000));
	    } while (!list.isUsernameUnique(username));
	    return username;
	}
*/

}
