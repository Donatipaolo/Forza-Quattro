package data;

import java.net.Socket;

enum Status{
	Free,
	InGame
}

public class Client {
	private String username;
	private Status status;
	private Socket socket;
	
	public Client(Socket socket,String username) {
		this.socket = socket;
		this.username = username;
		status = Status.Free;
	}
	
	public void changeUsername(String username) {
		this.username = username;
	}
	
}
