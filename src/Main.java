import java.util.Hashtable;


public class Main {

	public static void main(String[] args) {
		Hashtable<String,Long> h = new Hashtable<String,Long>(8);
		h.put("Mamba", 100L);
		Thread t1 = new Thread(new DanceCard(new Hashtable<String,Long>(8)),"L");
		Thread t2 = new Thread(new DanceCard(h),"F");
		h.put("weiner", 50L);
		Thread t3 = new Thread(new DanceCard(h),"F");
		t2.start();t1.start();t3.start();
		try {t1.join();t2.join();t3.join();} 
		catch (InterruptedException e) {e.printStackTrace();}
	}

}
