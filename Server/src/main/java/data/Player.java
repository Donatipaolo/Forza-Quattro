package data;

public class Player {
	private String username;
    private Status status;

    public Player(String username, Status status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() { 
    	return username; 
    }
    
    public Status getStatus() { 
    	return status; 
    }
}
