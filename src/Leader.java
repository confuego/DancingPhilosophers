import java.util.Hashtable;
public class Leader extends Thread{
	Hashtable<String,Integer> danceCard = new Hashtable<String,Integer>(8);
	Buffer buff;
	int followerCount;
	int id;
	
	public Leader(Buffer b,int id,int followerCount){
		this.followerCount = followerCount;
		buff = b;
		this.id = id;
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
		for(int x=0;x<followerCount;x++){
			Follower f = buff.get();
			//System.out.println("Leader " + this.id + " Got follower: " + f.id);
			this.compare(f);
		}
	}
	
	public void compare(Follower f){
		String[] keyArray = {"Waltz","Tango","Foxtrot","Quickstep","Rumba","Samba","Cha Cha","Jive"};
		int danceCount = 0;
		for(String key  : f.danceCard.keySet()){
			if(this.danceCard.get(key) == f.id){
				danceCount++;
			}
		}
		int count =0;
		while(danceCount < 2 && count < 10000){
			int rand = (int) (Math.random() * 7);
			String key = keyArray[rand];
			if(this.danceCard.get(key) == -1 && f.danceCard.get(key) == -1){
				this.danceCard.put(key, f.id);f.danceCard.put(key, this.id);
				danceCount++;
			}
			count++;
		}
		//notifyAll();
	}
	
	public  void printTable(){
		System.out.println("Leader: " + this.id);
		for(String key : this.danceCard.keySet()){
			if(key.equals("Quickstep") && this.danceCard.get(key)==-1){
				System.out.println(key + "\t------");
			}
			else if(this.danceCard.get(key) == -1)
				System.out.println(key + "\t\t" + "------");
			else if(key.equals("Quickstep") && this.danceCard.get(key)!=-1){
				System.out.println(key + "\twith " + this.danceCard.get(key));
			}
			else
				System.out.println(key + "\t\twith " + this.danceCard.get(key));
		}
	}
	
}
