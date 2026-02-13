package data;

import java.net.Socket;

enum Status{
	free,
	in_game
}

public class Client {
	private String username;
	private Status status;
	private Socket socket;
	
	public Client(Socket socket,String username) {
		this.socket = socket;
		this.username = username;
		status = Status.free;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
}
