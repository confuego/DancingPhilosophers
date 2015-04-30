import java.util.Hashtable;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Leader extends Thread {
	Buffer b;
	Hashtable<String,Integer> danceCard;
	int id;
	int followerCount;

	
	public Leader(Buffer b,Hashtable<String,Integer> table, int id,int followerCount){
		this.b = b;
		this.danceCard = table;
		this.id = id;
		this.followerCount = followerCount;
	}
	
	public void run(){
		for(int x=0;x<followerCount;x++){
			Follower current;
			do{
				current = b.takeValue(id);
			}while(current == null);
			System.out.println("Leader " + id + " is looking at Follower " + current.id);
			b.releaseLeader();
			System.out.println("Leader " + id + " is done looking at Follower " + current.id);
			b.releaseFollower();
		}
	 }
}
