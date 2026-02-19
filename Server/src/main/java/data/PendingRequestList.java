package data;

import java.util.ArrayList;
import java.util.Iterator;

public class PendingRequestList implements Iterable<PendingRequest>{
	private ArrayList<PendingRequest> pendingRequestList;
	
	public PendingRequestList() {
		this.pendingRequestList = new ArrayList<PendingRequest>();
	}
	
	public synchronized void push(Client sender, Client destination) {
		pendingRequestList.add(new PendingRequest(sender,destination));
	}
	
	public synchronized ArrayList<PendingRequest> getPendingRequestBySender(Client sender) {
		
		ArrayList<PendingRequest> requests = new ArrayList<PendingRequest>();
		for(PendingRequest p : pendingRequestList) {
			if(p.getSender() == sender) {
				requests.add(p);
			}
		}
		
		return requests;
	}
	
	public synchronized ArrayList<PendingRequest> getPendingRequestByDestination(Client destination){
		ArrayList<PendingRequest> requests = new ArrayList<PendingRequest>();
		for(PendingRequest p : pendingRequestList) {
			if(p.getDestination() == destination) {
				requests.add(p);
			}
		}
		
		return requests;
	}
	
	public synchronized PendingRequest getPendingRequest(Client sender, Client destination) {
		for(PendingRequest p : pendingRequestList) {
			if(p.getSender() == sender && p.getDestination() == destination) {
				return p;
			}
		}
		
		return null;
	}
	
	@Override
    public Iterator<PendingRequest> iterator() {
        return new PendingRequestIterator(this.pendingRequestList);
    }
}

class PendingRequestIterator implements Iterator<PendingRequest>{
	private ArrayList<PendingRequest> data;
    private int index = 0;
    
    public PendingRequestIterator(ArrayList<PendingRequest> data) {
    	this.data = data;
    }
    
    @Override
    public boolean hasNext() {
        return index < data.size();
    }

    @Override
    public PendingRequest next() {
        return data.get(index++);
    }
}
