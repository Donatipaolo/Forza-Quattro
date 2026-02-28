package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import data.Queue;
import protocol.Message;
import protocol.MessageFormatterParser;

public class NetworkService {
	private Queue sendQueue; //Coda dei messaggi da inviare
	private Queue readQueue; //Coda dei messaggi da leggere
	private Socket socket;
	
	private SendThread sendThread;
	private ListenerThread listenerThread;
	
	private final int PORT = 20000;
	private final String ADDRESS = "localhost";

	public NetworkService() {
		sendQueue = new Queue();
		readQueue = new Queue();
		
		//Inizializzo la connessione
		try {
			this.socket = new Socket(ADDRESS,PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Creo i due thread
		try {
			sendThread = new SendThread(sendQueue,new PrintWriter(socket.getOutputStream(),true));
			listenerThread = new ListenerThread(readQueue, new BufferedReader(new InputStreamReader(socket.getInputStream())));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public Queue getReadQueue() {
		return this.readQueue;
	}
	
	public Queue getSendQueue() {
		return this.sendQueue;
	}

	public void start() {
		//Avvio i thread
		sendThread.start();
		listenerThread.start();
	}
}

class SendThread extends Thread implements Runnable{
	
	private Queue queue;
	private PrintWriter out;
	
	public SendThread(Queue queue,PrintWriter out) {
		super("Send Thread");
		
		this.queue = queue;
		this.out = out;
	}
	
	@Override
	public void run() {
		while(true) {
			Message msg = queue.remove();
			out.println(msg);
			
			if(out.checkError()) {
				break;
			}
			
			out.flush();
		}
	}
}

class ListenerThread extends Thread implements Runnable{
	
	private Queue queue;
	private BufferedReader in;
	
	public ListenerThread(Queue queue,BufferedReader in) {
		super("Send Thread");
		this.in = in;
		
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				Message msg = MessageFormatterParser.fromJson(in.readLine());
				queue.insert(msg);
			}
		} catch (IOException e) {
			//TODO non so che cosa ci devo mettere
		}
	}
}
