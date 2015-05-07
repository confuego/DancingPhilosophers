import java.util.Hashtable;

public abstract class Dancer extends Thread {
	public Buffer buff;
	public int id, otherCount;
	public Hashtable<String, Integer> danceCard;
	public DanceCard card;

	public Dancer(Buffer b, int id, int otherCount) {
		buff = b;
		this.id = id;
		this.otherCount = otherCount;
		this.danceCard = new Hashtable<String, Integer>(8);
	}

	@Override
	public String toString() {
		return this.card.toString();
	}

	public void compare(Dancer f) {
		String[] keyArray = { "Waltz", "Tango", "Foxtrot", "Quickstep",
				"Rumba", "Samba", "Cha Cha", "Jive" };
		int danceCount = 0;
		for (String key : f.danceCard.keySet()) {
			if (this.danceCard.get(key) == f.id) {
				danceCount++;
			}
		}
		int count = 0;
		while (danceCount < 2 && count < 10000) {
			int rand = (int) (Math.random() * 7);
			String key = keyArray[rand];
			if (this.danceCard.get(key) == -1 && f.danceCard.get(key) == -1) {
				this.danceCard.put(key, f.id);
				this.card.danceWith(key, f);
				f.danceCard.put(key, this.id);
				f.card.danceWith(key, this);
				danceCount++;
			}
			count++;
		}
		// notifyAll();
	}

	public void printTable() {
		System.out.println("Leader: " + this.id);
		for (String key : this.danceCard.keySet()) {
			if (key.equals("Quickstep") && this.danceCard.get(key) == -1) {
				System.out.println(key + "\t------");
			} else if (this.danceCard.get(key) == -1)
				System.out.println(key + "\t\t" + "------");
			else if (key.equals("Quickstep") && this.danceCard.get(key) != -1) {
				System.out.println(key + "\twith " + this.danceCard.get(key));
			} else
				System.out.println(key + "\t\twith " + this.danceCard.get(key));
		}
	}
}
