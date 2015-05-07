public class Leader extends Dancer {

	public Leader(Buffer b, int id, int otherCount) {
		super(b, id, otherCount);
		this.card = new DanceCard("Leader", id);
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

		for (int x = 0; x < this.otherCount; x++) {
			Follower f = buff.get();
			// System.out.println("Leader " + this.id + " Got follower: " +
			// f.id);
			this.compare(f);
		}
	}

}
