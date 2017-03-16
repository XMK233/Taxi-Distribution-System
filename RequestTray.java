package Week12_Work10;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo2.Customer;

class Request {
	/*Overview: a Request is a certain class representing the specific request, containing 
	 * some properties including original position and destination position and the taxi that
	 * should pick up this passenger and some flag and some less important properties*/
	/*表示对象:Position origin, destination; int typeBit, double winTime, Taxi1 mostSuittaxi, ArrayList<Taxi1>sbtTaxi
	 * 抽象函数:AF(c) = {origin, destination, typeBit, winTime, MStaxi}
	 * where MStaxi = mostSuitTaxi
	 * 不变式: origin != null &&
	 * destination != null && !destination.equals( origin )&&
	 * 1 <= typeBit <= 5 && 
	 * 0.0 <= winTime <= 3.0 &&
	 * mostSuitTaxi = null || sbtTaxi.contains(mostSuitTaxi) 
	 * */
	public Position origin = new Position();
	public Position destination = new Position();
	public int typeBit = 0;
	public double winTime = 3.0;
	public Taxi1 mostSuitTaxi = null;
	public ArrayList<Integer> sbtTaxi = new ArrayList<Integer>();
	public String reqString;
	public int taxiRead = 0;//for request type 4
	public String changeMap = null;
	public boolean repOK(){
		if (origin == null || destination == null || destination.posEquals(origin) || 
			typeBit > 5 || typeBit < 1 ||
			winTime > 3.0 || winTime < 0.0
				)
			return false;
		return true;
	}
	//构造操作
	public Request(String req){
		/*REQUEST: string
		 * MODIFIED: this requsest's corresponding property(shuxing)
		 * EFFECT: initialize this request according to the type of this string, type is defined 
		 * in zheng ze biao da shi
		 * */
		final String reqFormat = "\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\).\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\)";
		Pattern scan1 = Pattern.compile(reqFormat.trim());
    	Matcher scan1m = scan1.matcher(req.trim());
    	Pattern scan2 = Pattern.compile("getAllFreeTaxis");
    	Matcher scan2m = scan2.matcher(req.trim());
    	Pattern scan3 = Pattern.compile("seeStatusOfTaxi[0-9]+");
    	Matcher scan3m = scan3.matcher(req.trim());
    	Pattern scan4 = Pattern.compile("changeMap\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\)[0-3]{0,1}");
    	Matcher scan4m = scan4.matcher(req.trim());
    	Pattern scan5 = Pattern.compile("restoreDefaultMap");
    	Matcher scan5m = scan5.matcher(req.trim());
    	Pattern scan6 = Pattern.compile("iterateTaxi[0-9]{1,3}service[0-9]+");
    	Matcher scan6m = scan6.matcher(req.trim());
    	if (scan1m.find()){
    		this.typeBit = 1;//ordinary requests
			Pattern neg_3 = Pattern.compile("[0-9]+");
			Matcher neg_3m = neg_3.matcher(req);
			int []temp = new int[4];
			int j = 0;
			while (neg_3m.find()){
				temp[j] = Integer.parseInt(neg_3m.group());
				j++;  
			}
			this.origin = new Position(temp[0], temp[1]);
			this.destination = new Position(temp[2], temp[3]);
			this.reqString = req;
    	}
    	else if (scan2m.find()){
    		this.typeBit = 2;//see all of the taxis
    	}
    	else if (scan3m.find()){
    		Pattern neg_3 = Pattern.compile("[0-9]+");
			Matcher neg_3m = neg_3.matcher(req);
			if (neg_3m.find()) this.taxiRead = Integer.parseInt(neg_3m.group());
    		this.typeBit = 3;//see one specific taxi
    	}
    	else if (scan4m.find()){
    		/*Pattern neg_3 = Pattern.compile("[0-9]+");
			Matcher neg_3m = neg_3.matcher(req);
			int []temp = new int[3];
			int j = 0;
			while (neg_3m.find()){
				temp[j] = Integer.parseInt(neg_3m.group());
				j++;  
			}*/
			this.changeMap = req.trim();
			this.typeBit = 4;
    	}
    	else if (scan5m.find()){
    		this.changeMap = req.trim();
    		this.typeBit = 5;
    	}
    	else if (scan6m.find()){
    		this.changeMap = req.trim();
    		this.typeBit = 6;
    	}
		/*this.reqTime = (double)Math.round((System.currentTimeMillis())/100)/10;*/
	}
	//更新操作
	public synchronized void countDown(){
		/*REQUEST:none
		 * MODIFIED: time window symbol
		 * EFFECT: time window shrink by 0.1s
		 * */
		this.winTime -= 0.1;
	}
}
class Producer extends Thread{
	/*Overview: this is a class of producer model. it has a request tray and can 
	 * add request to request tray constantly
	 * */
	/*表示对象: RequestTray rt
	 * 抽象函数: AF(c) = {rt}
	 * 不变式: rt != null
	 * */
	private RequestTray rt;
	public boolean repOK(){
		if (this.rt == null) return false;
		return true;
	}
	//构造操作
	public Producer(RequestTray r){
		/*REQUEST: a requestTray thread
		 * MODIFIED: this requestTray
		 * EFFECT: attached to a RequestTray and set name
		 * */
		this.rt = r;
		this.setName("producer");
	}
	//run操作
	public void run(){
		while (!Thread.interrupted()) {  
            this.rt.inputRequest();
        }  
	}
}
//produce requests
class Consumer extends Thread{
	/*Overview: it contains a request tray and a array list to store requests.
	 * it can retrieve requests from request tray and return them out constantly 
	 * */
	/*表示对象: RequestTray rt
	 * 抽象函数: AF(c) = {rt}
	 * 不变式: rt != null
	 * */
	private RequestTray rt;
	private ArrayList<Request> req = new ArrayList<Request>();
	//更新/观察操作
	public  ArrayList<Request> pushRequest(){
		/*REQUEST: nothing
		 * MODIFIED: this req
		 * EFFECT: clone the request list and return it and after that clear it
		 * */
		ArrayList<Request> temp = (ArrayList<Request>) this.req.clone();
		this.req.clear();
		return temp;
	}
	public boolean repOK(){
		if (this.rt == null) return false;
		return true;
	}
	//构造操作
	public Consumer(RequestTray r){
		/*REQUEST: RequestTray Thread
		 * MODIFIED: this rt
		 * EFFECT: attached to a RequestTray and set name
		 * */
		this.rt = r;
		this.setName("consumer");
	}
	//run操作
	public void run(){
		while(!Thread.interrupted()){
			try {
				this.req.add(this.rt.retrieveRequest());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
//deal with requests
public class RequestTray extends Thread{
	/*Overview:it has a requests queue of BlockingQueue storing requests. the blocing queue can be added in
	 * and  taken out. 
	 * */
	/*表示对象: BlockingQueue requests
	 * 抽象函数: AF(c) = {requests}
	 * 不变式: requests == null || requests != null(讲道理这个真没什么好写的)
	 * */
	public BlockingQueue requests = new ArrayBlockingQueue(400);
	//properties
	public boolean repOK(){
		return true;//有什么好说的吗？就算request数组是空的也不是错的，含有东西也不是错。。
	}
	//构造操作
	public RequestTray(){
		/*REQUEST:nothing 
		 * MODIFIED: this block queue 
		 * EFFECT: this requests cleared and set name
		 * */
		requests.clear();
		this.setName("request tray");
	}
	//construction
	//观察操作
	public static boolean validInput(String str){
		/*REQUEST: a string
		 * MODIFIED: nothing
		 * EFFECT: return if str is valid request sentence. If it is one of the type, 
		 * if the numbers is within range
		 * */
		String newsc = str.replaceAll("\\s+", "");
		final String reqFormat = "^\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\).\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\)$";
		Pattern neg_x = Pattern.compile(reqFormat.trim());
    	Matcher neg_xn = neg_x.matcher(newsc.trim());
    	Pattern scan1 = Pattern.compile("(getAllFreeTaxis)|(seeStatusOfTaxi[0-9]+)|(restoreDefaultMap)|(iterateTaxi[0-9]{1,3}service[0-9]+)");
    	Matcher scan1m = scan1.matcher(newsc.trim());
    	Pattern mapChange = Pattern.compile("changeMap\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\)[0-3]{0,1}");//^^^^^^^^^^^^^^^^^^
    	Matcher mapChangeMatch = mapChange.matcher(newsc.trim());
    	if (neg_xn.find()){
        	Pattern neg_3 = Pattern.compile("[0-9]+");
    		Matcher neg_3m = neg_3.matcher(newsc.trim());
    		int []temp = new int[4];
    		int j = 0;
    		while (neg_3m.find()){
    			temp[j] = Integer.parseInt(neg_3m.group());
    			j++;  
    		}
    		if (temp[0] == temp[2] && temp[1] == temp[3])
    			return false;// if the destiny is exactly right here
    		for (j = 0; j < 4; j++){
    			if (temp[j] < 0 || temp[j] > 79)
    				return false;
    		}//if one coordinate is out of the map, false
    		return true;
    	}
    	else if (scan1m.find()) {
    		Pattern m = Pattern.compile("iterateTaxi[0-9]{1,3}service[0-9]+");
    		Matcher mm = m.matcher(newsc.trim());
    		if (mm.find()){
    			Pattern neg_3 = Pattern.compile("[0-9]+");
	    		Matcher neg_3m = neg_3.matcher(newsc.trim());
	    		int []temp = new int[2];
	    		int j = 0;
	    		while (neg_3m.find()){
	    			temp[j] = Integer.parseInt(neg_3m.group());
	    			j++;  
	    		}
	    		if (temp[0] < 71 || temp[0] > 100 ||
	    			temp[1] < 0){
	    			return false;
	    		}
    		}
    		
    		return true;
    	}
    	else if (mapChangeMatch.find()){
    		Pattern neg_3 = Pattern.compile("[0-9]+");
    		Matcher neg_3m = neg_3.matcher(newsc.trim());
    		int []temp = new int[4];
    		int j = 0;
    		while (neg_3m.find()){
    			temp[j] = Integer.parseInt(neg_3m.group());
    			j++;  
    		}
    		if ((temp[0] < 0 || temp[0] > 79) || 
    			(temp[1] < 0 || temp[1] > 79) || 
    			(temp[2] < 0 || temp[2] > 3))
    			return false;
    		return true;
    	}
    	else return false;
	}//if the request is valid?
	//function methods
	//更新操作
	public void inputRequest(){
		/*REQUEST: nothing
		 * MODIFIED: this. requests
		 * EFFECT: add some elements in this request block queue for others class's use 
		 * if the request is right
		 * */
		//System.out.print("please input request(where is the passenger and where is he going): ");
		String temp = new Scanner(System.in).nextLine();
		String newsc = temp.replaceAll("\\s+", "");
		if (validInput(newsc)){
			Request r = new Request(newsc);
			this.requests.add(r);
		}
		else{
			System.out.println("this request is invalid.");
		}
	}
	//更新操作（使得这个队列的元素数目发生了变化）
	public Request retrieveRequest() throws InterruptedException{
		/*REQUEST: nothing 
		 * MODIFIED: this reqeuest block queue
		 * EFFECT: retrieve and return one of the requests in the block queue
		 * */
		Request r = (Request) this.requests.take();
		return r;
	}
	//methods
}

// add some requests in the initialization status
/*requests.add(new Request("(12,15).(17,18)"));
requests.add(new Request("(47,68).(0,49)"));
requests.add(new Request("(72,16).(43,20)"));*/