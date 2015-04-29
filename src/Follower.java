import java.util.Hashtable;
import java.util.concurrent.*;

public class Follower extends Thread {
	private Buffer b;
	private Hashtable<String,Integer> table;
	private int id;
	public Follower(Buffer b,Hashtable<String,Integer> table, int id){
		this.b = b;
		this.table = table;
		this.id = id;
	}
	
	public void setTable(Hashtable<String,Integer> update){
		this.table = update;
	}
	
	public Hashtable<String,Integer> getTable(){
		return this.table;
	}
	public int gId(){
		return this.id;
	}
	
	public void run(){
		System.out.println("Follower: " + gId());
		b.insertValue(this);
	}
}