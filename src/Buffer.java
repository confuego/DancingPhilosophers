public class Buffer {
	Follower[] vals;
	int low,high,size;
	public Buffer(int len){
		vals = new Follower[len];
	}
	
	public synchronized void put(Follower v){
		while(size==vals.length){
			try{this.wait();}catch(InterruptedException e){e.printStackTrace();}
		}
		vals[high] = v;
		high = (high+1)%vals.length;
		size++;
		notifyAll();
	}
	public synchronized void update(Follower f){
		int x=0;
		for(Follower val : vals){
			if(vals[x].equals(f)){
				vals[x] = val;
				notifyAll();
			}
			x++;
		}
	}
	public synchronized Follower get(){
		Follower v;
		
		while(size<=0){
			try{wait();}catch(InterruptedException e){e.printStackTrace();}
		}
		v = vals[low];
		low = (low+1)%vals.length;
		size--;
		notifyAll();
		return v;
	}
}
