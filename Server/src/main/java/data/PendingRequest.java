package data;

public class PendingRequest {
	private Client sender;
	private Client destination;
	
	public PendingRequest(Client sender, Client destination) {
		this.sender = sender;
		this.destination = destination;
	}
	
	public Client getSender() {
		return this.sender;
	}
	
	public Client getDestination() {
		return this.destination;
	}
	
}
