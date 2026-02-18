package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import enums.Status;

public class Client {
	private String username;
	private Status status;
	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;
	
	public Client(Socket socket,String username) {
		this.socket = socket;
		this.username = username;
		status = Status.free;
		
		try {
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public BufferedReader getIn() {
		return this.in;
	}

	public PrintWriter getOut() {
		return this.out;
	}
}
