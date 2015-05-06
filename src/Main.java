public class Main {

	public static void main(String[] args) {

		// declare some needed variables
		int numLeaders, numFollowers, count;

		// set the leaders and followers if not passed in
		if (args.length < 2) {
			System.err.println("Defaulting to 2 leaders and 8 followers");
			numLeaders = 2;
			numFollowers = 8;
		} else {
			numLeaders = Integer.parseInt(args[0]);
			numFollowers = Integer.parseInt(args[1]);
		}

		// set the count equal to 1
		count = 1;

		Leader[] leaderList = new Leader[numLeaders];
		Follower[] followerList = new Follower[numFollowers];
		Buffer b = new Buffer(numFollowers);

		while (count <= numLeaders) {
			leaderList[count - 1] = new Leader(b, count, numFollowers);
			count++;
		}
		count = 1;

		while (count <= numFollowers) {
			followerList[count - 1] = new Follower(b, count, numLeaders);
			count++;
		}

		for (Leader l : leaderList) {
			l.start();
		}
		for (Follower f : followerList) {
			f.start();
		}
		try {
			for (Leader l : leaderList) {
				l.join();
			}
			for (Follower f : followerList) {
				f.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Leader l : leaderList) {
			l.printTable();
			System.out.println();
		}
	}

}
