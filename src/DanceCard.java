public class DanceCard {
	int Waltz, Tango, Foxtrot, Quickstep, Rumba, Samba, ChaCha, Jive;
	String type;
	int id;

	public DanceCard(String type, int id) {
		// set all dances to 0
		Waltz = 0;
		Tango = 0;
		Foxtrot = 0;
		Quickstep = 0;
		Rumba = 0;
		Samba = 0;
		ChaCha = 0;
		Jive = 0;
		this.type = type;
		this.id = id;
	}

	public static String[] songList() {
		String[] songList = new String[8];
		songList[0] = "Waltz";
		songList[1] = "Tango";
		songList[2] = "Foxtrot";
		songList[3] = "Quickstep";
		songList[4] = "Rumba";
		songList[5] = "Samba";
		songList[6] = "ChaCha";
		songList[7] = "Jive";

		return songList;
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

	public boolean danceWith(String name, Dancer partner) {
		if (name.equals("Waltz") && (Waltz == 0)) {
			this.Waltz = partner.id;
			return true;
		}
		if (name.equals("Tango") && (Tango != 0)) {
			this.Tango = partner.id;
			return true;
		}
		if (name.equals("Foxtrot") && (Foxtrot != 0)) {
			this.Foxtrot = partner.id;
			return true;
		}
		if (name.equals("Quickstep") && (Quickstep != 0)) {
			this.Quickstep = partner.id;
			return true;
		}
		if (name.equals("Rumba") && (Rumba != 0)) {
			this.Rumba = partner.id;
			return true;
		}
		if (name.equals("Samba") && (Samba != 0)) {
			this.Samba = partner.id;
			return true;
		}
		if (name.equals("ChaCha") && (ChaCha != 0)) {
			this.ChaCha = partner.id;
			return true;
		}
		if (name.equals("Jive") && (Jive != 0)) {
			this.Jive = partner.id;
			return true;
		}
		// if it gets here return false
		return false;
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

	@Override
	public String toString() {

		String toReturn = "\nLeader " + this.id + ":" + "\n";

		if (Waltz != 0)
			toReturn = toReturn + "Waltz\t\twith " + Waltz + "\n";
		else
			toReturn = toReturn + "Waltz\t\t------\n";
		if (Tango != 0)
			toReturn = toReturn + "Tango\t\twith " + Tango + "\n";
		else
			toReturn = toReturn + "Tango\t\t------\n";
		if (Foxtrot != 0)
			toReturn = toReturn + "Foxtrot\t\twith " + Foxtrot + "\n";
		else
			toReturn = toReturn + "Foxtrot\t\t------\n";
		if (Quickstep != 0)
			toReturn = toReturn + "Quickstep\twith " + Quickstep + "\n";
		else
			toReturn = toReturn + "Quickstep\t------\n";
		if (Rumba != 0)
			toReturn = toReturn + "Rumba\t\twith " + Rumba + "\n";
		else
			toReturn = toReturn + "Rumba\t\t------\n";
		if (Samba != 0)
			toReturn = toReturn + "Samba\t\twith " + Samba + "\n";
		else
			toReturn = toReturn + "Samba\t\t------\n";
		if (ChaCha != 0)
			toReturn = toReturn + "Cha Cha\t\twith " + ChaCha + "\n";
		else
			toReturn = toReturn + "Cha Cha\t\t------\n";
		if (Jive != 0)
			toReturn = toReturn + "Jive\t\twith " + Jive + "\n";
		else
			toReturn = toReturn + "Jive\t\t------\n";

		toReturn = toReturn + "\n";

		return toReturn;

	}
}
