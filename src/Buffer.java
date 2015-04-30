import java.util.Hashtable;
import java.util.concurrent.atomic.*;
public class Buffer {
	Follower current;
	AtomicBoolean inUse = new AtomicBoolean(false);
	AtomicBoolean leaderUsing = new AtomicBoolean(false);
	
	public synchronized void insertValue(Follower request){
		if(inUse.get()){
			//System.out.println("Waiting..");
			try {wait();} catch (InterruptedException e) {e.printStackTrace();}
		}
		inUse.set(true);
		current = request;
	}
	
	public synchronized void releaseFollower(){
		notifyAll();
		inUse.set(false);
	}
	
	public synchronized void releaseLeader(){
		leaderUsing.set(false);
		notifyAll();
	}
	
	public synchronized void update(Follower update){
		current = update;
	}
	
	public synchronized Follower takeValue(int id){
		if(current == null)
			return null;
		if(leaderUsing.get())
			try{System.out.println("Leader " + id + " is waiting for Follower " + current.id);wait();}catch(InterruptedException e){e.printStackTrace();}
		System.out.println("Leader " + id +" grabbed Follower " + current.id);
		leaderUsing.set(true);
		return current;
	}
}
