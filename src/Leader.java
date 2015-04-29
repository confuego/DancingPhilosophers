import java.util.Hashtable;
import java.util.concurrent.*;

public class Leader extends Thread {
	private Buffer b;
	private Hashtable<String,Integer> table;
	int id;
	public Leader(Buffer b,Hashtable<String,Integer> table, int id){
		this.b = b;
		this.table = table;
		this.id = id;
	}
	
	public int gId(){
		return this.id;
	}
	
	public void run(){
		System.out.println("Leader: " + gId());
		Follower follower = b.takeValue();
		while(follower == null){
			follower = b.takeValue();
		}
		
		this.compare(follower);
		
	}
	public synchronized boolean compare(Follower current){
		
		int follower_id =  current.gId();
		int danceCount = 0;
		Hashtable<String,Integer> current_table = current.getTable();
		
		
		for(String key : table.keySet()){
			if(table.get(key) == follower_id){
				danceCount++;
			}
		}
		if(danceCount >= 2){
			System.out.println("can't!");
		}
		
		for(String key : current.getTable().keySet()){
			if(table.get(key) == -1 && current_table.get(key) == -1 && danceCount < 2){
				danceCount++;
				table.put(key, current.gId());current_table.put(key, gId());
			}
		}
		current.setTable(current_table);
		b.returnAndRelease(current);
		return true;
	}
	
	public Hashtable<String,Integer> getTable(){
		return this.table;
	}
}
