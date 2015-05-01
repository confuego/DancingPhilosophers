import java.util.Hashtable;
public class Follower extends Thread {
	Buffer buff;
	int id;
	Hashtable<String,Integer> danceCard = new Hashtable<String,Integer>(8);
	int leaderCount;
	
	public Follower(Buffer b, int id,int leaderCount){
		buff = b;
		this.id = id;
		this.leaderCount = leaderCount;
	}
	
	public void run(){
		danceCard.put("Waltz", -1);
		danceCard.put("Tango", -1);
		danceCard.put("Foxtrot", -1);
		danceCard.put("Quickstep", -1);
		danceCard.put("Rumba", -1);
		danceCard.put("Samba", -1);
		danceCard.put("Cha Cha", -1);
		danceCard.put("Jive", -1);
		for(int x=0;x<leaderCount;x++){
			buff.put(this);
		}
	}
}
