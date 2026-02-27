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
	
	public synchronized void remove(Client sender, Client destination) {
		for(PendingRequest p : pendingRequestList) {
			if(p.getSender() == sender && p.getDestination() == destination) {
				pendingRequestList.remove(p);
			}
		}
	}
	
	public synchronized ArrayList<PendingRequest> getPendingRequestListBySender(Client sender) {
		
		ArrayList<PendingRequest> requests = new ArrayList<PendingRequest>();
		for(PendingRequest p : pendingRequestList) {
			if(p.getSender() == sender) {
				requests.add(p);
			}
		}
		
		return requests;
	}
	
	public synchronized ArrayList<PendingRequest> getPendingRequestListByDestination(Client destination){
		ArrayList<PendingRequest> requests = new ArrayList<PendingRequest>();
		for(PendingRequest p : pendingRequestList) {
			if(p.getDestination() == destination) {
				requests.add(p);
			}
		}
		
		return requests;
	}
	
	public synchronized void removePendingRequest(PendingRequest pendingRequest) {
		pendingRequestList.remove(pendingRequest);//TODO Spero funzioni
		
	}
	
	public synchronized PendingRequest getPendingRequest(Client sender, Client destination) {
		for(PendingRequest p : pendingRequestList) {
			if(p.getSender() == sender && p.getDestination() == destination) {
				return p;
			}
		}
		
		return null;
	}
	
	public synchronized ArrayList<PendingRequest> getPendingRequestlist(Client client){
		ArrayList<PendingRequest> requests = new ArrayList<PendingRequest>();
		for(PendingRequest p : pendingRequestList) {
			if(p.getSender() == client || p.getDestination() == client) {
				requests.add(p);
			}
		}
		
		return requests;
	}
	
	public synchronized boolean isInPendingRequestList(Client sender, Client destination) {
		for(PendingRequest p : pendingRequestList) {
			if(p.getSender() == sender && p.getDestination() == destination) {
				return true;
			}
		}
		
		return false;
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
