package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import enums.Status;
import protocol.Message;
import protocol.MessageFormatterParser;

public class Client {
	private String username;
	private Status status;
	private Socket socket;
	private GameSession gameSession;
	private BufferedReader in;
	private PrintWriter out;
	
	public Client(Socket socket,String username) {
		this.socket = socket;
		this.username = username;
		this.gameSession = null;
		this.status = Status.free;
		
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
	
	public Message read() throws IOException {
		synchronized(in) {
			String inputJson = in.readLine();
			if (inputJson == null)
				throw new IOException();
			
			return MessageFormatterParser.fromJson(inputJson);
		}
		
	}
	
	public void write(Message msgResponse) throws IOException {
		synchronized(out) {
			out.println(MessageFormatterParser.toJson(msgResponse));
			out.flush();
		}
		
	}
	
	public BufferedReader getIn() {
		return this.in;
	}

	public PrintWriter getOut() {
		return this.out;
	}
	
	public GameSession getGameSession() {
		return this.gameSession;
	}
	
	public synchronized void setGameSession(GameSession gameSession) {
		this.gameSession = gameSession;
	}
	
}
