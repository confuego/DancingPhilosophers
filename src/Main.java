import java.util.Hashtable;

public class Main {

	public static void main(String[] args) {
		Hashtable<String,Long> defaultTable = new Hashtable<String,Long>(8);
		defaultTable.put("Waltz", -1L);
		defaultTable.put("Tango", -1L);
		defaultTable.put("Foxtrot", -1L);
		defaultTable.put("Quickstep", -1L);
		defaultTable.put("Rumba", -1L);
		defaultTable.put("Samba", -1L);
		defaultTable.put("Cha Cha", -1L);
		defaultTable.put("Jive", -1L);
		
		Follower[] flist = new Follower[2];
		flist[0] = new Follower(defaultTable);
		flist[1] = new Follower(defaultTable);
		flist[0].start();flist[1].start();
		
		
		Leader l =  new Leader(defaultTable,flist);
		l.start();
	}

}
