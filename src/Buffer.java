import java.util.Hashtable;
import java.util.concurrent.atomic.*;
public class Buffer extends Thread {
	private Hashtable<String,Integer> currentLeader;
	AtomicBoolean inUse = new AtomicBoolean(false);
	public synchronized void insertValue(Hashtable<String,Integer> request){
		if(inUse.get()){
			System.out.println("Waiting..");
			try {wait();} catch (InterruptedException e) {e.printStackTrace();}}
		inUse.set(true);
		currentLeader = request;
	}
	public synchronized Hashtable<String,Integer> takeValue(){
		System.out.println("Taking..");
		notifyAll();
		inUse.set(false);
		return currentLeader;
	}
}
