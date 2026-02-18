package protocol;

import com.fasterxml.jackson.annotation.JsonCreator;

import enums.MessageType;

public class ServerConnectionResult implements Message {

    private MessageType type;
    private String username;
    
    
    public ServerConnectionResult(String username) {
    	this.type = MessageType.server_connection_result;
        this.username = username;
    }
    
    public ServerConnectionResult() {}
    
    public MessageType getType() {
		return this.type;
	}
    
    public String getUsername() {
    	return this.username;
    }
    
    public void setType() {
		this.type = MessageType.server_connection_result;
	}
    
    public void setUsername(String username) {
    	this.username = username;
    }
    


}
