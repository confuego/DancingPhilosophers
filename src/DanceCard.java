import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class DanceCard extends Thread {
	static AtomicInteger followerCount = new AtomicInteger(0);
	static long followerId;
	static Hashtable<String,Long> followerCard;
	Hashtable<String,Long> currentCard = new Hashtable<String,Long>(8);
	
	//can the current hashtable for followers dance with anyone else?
	static AtomicBoolean full = new AtomicBoolean(false);
	
	public DanceCard (Hashtable<String,Long> currentCard){
		this.currentCard = currentCard;
	}
	
	public void run(){
		findLeader();
		findFollower();
	}
	
	public synchronized void findLeader(){
		//current thread is a leader thread, the current hash isn't full, and the hash isn't empty.
		if(Thread.currentThread().getName().equals("L") && !full.get() && followerCard != null){
			System.out.println("Added!");
			currentCard.put("Mamba", followerId);
			full.set(true);
		}
	}
	
	public synchronized void findFollower(){
		// is my current thread a follower and is the current followerCard full or has never been touched?
	    // then notify the current follower thread and then wait the current thread.
		if(Thread.currentThread().getName().equals("F") && (full.get() == true || followerCard == null)){
			followerCard = this.currentCard;
			followerId = Thread.currentThread().getId();
			System.out.println(followerCount.incrementAndGet());
			
			// if we have not seen all the followers cards yet then keep waiting; otherwise notify all the threads to go
			if(followerCount.get()<2){
				try {wait();} 
				catch (InterruptedException e) {e.printStackTrace();}}
			else notifyAll();
		}
	}
}
