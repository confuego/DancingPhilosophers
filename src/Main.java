import java.util.Hashtable;

public class Main {

	public static void main(String[] args) {
		Hashtable<String,Integer> defaultTable = new Hashtable<String,Integer>(8);
		defaultTable.put("Waltz", -1);
		defaultTable.put("Tango", -1);
		defaultTable.put("Foxtrot", -1);
		defaultTable.put("Quickstep", -1);
		defaultTable.put("Rumba", -1);
		defaultTable.put("Samba", -1);
		defaultTable.put("Cha Cha", -1);
		defaultTable.put("Jive", -1);
		
		Buffer b = new Buffer();
		
		Leader l = new Leader(b,defaultTable,1,2);
		Leader l2 = new Leader(b,defaultTable,2,2);
		Follower f = new Follower(b,defaultTable,1);
		Follower f2 = new Follower(b,defaultTable,2);
		
		
		l.start();l2.start();f.start();f2.start();
		try{l.join();l2.join();f.join();f2.join();}catch (InterruptedException e){e.printStackTrace();}
		
	}

}
