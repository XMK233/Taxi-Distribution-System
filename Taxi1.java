package Week12_Work10;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Taxi1 extends Thread{
	/*Overview: class taxi is a very long class. it has a enormous number of 
	 * properties. some of the most important properties are status and last status,
	 * current position, target position and passenger position
	 * credits, the time of it and current or previous moving direction
	 * and a mobile map. 
	 * taxi can move and stop according to the specific modification of properties above.
	 * So don't you think it is just like a taxi and its driver 
	 * */
	/*表示对象：（像什么车牌号，车辆代号就不写了，就是一个代号而已，写这个真没什么意义）
	 * （时间也不写了，没什么可写的）
	 * int status, lastStatus, credit, oriDir, curDir
	 * Position txLoc, psgLoc, dstLoc
	 * 抽象函数：AF(c) = {status, lastStatus, credit, oriDir, curDir, txLoc, psgLoc, dstLoc}
	 * 不变式：1 <= status, lastStatus <= 4 && 
	 * credit >= 0 && 
	 * 1 <= oriDir, curDir <= 4 && 
	 * txLoc != null && txLoc.equals(psgLoc) || txLoc.equals(dstLoc) || txLoc.equals(new Position(random 0 to 79, random 0 to 79))
	 * */
	Random rand = new Random();
	public int name = 0;
	public String plate = "京B·";
	public int status = 3;//1:serving with passenger on 2:ready without passenger; 3:hounding; 4:stop
	public int lastStatus = 3;
	/*/public boolean beenChosen = false;//linked with a certain request?*/
	public double acmTime = 0;//accumulative waiting time, till 20 change status
	public Position txLoc = new Position();//where is the taxis
	public Position psgLoc = new Position();//where is the passenger
	public Position dstLoc = new Position();//where is the destination
	public int credit = 0;
	public double time = 0;
	public int oriDir = 0;// origin direction, 0 means that initialized
	public int curDir = 0;// current direction
	public boolean trfStopFlag = false;
	public MapInfoOrd mobMap;//mobile map
	public boolean repOK(){
		if (this.status > 4 || this.status < 1 || this.lastStatus > 4 || this.lastStatus < 1 ) return false;
		if (this.credit < 0) return false;
		if (this.oriDir > 4 || this.oriDir < 0 || this.curDir > 4 || this.curDir < 1 ) return false;
		if (this.txLoc == null) return false;
		if (this.txLoc.raw > 79 || this.txLoc.raw < 0 || this.txLoc.col > 79 || this.txLoc.col < 0) return false;
		System.out.println("taxi is OK " + this.name);
		return true;
	}
	//构造操作
	public Taxi1(int a){
		/*REQUEST: int a 
		 * MODIFIED: all of the attributes it has
		 * EFFECT: all of the attributes initialized and name this taxi by using int a;
		 * */
		Random r = new Random();
		this.name = a;
		this.status = 3;
		this.acmTime = 0;
		this.txLoc = new Position(r.nextInt(80), r.nextInt(80));
		this.psgLoc = new Position();
		this.dstLoc = new Position();
		this.curDir = this.nxtDirArb();
		this.oriDir = 0;
		this.trfStopFlag = false;
		this.credit = 0;
		this.time = System.currentTimeMillis();
		this.plate += (String.valueOf(this.name - 1));
		while (this.plate.length() < 8){
			this.plate += String.valueOf(rand.nextInt(10));
		}
		this.setName("taxi" + String.valueOf(this.name));
	}
	//construction
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
				if (!(MapInfoOrd.map[r - 1][c] == 2 || MapInfoOrd.map[r - 1][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 2){
				if (!(MapInfoOrd.map[r][c] == 2 || MapInfoOrd.map[r][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 3){
				if (!(MapInfoOrd.map[r][c - 1] == 1 || MapInfoOrd.map[r][c - 1] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 4){
				if (!(MapInfoOrd.map[r][c] == 1 || MapInfoOrd.map[r][c] == 3)){
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
				if (!(MapInfoOrd.map[r - 1][c] == 2 || MapInfoOrd.map[r - 1][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 2){
				if (!(MapInfoOrd.map[r][c] == 2 || MapInfoOrd.map[r][c] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 3){
				if (!(MapInfoOrd.map[r][c - 1] == 1 || MapInfoOrd.map[r][c - 1] == 3)){
					dir.remove(i);
					i--;
				}
			}
			else if (d == 4){
				if (!(MapInfoOrd.map[r][c] == 1 || MapInfoOrd.map[r][c] == 3)){
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
		short [][]cost = MapInfoOrd.vlink;
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
				short[]temp = MapInfoOrd.vlink[u];
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
						}
						/*this.moveToTarget(this.dstLoc);*/						
					}				
					else{
						/*if (this.dstLoc.equals(this.txLoc)){*/
							System.out.println("taxi " + this.plate + " arrive at (" + this.txLoc.raw + ", " + this.txLoc.col + ")" + 
												", thank you for choosing our taxis.");
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
	}
	//read method
	
}
