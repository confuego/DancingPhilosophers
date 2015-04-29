import java.util.Hashtable;
import java.util.concurrent.*;

public class Follower extends Thread {
	int danceLimit;
	long id;
	Hashtable<String,Long> currentCard = new Hashtable<String,Long>(8);
	public Follower (Hashtable<String,Long> currentCard){
		this.currentCard = currentCard;
		this.id = getId();
	}
	
	public void run(){
		synchronized(this){
			System.out.println("Blocking thread ID: " + id);
			try {wait();} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public synchronized void compare(Leader l, long fid) {
		for (String key : l.currentCard.keySet()){
			if(this.currentCard.get(key) == -1L && l.currentCard.get(key) == -1L && danceLimit<2){
				this.currentCard.put(key,l.getId());
				l.currentCard.put(key, fid);
				danceLimit++;
			}
			else if(danceLimit >=2)
				danceLimit = 0;				
		}
		notify();
	}

	private synchronized boolean isAvailable(long id) {
		int count = 0;
		System.out.println("Id is: " + id);
		for (String key : this.currentCard.keySet()){
			if(this.currentCard.get(key) != id)
				count++;	
		}
		if(count == 0 || count == 1)
			return true;
		return false;
	}
}