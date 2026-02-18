package protocol;

import com.fasterxml.jackson.annotation.JsonCreator;

public class ServerConnectionResult implements Message {

    private String type;
    private String username;
    
    
    public ServerConnectionResult(String username) {
    	this.type = "server_connection_result";
        this.username = username;
    }
    
    public ServerConnectionResult() {}
    
    public String getType() {
		return this.type;
	}
    
    public String getUsername() {
    	return this.username;
    }
    
    public void setType() {
		this.type = "server_connection_result";
	}
    
    public void setUsername(String username) {
    	this.username = username;
    }
    


}
