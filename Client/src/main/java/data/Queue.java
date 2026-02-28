package data;

import java.util.ArrayList;

import protocol.Message;

public class Queue {
	
	private ArrayList<Message> list;
	
	//Un metodo per inserire e un metodo per estrarre
	public Queue() {
		this.list = new ArrayList<Message>();
	}
	
	public synchronized void insert(Message msg) {
		list.add(msg);
		notify();
	}
	
	public synchronized Message remove() {
	
		while(list.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
		Message msg = list.getFirst();
		list.removeFirst();
		return msg;
	}
	
}
