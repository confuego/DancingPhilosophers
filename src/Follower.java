import java.util.Hashtable;
import java.util.concurrent.*;

public class Follower extends Thread {
	Buffer b;
	Hashtable<String,Integer> danceCard;
	int id;
	public Follower(Buffer b,Hashtable<String,Integer> table, int id){
		this.b = b;
		this.danceCard = table;
		this.id = id;
	}
	
	public void run(){
		System.out.println("Follower " + id +" has started. ");
		b.insertValue(this);
	}
}