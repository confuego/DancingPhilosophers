public class DanceCard {
	int Waltz, Tango, Foxtrot, Quickstep, Rumba, Samba, ChaCha, Jive;

	public DanceCard() {
		// set all dances to 0
		Waltz = 0;
		Tango = 0;
		Foxtrot = 0;
		Quickstep = 0;
		Rumba = 0;
		Samba = 0;
		ChaCha = 0;
		Jive = 0;
	}

	public boolean hasDanced(String name) {
		if (name.equals("Waltz"))
			return (Waltz != 0);
		if (name.equals("Tango"))
			return (Tango != 0);
		if (name.equals("Foxtrot"))
			return (Foxtrot != 0);
		if (name.equals("Quickstep"))
			return (Quickstep != 0);
		if (name.equals("Rumba"))
			return (Rumba != 0);
		if (name.equals("Samba"))
			return (Samba != 0);
		if (name.equals("ChaCha"))
			return (ChaCha != 0);
		if (name.equals("Jive"))
			return (Jive != 0);
		// if dance doesn't exist, we aren't interested so we say we've already
		// done that
		return true;
	}

	public boolean hasDancedWithTwice(int partnerID) {
		int count = 0;
		if (Waltz == partnerID)
			count++;
		if (Tango == partnerID)
			count++;
		if (Foxtrot == partnerID)
			count++;
		if (Quickstep == partnerID)
			count++;
		if (Rumba == partnerID)
			count++;
		if (Samba == partnerID)
			count++;
		if (ChaCha == partnerID)
			count++;
		if (Jive == partnerID)
			count++;

		return (count >= 2);
	}
}
