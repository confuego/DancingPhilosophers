import java.util.Hashtable;
import java.util.concurrent.atomic.*;
public class Buffer {
	private Follower current;
	private AtomicBoolean inUse = new AtomicBoolean(false);
	
	public synchronized void insertValue(Follower request){
		if(inUse.get()){
			System.out.println("Waiting..");
			try {wait();} catch (InterruptedException e) {e.printStackTrace();}
		}
		inUse.set(true);
		current = request;
	}
	public synchronized void returnAndRelease(Follower update){
		current = update;
		notifyAll();
		inUse.set(false);
	}
	public synchronized Follower takeValue(){
		return current;
	}
}
