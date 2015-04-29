import java.util.Hashtable;
import java.util.concurrent.*;

public class Leader extends Thread {
	Follower[] flist;
	Hashtable<String,Long> currentCard = new Hashtable<String,Long>(8);
	public Leader (Hashtable<String,Long> currentCard,Follower[]  flist){
		this.currentCard = currentCard;
		this.flist = flist;
	}
	
	public void run(){
		synchronized(this){
			System.out.println("Leader Thread ID is: " + getId());
			for(Follower f : flist){
				//System.out.println(f.id);
				f.compare(this,f.id);
				System.out.println("Follower Card: "+f.currentCard);
			}
			System.out.println();
			System.out.println("Leader Card: " + this.currentCard);
		}
	}
}
