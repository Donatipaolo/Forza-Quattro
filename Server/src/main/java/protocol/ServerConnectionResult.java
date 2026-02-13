package protocol;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerConnectionResult implements Message {

    private String type ;
    private String username;

    public ServerConnectionResult(data.Client c) {
    	this.type = "server_connection_result";
        this.username = c.getUsername();
    }

    public ServerConnectionResult(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readerForUpdating(this).readValue(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }

    @Override
    public String getType() {
        return type;
    }
    
    public String getUsername() {
		return username;
	}
}
