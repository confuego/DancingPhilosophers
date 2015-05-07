public class Follower extends Dancer {

	public Follower(Buffer b, int id, int otherCount) {
		super(b, id, otherCount);
		this.card = new DanceCard("Follower", id);
	}

	@Override
	public void run() {
		danceCard.put("Waltz", -1);
		danceCard.put("Tango", -1);
		danceCard.put("Foxtrot", -1);
		danceCard.put("Quickstep", -1);
		danceCard.put("Rumba", -1);
		danceCard.put("Samba", -1);
		danceCard.put("Cha Cha", -1);
		danceCard.put("Jive", -1);

		// put each of these into the buffer
		for (int x = 0; x < otherCount; x++) {
			buff.put(this);
		}
	}
}
