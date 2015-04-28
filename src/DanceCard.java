import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicBoolean;

public class DanceCard extends Thread {
	int followerThreadCount;
	static volatile AtomicInteger followerCount = new AtomicInteger(0);
	static volatile long followerId;
	static volatile Hashtable<String,Long> followerCard;
	Hashtable<String,Long> currentCard = new Hashtable<String,Long>(8);
	
	//can the current hashtable for followers dance with anyone else?
	static volatile AtomicBoolean full = new AtomicBoolean(false);
	
	public DanceCard (Hashtable<String,Long> currentCard, int threadCount){
		this.currentCard = currentCard;
		this.followerThreadCount = threadCount;
	}
	
	public void run(){
		System.out.println("Thread ID is: " + Thread.currentThread().getId());
		findLeader();
		findFollower();
	}
	
	public synchronized void findLeader(){
		//current thread is a leader thread, the current hash isn't full, and the hash isn't empty.
		if(Thread.currentThread().getName().equals("L") && !full.get() && followerCard != null){
			currentCard.put("Mamba", followerId);
			full.set(true);
			System.out.println("Added " + currentCard + " to " + Thread.currentThread().getId());
		}
	}
	
	public synchronized void findFollower(){
		if(Thread.currentThread().getName().equals("F") && (full.get() == true || followerCard == null)){
			followerCard = this.currentCard;
			followerId = Thread.currentThread().getId();
			System.out.println("Follower count is: " + followerCount.incrementAndGet() + "\nID is: " + followerId);
			
			
			while(!full.get() && followerCount.get() > followerThreadCount){
				try{
					wait();
				}
				catch(InterruptedException e){e.printStackTrace();}
			}
			notify();
		}
	}
}
