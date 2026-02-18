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
	
	public synchronized void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public synchronized void setStatus(Status status) {
		this.status = status;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
}
