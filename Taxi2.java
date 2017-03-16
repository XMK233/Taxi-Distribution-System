package Week12_Work10;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

//import Week12_Work10.Poly.trackGen;

public class Taxi2 extends Taxi1{
	private ArrayList<ArrayList>track = new ArrayList();// the track that this taxi been through
	private int srvTime;//serve times
	//构造操作
	public ArrayList<ArrayList> getAllTrack (){
		return this.track;
	}
	public Taxi2(int a) {
		super(a);
		this.plate = "特" + this.plate;
		this.track.clear();
		this.srvTime = 0;
	}
	//更新操作
	public synchronized void compete(){
		/*REQUEST: nothing
		 * MODIFIED: this credit
		 * EFFECT: means "qiang dan" and once "qiang dan", credit add up
		 * */
		/*this.beenChosen = true;*/
		this.credit++;
	}// compete order, +1
	//更新操作
	public synchronized void decided(Request req){
		/*REQUEST:the request that decide to be taken by this taxis
		 * MODIFIED:this credit and both passenger's and destination's position 
		 * EFFECT: get the psgLoc and dstLoc, the credit add;
		 * */
		this.dstLoc.getCopy(req.destination);
		this.psgLoc.getCopy(req.origin);
		this.credit += 3; 
		this.srvTime++;
	}// win the compete, +3 in addition, and get 
	//the passenger location and destination position
	//观察操作（产生一个方向，但还没有行动）
	public synchronized int nxtDirPur(Position tar){// next direction purposeful
		/*REQUEST: position tar, means target
		 * MODIFIED: nothing
		 * EFFECT: generate a direction for arriving the final destination
		 * considering the flow and shortesst path and all the requests 
		 * considered in previous works. 
		 * */
		ArrayList<Integer> dir = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++){
			dir.add(i + 1);
		}
		int r = this.txLoc.getRaw();
		int c = this.txLoc.getCol();
		//
		if (r == 0){
			dir.remove(dir.indexOf(1));
		}
		else if(r == 79){
			dir.remove(dir.indexOf(2));
		}
		if (c == 0){
			dir.remove(dir.indexOf(3));
		}
		else if (c == 79){
			dir.remove(dir.indexOf(4));
		}//remove edge points
		for (int i = 0;!dir.isEmpty() && i < dir.size(); i++){
			int d = (Integer) dir.get(i);
			if (d == 1){
				if (!(MapInfoOrd.stcMap[r - 1][c] == 2 || MapInfoOrd.stcMap[r - 1][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 2){
				if (!(MapInfoOrd.stcMap[r][c] == 2 || MapInfoOrd.stcMap[r][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 3){
				if (!(MapInfoOrd.stcMap[r][c - 1] == 1 || MapInfoOrd.stcMap[r][c - 1] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 4){
				if (!(MapInfoOrd.stcMap[r][c] == 1 || MapInfoOrd.stcMap[r][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
		}//if remove one element, i--;no corresponding access? remove the direction.
		Position fittestPos = new Position(this.txLoc.raw, this.txLoc.col);
		fittestPos.hypothesicalMove(dir.get(0));//fittest position for now
		int shoDist = this.shortestDistance(fittestPos, tar);//shortest distance for now
		double flowMin = MapInfoOrd.vehicleNumTable[this.pos2ver(this.txLoc)][this.pos2ver(fittestPos)];
		int fittestDir = dir.get(0);
		//fewest flow for now
		for (int i = 1 ; i < dir.size(); i++){
			Position tempPos = new Position(this.txLoc.raw, this.txLoc.col);
			tempPos.hypothesicalMove(dir.get(i));
			int dist = this.shortestDistance(tempPos, tar);
			double flow = MapInfoOrd.vehicleNumTable[this.pos2ver(this.txLoc)][this.pos2ver(tempPos)];
			if (dist < shoDist){
				fittestPos.getCopy(tempPos);
				shoDist = dist;//////
				flowMin = flow;//////
				fittestDir = dir.get(i);
			}
			else if (dist == shoDist){
				if (flow < flowMin){
					fittestPos.getCopy(tempPos);
					shoDist = dist;///
					flowMin = flow;///
					fittestDir = dir.get(i);
				}
			}
		}
		return fittestDir;
	}
	public synchronized int getSrvTime(){
		/*EFFECT: return the srvTime in this class
		 */
		return this.srvTime;
	}
	//观察操作
	public synchronized int nxtDirArb(){//next direction arbitrary
		/*REQUEST: nothing
		 * MODIFIED: nothing
		 * EFFECT: generate an arbitrary direction for random movement 
		 * */
		//1:up 2:down 3:left 4:right
		ArrayList<Integer> dir = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++){
			dir.add(i + 1);
		}
		int r = this.txLoc.getRaw();
		int c = this.txLoc.getCol();
		//
		if (r == 0){
			dir.remove(dir.indexOf(1));
		}
		else if(r == 79){
			dir.remove(dir.indexOf(2));
		}
		if (c == 0){
			dir.remove(dir.indexOf(3));
		}
		else if (c == 79){
			dir.remove(dir.indexOf(4));
		}//remove edge points
		for (int i = 0;!dir.isEmpty() && i < dir.size(); i++){
			int d = (Integer) dir.get(i);
			if (d == 1){
				if (!(MapInfoOrd.stcMap[r - 1][c] == 2 || MapInfoOrd.stcMap[r - 1][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 2){
				if (!(MapInfoOrd.stcMap[r][c] == 2 || MapInfoOrd.stcMap[r][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 3){
				if (!(MapInfoOrd.stcMap[r][c - 1] == 1 || MapInfoOrd.stcMap[r][c - 1] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 4){
				if (!(MapInfoOrd.stcMap[r][c] == 1 || MapInfoOrd.stcMap[r][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
		}//if remove one element, i--;no corresponding access? remove the direction.
		if (!dir.isEmpty()){
//	
			Position fittestPos = new Position(this.txLoc.raw, this.txLoc.col);
			fittestPos.hypothesicalMove(dir.get(0));//fittest position for now
			short flowMin = MapInfoOrd.vehicleNumTable[this.pos2ver(this.txLoc)][this.pos2ver(fittestPos)];
			
			//fewest flow for now
			for (int i = 1 ; i < dir.size(); i++){
				Position tempPos = new Position(this.txLoc.raw, this.txLoc.col);
				tempPos.hypothesicalMove(dir.get(i));
				short flow = MapInfoOrd.vehicleNumTable[this.pos2ver(this.txLoc)][this.pos2ver(tempPos)];
				if (flow < flowMin){
					fittestPos.getCopy(tempPos);
					flowMin = flow;///
					dir.remove(i - 1);
					i--;
				}
				else if (flow > flowMin){
					dir.remove(i);
					i--;
				}
			}
//			
			Random rand = new Random();
			int num = dir.size();
			int index = rand.nextInt(num);
			int d = (Integer) dir.get(index);
			return d;
			
		}
		return 0;
	}//move randomly, without any destination
	//更新操作
	public synchronized void moveAlongDir(int i){// move along direction
		/*REQUEST: none
		 * MODIFIED: this txloc
		 * EFFECT: change txloc according to a certain direction
		 * */
		Position fittestPos = new Position(this.txLoc.raw, this.txLoc.col);
		fittestPos.hypothesicalMove(i);
		MapInfoOrd.addVehicleNum(this.txLoc, fittestPos);//modified the vehicle num information
		this.txLoc.raw = fittestPos.raw;//change position information
		this.txLoc.col = fittestPos.col;
	}
	//观察操作
	public synchronized boolean trafficStop(){
		/*REQUEST: this oriDir and curDir
		 * MODIFIED: nothing
		 * EFFECTS: if the taxi should be stoped by traffic light, return true
		 * else return false;
		 * */
		//1:up 2:down 3:left 4:right
		if (this.oriDir == 1 && this.curDir == 1 ||
			this.oriDir == 2 && this.curDir == 2 ||//move forward
			this.oriDir == 1 && this.curDir == 3 ||
			this.oriDir == 2 && this.curDir == 4 ||//move left
			this.oriDir == 1 && this.curDir == 2 ||
			this.oriDir == 2 && this.curDir == 1 //U turn
			){
			if (MapInfoOrd.trfLgtNS[this.txLoc.raw][this.txLoc.col] == 2)// red light
				return true;
		}// north and south direction
		if (this.oriDir == 3 && this.curDir == 3 ||
			this.oriDir == 4 && this.curDir == 4 ||// move forward
			this.oriDir == 3 && this.curDir == 2 ||
			this.oriDir == 4 && this.curDir == 1 ||//move left
			this.oriDir == 3 && this.curDir == 4 ||
			this.oriDir == 4 && this.curDir == 3// U turn
			){
			if (MapInfoOrd.trfLgtWE[this.txLoc.raw][this.txLoc.col] == 2)//red light
				return true;
		}// west and east direction
		return false;
	}
	//观察操作
	public int pos2ver(Position a){
		/*REQUEST: position a 
		 * MODIFIED: none
		 * EFFECT: change position to vertex
		 * */
		int ver = 0;
		ver = a.raw * 80 + a.col;
		return ver;
	}//position to vertex index
	//观察操作
	public synchronized int shortestDistance(Position a, Position b){
		/*REQUEST: two positions, using Dijkstra algorithom
		 * MODIFIED: nothing
		 * EFFECT: return the shortest distance between these two points
		 * */
		short [][]cost = MapInfoOrd.stcVlink;
		short v = (short) pos2ver(a);
		short n = (short) pos2ver(b);
		short []dist = new short[6400];
		short [][]path = new short[6400][6400];
		
		short i = 0,w = 0,u = 0;
		short []pos = new short [6400];
		short []s = new short[6400];
		
		for (i = 0; i < 6400; i++){
			dist[i] = cost[v][i];
			path[i][0] = v;
			pos[i] = 0;
		}
		s[v] = 1;
		//count = 1;
		while(s[n] != 1){
			u = MINDIST(s, dist);
			s[u] = 1;
			path[u][++pos[u]] = u;
			//count++;
			for (w = 0; w < 6400; w++){
				if (/*(w = SERCH_VER(s, dist, u)) == -1*/s[w] == 1 || cost[u][w] == Short.MAX_VALUE)
					continue;
				else{
					if (dist[u] + cost[u][w] < dist[w]){
						dist[w] = (short) (dist[u] + cost[u][w]);
						for (i = 0; i <= pos[u]; i++){
							path[w][i] = path[u][i];
						}
						pos[w] = (short)(pos[u]);
					}
				}
			}
		}
		return getSRPoint(path[n], n);
	}
	//观察操作（中间步骤，脱离了上一个方法，就没有什么实际的意义）
	public static int getSRPoint(short[] indexList, short index) {
		/*REQUEST: short indexList, short index
		 * MODIFIED: nothing
		 * EFFECT: return the final distance of two points
		 * */
		int p = 0;			
		for (int j = 0; j < indexList.length; j++) {
			p++;
			if (indexList[j] == index)
				break;
		}
		return p;
	}
	//观察操作
	public short MINDIST(short []s, short [] dist){
		/*REQUEST: s array and dist array 
		 * MODIFIED: nothing
		 * EFFECT: using the s and dist to find a vertex u that hasn't found shortest path and 
		 * is the closest point to v. return the index
		 * one of Dijkstra algrithom's mediate function
		 * */
		/*mediate function of Dijkstra algorithom*/
		short minVal = -1;
		short index = -1;
		for (short i = 0; i < s.length; i++){
			if (s[i] == 0){
				if (minVal < 0){
					minVal = dist[i];
					index = i;
				}
				else{
					if (dist[i] < minVal){
						minVal = dist[i];
						index = i;
					}
				}
			}
		}
		return index;
	}
	//观察操作
	public short SERCH_VER(short []s, short[]dist, short u){
		/*REQUEST: s, dist, u in the Dijkstra algorithm
		 * MODIFIED: none
		 * EFFECT: find a vertex 1. can be reached directly through u
		 * 2. hasn't found the shortest path
		 * if finded, return index
		 * else return -1;
		 * one of Dijkstra algrithom's mediate function
		 * */
		/*mediate function of Dijkstra algorithom*/
		for (int i = 0; i < s.length; i++){
			if (s[i] == 0){
				short[]temp = MapInfoOrd.stcVlink[u];
				for (int j = 0; j < 6400; j++){
					if (temp[j] != Short.MAX_VALUE){
						if(s[j] == 0)
							return (short) j;
					}
				}
			}
		}
		return -1;
	}//all the corresponding methods related to minimum distance
	/*public synchronized void purposefulMove(ArrayList<Position> r){
		if (!r.isEmpty()){
			Position p = r.get(0);
			this.txLoc.col = p.col;
			this.txLoc.raw = p.raw;
			r.remove(0);
		}		
	}//move with a specific destination, move along a certain road*/
	//important methods
	//run操作
	public void run(){
		ArrayList<Position> trackTemp = new ArrayList<Position>();
		while(true){
			synchronized (this){
				if (this.status == 1){
					if (!this.txLoc.posEquals(this.dstLoc) ){/*!this.TDRoute.isEmpty()*/
						if (!this.trfStopFlag){
							this.oriDir = this.curDir;
							this.curDir = this.nxtDirPur(this.dstLoc);
						}
						if (this.trafficStop()){
							this.trfStopFlag = true;
						}// actually do nothing, just stop there and change a flag
						else{
							this.trfStopFlag = false;
							try {
								sleep(1000 / 10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							this.moveAlongDir(this.curDir);
							System.out.print(this.txLoc.toString());
							Position t = new Position(this.txLoc.raw, this.txLoc.col);//
							trackTemp.add(t);//add track
						}
						/*this.moveToTarget(this.dstLoc);*/						
					}				
					else{
						/*if (this.dstLoc.equals(this.txLoc)){*/
							System.out.println("taxi " + this.plate + " arrive at (" + this.txLoc.raw + ", " + this.txLoc.col + ")" + 
												", thank you for choosing our taxis.");
							Position t = new Position(this.txLoc.raw, this.txLoc.col);//add track
							//trackTemp.add(t);
							this.track.add(trackTemp);
							trackTemp = new ArrayList<Position>();// store the track in this track table
							this.lastStatus = 1;
							this.status = 4;
						/*}*/
					}//whether is arrived
				}//going with passenger on
				else if (this.status == 2){
					if (!this.txLoc.posEquals(this.psgLoc)){
						if (!this.trfStopFlag){
							this.oriDir = this.curDir;
							this.curDir = this.nxtDirPur(this.psgLoc);
						}
						if (this.trafficStop()){
							this.trfStopFlag = true;
						}// actually do nothing, just stop there and change a flag
						else{
							this.trfStopFlag = false;
							try {
								sleep(1000 / 10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							this.moveAlongDir(this.curDir);
							System.out.print(this.txLoc.toString());
						}
						/*try {
							sleep(1000 / 10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						this.moveToTarget(this.psgLoc);*/
					}
					else {
						/*if (this.psgLoc.equals(this.txLoc)){*/
							System.out.println("taxi " + this.plate + " arrive at (" + this.txLoc.raw + ", " + this.txLoc.col + ")" + 
								" to pick up passenger");
							this.lastStatus = 2;
							this.status = 4;
						/*}*/
					}//whether is arrived
				}// go to pick up passenger
				else if (this.status == 3){
					if (!this.trfStopFlag){
						this.oriDir = this.curDir;
						this.curDir = this.nxtDirArb();
					}
					if (this.trafficStop()){
						this.trfStopFlag = true;
					}// actually do nothing, just stop there and change a flag
					else{
						this.trfStopFlag = false;
						try {
							sleep(1000 / 10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						this.moveAlongDir(this.curDir);
					}
					/*try {  
						sleep(1000 / 10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.acmTime += 0.1;
					this.randomMove();*/
					if (this.acmTime == 20.0){
						this.status = 4;
						this.lastStatus = 3;
					}
				}// random move to hound requests
				else if (this.status == 4){
					try {
						sleep(1000 * 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (this.lastStatus == 1){
						this.status = 3;
					}//arrive destination, this request has accomplished
					else if (this.lastStatus == 2){
						this.status = 1;
					}//arrive where passengers are
					else if (this.lastStatus == 3){
						this.status = 3;
					}//
					this.acmTime = 0;// no matter how the taxi moved, if it stop, this property clear to 0;
				}//stop, neglect any request, and change next status
			}
		}
	}
	//观察操作
	public synchronized void readInfo(){
		/*REQUEST: nothing
		 * MODIFIED: nothing
		 * EFFECT: print some information about this taxi
		 * */
		//这里可以扩展。
		// 浣犲彲浠ョ敤Position绫荤殑toString()鏂规硶杩斿洖鍙嶆槧涓�涓偣鐨勫潗鏍囩殑瀛楃涓层��
		System.out.println("Taxi " + this.name + " " + this.plate +" is in: (" + this.txLoc.getRaw() + ", " + 
							this.txLoc.getCol() + ") "+"at " + 
							(double)Math.round((System.currentTimeMillis() - this.time)/100)/10 + "s."
							+ " of which status is: " + this.status/* + " " + this.lastStatus + " " + this.credit*/);
		for (int i = 0; i < this.track.size(); i++){
			for (int j = 0; j < this.track.get(i).size(); j++){
				Position a = (Position)this.track.get(i).get(j);
				System.out.print(a.toString());
			}
		}
	}
	//read method	
	public Iterator trackOfThisTaxi(int i){
		/*	REQUEST: int i
		 * MODIFIED: none
		 * EFFECT: return a iterator about a specific track
		 * */
		return new TrackGen(this, i);
	}//i means that which one of track you want to get
}
class TrackGen implements Iterator{
	/*Overview: this is a iterator that can iterate position in arrayList. this iterator will be
	 * impliment in Taxi2 class
	 * */
	/*表示对象：arrayList t, int n
	 * 抽象函数：AF(c) = (t, n)
	 * 不变式：t != null &&
	 * n >= 0 && n <= 6400 &&
	 * p >= -1 && p < 6400 &&
	 * name >= 1 && name <= 100
	 * num >= 0
	 * */
	private ArrayList<Position> t = new ArrayList<Position>();
	private int name = 0;
	private int num = 0;
	private int n = 0;
	private int p = 0;
	public int getName(){
		/*EFFECT: this taxi's name
		 * */
		return this.name;
	}
	public int getNum(){
		/*EFFECT: return this num
		 * 	*/
		return this.num;
	}
	public TrackGen (Taxi2 i, int index){
		/*	REQUEST: int i and a taxi
		 * MODIFIED: none
		 * EFFECT: get the copy of one of the track of taxi's and number of serve times
		 * */
		this.t = (ArrayList) i.getAllTrack().get(index).clone();
		this.num = i.getSrvTime();
		this.name = i.name;
		this.p = t.size() - 1;
	}

	public boolean hasNext() {
		/*	REQUEST: none
		 * MODIFIED: none
		 * EFFECT: if there is still have something remain, return true
		 * else return false;
		 * */
		return this.n < this.t.size();
	}
	public boolean hasPrevious(){
		/*	REQUEST: none
		 * MODIFIED: none
		 * EFFECT: if there is still have something ahead, return true
		 * else return false;
		 * */
		return this.p >= 0;
	}
	public Object next(){		
		/*	REQUEST: none
		 * MODIFIED: n
		 * EFFECT: if there are still have something(position in this case) to return, return the first one 
		 * that can be returned. then add the number of n(up limit)
		 * */
       for(int e = this.n; e < this.t.size(); e++){
    	   this.n= e+1;
    	   Position a = new Position(t.get(e).raw, t.get(e).col);
    	   return a.toString();
       }
       return null;
	}// end PolyGen
	public Object previous(){
		/*	REQUEST: none
		 * MODIFIED: n
		 * EFFECT: if there are still have something(position in this case) to return, return the first one 
		 * that can be returned. then subtract the number of p(down limit)
		 * */
		for (int e = this.p; e >= 0; e--){
			this.p = e - 1;
			Position a = new Position(this.t.get(e).raw, this.t.get(e).col);
			return a.toString();
		}
		return null;
	}
	public boolean repOK(){
		if (this.t == null) return false;
		if (this.n >= 6400 ||
			this.n  < 0 || 
			this.p < -1 || this.p > 6400 ||
			this.num < 0 || 
			this.name < 1 || this.name > 100)
			return false;
		return true;
	}
}