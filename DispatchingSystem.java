package Week12_Work10;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*以下三个类是与抛出异常的相关的类，它们中每一个类都与其他的类密切相关。
 *由于这些类与本程序所要求实现的功能关联极小，给这几个类写文档没有意义。
 *基本法也未要求对于抛出异常的catch要达到什么标准，所以在此声明，
 *不给这三个类写任何文档，仅仅保留一些方法规格，助于阅读者理解。*/
class ExceptionThread2 implements Runnable  
{  
      
    /* (non-Javadoc) 
     * @see java.lang.Runnable#run() 
     */  
    public void run()  
    {     
        throw new RuntimeException();  
    }  
}
class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler  
{  
  
    public void uncaughtException(Thread thread, Throwable throwable)  
    {  
		/*REQUEST: thread and a throwable
		 * MODIFIED: nothing
		 * EFFECT: print throwable information
		 * */
        // TODO Auto-generated method stub  
        System.out.println("caught "+throwable);  
    }  
      
}  
class HandlerThreadFactory implements ThreadFactory  
{  
      
    
    public Thread newThread(Runnable runnable)  
    {  
		/*REQUEST: a runnable
		 * MODIFIED: nothing
		 * EFFECT: create a new thread and return it, do some set-up
		 * */
        // TODO Auto-generated method stub  
        Thread t = new Thread(runnable);  
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());  
        return t;  
    }  
      
} 
public class DispatchingSystem extends Thread{
	/*Overview: most important class. it has a map, a cluster of taxis, a request tray and some 
	 * inputing  and retrieving class.
	 * map is static. input and retrieve methods are always ready to function
	 * in this class all the requests will be changed to the information that prepared for a certain taxi
	 * or no taxi. 
	 *  
	 * */
	/*表示对象：RequestTray RT, Producer reqProducer, reqConsumer, 
	 * ArrayList <Request> allRequest
	 * MapInfo mapInfoPointer
	 * 抽象函数： AF(c) = {RT, reqProducer, reqConsumer}
	 * 不变式：RT != null && reqProducer != null && reqConsumer != null  &&
	 * mapInfoPointer != null
	 * */
	//public ArrayList<Taxi1> cptQueue = new ArrayList<Taxi1>();//compete queue
	//public ArrayList<Integer> cptIndex = new ArrayList<Integer>();//compete queue index, indicating which one is the final decision if there are two taxi is comparable
	
	public RequestTray RT = new RequestTray();
	public Producer reqProducer = new Producer(this.RT);
	public Consumer reqConsumer = new Consumer(this.RT);

	public ArrayList<Request> allRequest = new ArrayList<Request>();
	public MapInfoOrd mapInfoPointerOrd;
//	public MapInfoPri mapInfoPointerPri;
	public static ArrayList<Iterator> itrList = new ArrayList<Iterator>();
	//properties
	public boolean repOK(){
		if (this.RT == null) return false;
		if (this.reqProducer == null || this.reqConsumer == null) return false;
		System.out.println("class DispatchingSystem is OK");
		return true;
	}
	//构造操作
	public DispatchingSystem(){
		/*REQUEST:initialize the map. create the map and corresponding matrix
		 * MODIFIED: none
		 * EFFECT:  create the overall map class
		 * */
		this.mapInfoPointerOrd = new MapInfoOrd();
		//this.mapInfoPointerPri = new MapInfoPri("Hail_OO");
	} 
	//construction
	//观察操作（一个中间步骤）
	public int pos2ver(int raw, int col){
		/*REQUESTS: Position's raw and column number
		 * MODIFIED: none
		 * EFFECT: Position transformed into linear number of vertexes and return it
		 * */
		int ver = 0;
		ver = raw * 80 + col;
		return ver;
	}//position to vertex index
	//观察操作（中间步骤）
	public static int[] ver2pos(int ver){
		/*REQUESTS: vertex number
		 * MODIFIED: none
		 * EFFECT: vertex trancformed into Position's raw and column and return both 
		 * in an array
		 * */
		int []cr = new int[2];
		cr[0] = ver / 80;
		cr[1] = ver % 80;
		return cr;
	}//vertex to position
	//观察操作（勉强这么分吧）
	public ArrayList<Position> findShortestPath(Position a, Position b){
		/*REQUEST: two positions, a is from, b is to
		 * NODIFIED: none
		 * EFFECTS: find the shortest path, return a ArrayList<Position>.
		 * */
		ArrayList<Position> way = new ArrayList<Position>();
		short [][]cost = MapInfoOrd.vlink;
		short v = (short) pos2ver(a.raw, a.col);
		short n = (short) pos2ver(b.raw, b.col);
		short []dist = new short[6400];
		short [][]path = new short[6400][6400];
		
		short i = 0,w = 0,u = 0,count = 0;
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
	//观察操作（上一个方法的一步中间操作）
	public static ArrayList<Position> getSRPoint(short[] indexList, short index) {
		/*REQUEST: indexList and index
		 * MODIFIED: nothing
		 * EFFECT: using indexList and index to form the final arrayList for findShortestPath function
		 * */
		ArrayList<Position> p = new ArrayList<Position>();			
		for (int j = 0; j < indexList.length; j++) {
			int raw = ver2pos(indexList[j])[0];
			int col = ver2pos(indexList[j])[1];
			Position temp = new Position(raw, col);
			p.add(temp);
			if (indexList[j] == index)
				break;
		}
		return p;
	}
	//观察操作（寻找最短路径的中间步骤，仅在那个方法中运用，属于中间步骤）
	public short MINDIST(short []s, short [] dist){
		/*REQUEST: s array and dist array 
		 * MODIFIED: nothing
		 * EFFECT: using the s and dist to find a vertex u that hasn't found shortest path and 
		 * is the closest point to v. return the index
		 * one of Dijkstra algrithom's mediate function
		 * */
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
	}//minimum distance
	//观察操作（寻找最短路径的中间步骤，仅在那个方法中运用，属于中间步骤）
	public short SERCH_VER(short []s, short[]dist, short u){
		/*REQUEST: s, dist, u in the Dijkstra algorithm
		 * MODIFIED: none
		 * EFFECT: find a vertex 1. can be reached directly through u
		 * 2. hasn't found the shortest path
		 * if finded, return index
		 * else return -1;
		 * one of Dijkstra algrithom's mediate function
		 * */
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
	}
	//观察操作
	public int findSuitTaxi(Request req,Taxi1 []t){
		/*REQUESTS: the Request and all of the taxis
		 * MODIFIED: Request req's most suit taxi.
		 * EFFECT: if req's most suit taxi exists, add the corresponding taxi, and return 1
		 * otherwise, nothing will be added into Request req and return 0;
		 * */
		for (int i = -2; i <= 2; i++){
			for (int j = -2; j <= 2; j++){
				Position psgPos = new Position(req.origin.raw, req.origin.col);
				psgPos.nearBy(i, j);
				for (int k = 0; k < t.length; k++){
					Position txiPos = t[k].txLoc;
					if (txiPos.posEquals(psgPos) && !req.sbtTaxi.contains(k) && t[k].status == 3){
						req.sbtTaxi.add(k);//index of the nearby taxis
						t[k].compete();
					}//near by
				}
			}
		}//find nearby taxis
		for (int i = 0; i < req.sbtTaxi.size(); i++){
			Taxi1 tempTaxi = t[req.sbtTaxi.get(i)];
			if (req.mostSuitTaxi== null ){
				req.mostSuitTaxi = tempTaxi;
				continue;
			}//first time
			else{
				if (tempTaxi.credit > req.mostSuitTaxi.credit){
					req.mostSuitTaxi = tempTaxi;
					continue;
				}//higher credit
				else if(tempTaxi.credit == req.mostSuitTaxi.credit){
					int dstcOrigin = this.findShortestPath(req.mostSuitTaxi.txLoc, req.origin).size();//distance origin
					int dstcTemp = this.findShortestPath(tempTaxi.txLoc, req.origin).size();//distance of tempTaxi
					if (dstcOrigin > dstcTemp){
						req.mostSuitTaxi = tempTaxi;
						continue;
					}//compare the distance
				}
			} 
		}//shieve the taxis, find most suitable taxis.
		if (!(req.mostSuitTaxi == null)){
			return 1;
		}
		//anyhow, a most suitable taxi has found
		else
			return 0;
	}//find which one is the most suitable
	//important method
	//run操作（强行写一个）
	public void run(){
		ExecutorService exec1 = Executors.newCachedThreadPool(new HandlerThreadFactory());
		/*System.out.println("system initializing");
		boolean right = this.buildMap();*/
		int numOfTaxi = 100;
		int numOfOrdTaxi = 70;// number of ordinary taxi
		int numOfPriTaxi = 30;// number of priviledge taxi
		Taxi1 []taxis = new Taxi1[numOfTaxi];
		System.out.println("launching taxis");
		for (int i = 0; i < numOfTaxi; i++){
			synchronized (this)
			{	
				if (i < numOfOrdTaxi){
					taxis[i] = new Taxi1(i + 1/*, this.mapInfoPointer*/);
					exec1.execute(taxis[i]);
				}
				else{
					taxis[i] = new Taxi2(i + 1);
					exec1.execute(taxis[i]);
				}
			}
		}// let go the old drivers
		NewTest nwTst = new NewTest(taxis);
		System.out.println("ready to deal with requests");
		exec1.execute(this.reqProducer);
		exec1.execute(this.reqConsumer);
		int timeFlag = 0;// document whether it is 300ms
		//let go the requests
		while(true){
			synchronized(this){
				this.allRequest.addAll(this.reqConsumer.pushRequest());
				for (int i = 0; i < this.allRequest.size(); i++){
					if (this.allRequest.get(i).typeBit == 1){
						int st = findSuitTaxi(this.allRequest.get(i), taxis);//look for taxis
						this.allRequest.get(i).countDown();//count down, -0.1s
						if (this.allRequest.get(i).winTime <= 0){
							System.out.print("request: " + this.allRequest.get(i).reqString);
							if (this.allRequest.get(i).mostSuitTaxi == null){
								System.out.println(" is inconvenient for all taxis.");
							}//no taxi can deal with it*/
							else{
								Taxi1 t = taxis[this.allRequest.get(i).mostSuitTaxi.name - 1];
								System.out.println("is at taxi " + taxis[this.allRequest.get(i).mostSuitTaxi.name - 1].name + " " + taxis[this.allRequest.get(i).mostSuitTaxi.name - 1].plate + " 's service.");
								taxis[this.allRequest.get(i).mostSuitTaxi.name - 1].status = 2;
								taxis[this.allRequest.get(i).mostSuitTaxi.name - 1].decided(this.allRequest.get(i));
							}//change some property of relevant taxi
							this.allRequest.remove(i);
							i--;
						}
					}
					else if(this.allRequest.get(i).typeBit == 2){
						/*for (int k = 0; k < numOfTaxi; k++){
							if (taxis[k].status == 3)
								taxis[k].readInfo();
						}*/
						NewTest.seeAllStatus();
						this.allRequest.remove(i);
						i--;
					}
					else if (this.allRequest.get(i).typeBit == 3){
						taxis[this.allRequest.get(i).taxiRead - 1].readInfo();
						this.allRequest.remove(i);
						i--;
					}
					else if (this.allRequest.get(i).typeBit == 4){
						MapInfoOrd.mapChanging(this.allRequest.get(i).changeMap, this.allRequest.get(i).typeBit);
						this.allRequest.remove(i);
						i--;
					}
					else if (this.allRequest.get(i).typeBit == 5){
						MapInfoOrd.mapChanging(this.allRequest.get(i).changeMap, this.allRequest.get(i).typeBit);
						this.allRequest.remove(i);
						i--;
					}
					else if (this.allRequest.get(i).typeBit == 6){
						NewTest.addIterator(this.allRequest.get(i).changeMap);
						this.allRequest.remove(i);
						i--;
					}
				}
				try {
					/*MapInfo.computeFlow(100);*/
					MapInfoOrd.clearVehicleNum();
					sleep(1000 / 10);
					if (timeFlag == 2){
						timeFlag = 0;
						MapInfoOrd.reverseLight();
					}
					else 
						timeFlag++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}  
			}
		}
	}
	//main操作（强行发明了一种操作）
	public static void main(String []args){
		/*REQUEST: args
		 * MODIFIED: nothing
		 * EFFECT: begin all the thread
		 * */
		try {
			ExecutorService exec2 = Executors.newCachedThreadPool(new HandlerThreadFactory());
			/*MapInfo mi = new MapInfo();
			exec2.execute(mi);*/
			DispatchingSystem ds = new DispatchingSystem();
			exec2.execute(ds);
		} catch (Throwable e){
			System.out.println("you are fucking wrong");
		}
	}	
}