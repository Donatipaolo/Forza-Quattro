package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import application.MainApplication;
import data.Queue;
import protocol.Message;
import protocol.MessageFormatterParser;

public class NetworkService {
	private Queue sendQueue; //Coda dei messaggi da inviare
	private Queue readQueue; //Coda dei messaggi da leggere
	private Socket socket;
	
	private SendThread sendThread;
	private ListenerThread listenerThread;
	private boolean failed = false;
	

	public NetworkService() {
		sendQueue = new Queue();
		readQueue = new Queue();
		
		// Carichiamo la configurazione
        ConfigLoader config = new ConfigLoader("config.xml");
        String address = config.getAddress();
        int port = config.getPort();
		
		//Inizializzo la connessione
		try {
			this.socket = new Socket(address,port);
			sendThread = new SendThread(sendQueue,new PrintWriter(socket.getOutputStream(),true));
			listenerThread = new ListenerThread(readQueue, new BufferedReader(new InputStreamReader(socket.getInputStream())));
			
		} catch (IOException e) {
			failed = true;
		}

	}
	
	public void exit() {
		sendThread.exit();
		listenerThread.exit();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			sendThread.join();
			listenerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
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
	
	public boolean isFailed() {
		return this.failed;
	}
}

class SendThread extends Thread implements Runnable{
	
	private Queue queue;
	private PrintWriter out;
	private boolean exit = false;
	
	public SendThread(Queue queue,PrintWriter out) {
		super("Send Thread");
		
		this.queue = queue;
		this.out = out;
	}
	
	@Override
	public void run() {
		
		while(!exit) {
			Message msg = queue.remove();
			
			if(msg == null)
				continue;
			
			out.println(MessageFormatterParser.toJson(msg));
			
			if(out.checkError()) {
				break;
			}
			
			out.flush();
		}
	}
	
	public void exit() {
		exit = true;
		queue.insert(null);
	}
}

class ListenerThread extends Thread implements Runnable{
	
	private Queue queue;
	private BufferedReader in;
	private boolean exit = false;
	
	public ListenerThread(Queue queue,BufferedReader in) {
		super("Send Thread");
		this.in = in;
		
		this.queue = queue;
	}
	
	@Override
	public void run() {
		try {
			while(!exit) {
				Message msg = MessageFormatterParser.fromJson(in.readLine());
				
				queue.insert(msg);
			}
		} catch (IOException e) {
			//TODO non so che cosa ci devo mettere
		}
	}
	
	public void exit() {
		exit = true;
		
	}
}
