public class Main {

	public static void main(String[] args) {
		int numLeaders = Integer.parseInt(args[0]);
		int numFollowers = Integer.parseInt(args[1]);
		int count = 1;
		
		
		Leader[] leaderList = new Leader[numLeaders];
		Follower[] followerList = new Follower[numFollowers];
		Buffer b = new Buffer(numFollowers);
		
		while(count <= numLeaders){
			leaderList[count-1] = new Leader(b,count,numFollowers);
			count++;
		}
		count = 1;
		
		while(count <= numFollowers){
			followerList[count-1] = new Follower(b,count,numLeaders);
			count++;
		}
		
		for(Leader l : leaderList){l.start();}
		for(Follower f : followerList){f.start();}
		try{for(Leader l : leaderList){l.join();}for(Follower f : followerList){f.join();}}catch (InterruptedException e){e.printStackTrace();}
		for(Leader l : leaderList){l.printTable();System.out.println();}
	}

}
