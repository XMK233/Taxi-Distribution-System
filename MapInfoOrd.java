package Week12_Work10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapInfoOrd extends Thread{
	/*Overview: it has nearly all the subjective environment of the real traffic condition.
	 * such as the city map, cross condition map and traffic light table, and lingjie(邻接) matrix
	 * and a table calculating the vehicle number on each way.
	 * maps and matrixes should be generated or changed and 
	 * some information is supposed to be counted, so here has some methods.
	 * */
	/*表示对象: int [][]map, crossMap, trfLgt(2 of it), vlink(lingjie matrix), vehicleNumTable
	 * 抽象函数: AF(c) = {map, crossMap, trafficLights, vlink, vehicleNumTable}
	 * where trafficLight = {this.trfLgtNS, this.trfLgtWE}
	 * 不变式: 0 <= i, j <= 79 && 0<=map[i][j]<=3 && 0<=trafficLights[i][j]<=2 && 0<=crossMap<=1 &&
	 * 0 <= i, j <= 6399 && (vlink[i][j] == 0 || vlink[i][j] == 1 || vlink[i][j] == 32767 )   
	 * */
	//properties
	public static int [][]map = new int[80][80];
	public static int [][]stcMap = new int[80][80];
	public static int [][]crossMap = new int[80][80];
	public static int[][] trfLgtNS = new int [80][80];//traffic light north and south
	public static int[][] trfLgtWE = new int [80][80];//traffic light east and west
	public static short [][]vlink = new short[6400][6400];
	public static short [][]stcVlink = new short[6400][6400];
	public static short [][]vehicleNumTable = new short[6400][6400];
	//public static double[][] flowTable = new double[6400][6400];
	private static int flowFlag = 0;//current(liuliang) flag, add up by 50, to 100, clear it.
	private static int mapChangeTimes = 0;
	//construction
	public boolean repOK(){
		for(int i = 0; i < 80; i++) {
			for (int j = 0;j < 80; j++){
				if (map[i][j] < 0 || map[i][j] > 3 ||
					trfLgtNS[i][j] < 0 || trfLgtNS[i][j] > 2 ||
					trfLgtWE[i][j] < 0 || trfLgtWE[i][j] > 2 ||
					crossMap[i][j] < 0 || crossMap[i][j] > 1
					){
					return false;}
			}
		}
		for (int i = 0; i < 6400; i++){
			for (int j = 0; j < 6400; j++){
				if (!(vlink[i][j] == 0 || vlink[i][j] == 1 || vlink[i][j] == 32767 )||
					vehicleNumTable[i][j] < 0){
					return false;}
			}
		}
		return true;
	}
	//构造操作
	public MapInfoOrd(int k){
		;
	}
	public MapInfoOrd(){
		/*REQUEST: nothing
		 * MODIFIED: this map and vlink and crossMap and traffic lights
		 * EFFECT: initialize the maps and lingjie matrix
		 * */
		MapInfoOrd.buildMap();
		MapInfoOrd.buildVlink();
		MapInfoOrd.buildCrossMap();
		MapInfoOrd.buildTrafficLight();
		for (int i = 0; i < 80; i++){
			for (int j = 0; j < 80; j++){
				stcMap[i][j] = map[i][j];
			}
		}
		for (int i = 0; i < 6400; i++){
			for (int j = 0; j < 6400; j++){
				stcVlink[i][j] = vlink[i][j];
			}
		}
	}
	//function methods
	//更新操作（应该是酱紫吧，毕竟修改了属性）
	public synchronized static boolean buildCrossMap(){
		/*REQUESTS: nothing explicit, but a map txt file
		 * MODIFIED: map
		 * EFFECT: copy the content from txt file to map, return true
		 * or false if the map is or not invalid
		 * */
		File file = new File ("");
		File m = new File (file.getAbsolutePath() + "\\mapCross.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(m));
			String tempLine = null;
			int i = 0;
			while(i < 80 && (tempLine = reader.readLine()) != null){
				tempLine = tempLine.replaceAll("\\s+", "");
				Pattern neg_3 = Pattern.compile("([0-1],){79}([0-1]{1})");
				Matcher neg_3m = neg_3.matcher(tempLine);
				if (neg_3m.find()){
					String []t = tempLine.split(",");
					for (int j = 0; j < 80 && j < t.length; j++){
						crossMap[i][j] = Integer.parseInt(t[j]);
					}
					i++;
				}
				else {
					System.out.println("Wrong map !!!! Vice city~~~~"); 
					reader.close();
					return false;
				}
			}
			if (i != 80) {
				System.out.println("Wrong map !!!! Vice city~~~~"); 
				reader.close();
				return false;
			}
			else{
				System.out.println("cross information load complete");
				reader.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			System.out.println("wrong map");
		} catch (IOException e) {
			System.out.println("wrong map");
		} 
		System.out.println("you shouldn't see this sentence.");
		return false;
	}//get the cross map from certain .txt
	//更新操作（同上）
	public synchronized static void buildTrafficLight(){
		/*REQUEST: txt 
		 * MODIFIED: this two trfLgt
		 * EFFECT: build up the trfLgt
		 * */
		for (int i = 0 ; i < 80; i++){// the row
			for (int j = 0; j < 80; j++){// the column
				if (crossMap[i][j] == 1){// plain cross
					int k = 0;
					if (((i - 1) >= 0) && (map[i - 1][j] == 2 || map[i - 1][j] == 3)) k++;
					if (((j - 1) >= 0) && (map[i][j - 1] == 1 || map[i][j - 1] == 3)) k++;
					if (((i) < 80) && (map[i][j] == 1 || map[i][j] == 2)) k++;
					else if (((i) < 80) && (map[i][j] == 3)) k += 2;
					//lingjie points
					Random rand = new Random();
					if (k >= 3){
						trfLgtNS[i][j] = rand.nextInt(2) + 1;// 1 is green, 2 is red
						trfLgtWE[i][j] = 3 - trfLgtNS[i][j];// WE is always the reverse of NS
					}
				}
								
			}
		}
		System.out.println("traffic lights lit");
	}
	//更新操作
	public synchronized static void buildVlink(){
		/*REQEUSTS: map
		 * MODIFIED: buildVlink
		 * EFFECTS: create lingjie matrix according to map
		 * */
		for (int i = 0; i < 6400; i++){
			for (int j = 0; j < 6400; j++){
				if (i != j)
					vlink[i][j] = Short.MAX_VALUE;
				else
					vlink[i][j] = 0;
			}
		}
		for(int i = 0; i < 80; i++){
			for (int j = 0; j < 80; j++){
				short a = (short) pos2ver(i, j);
				short b = (short)pos2ver(i, j + 1);
				short c = (short)pos2ver(i + 1, j);
				if (map[i][j] == 0) ;
				else if (map[i][j] == 1) vlink[a][b] = vlink[b][a] = 1;
				else if (map[i][j] == 2) vlink[a][c] = vlink[c][a] = 1;
				else if (map[i][j] == 3){
					vlink[a][b] = vlink[b][a] = 1;
					vlink[a][c] = vlink[c][a] = 1;
				}
			}
		}
	}// vertex table
	//更新操作
	public synchronized static boolean buildMap(){
		/*REQUESTS: nothing explicit, but a map txt file
		 * MODIFIED: map
		 * EFFECT: copy the content from txt file to map, return true
		 * or false if the map is or not invalid
		 * */
		File file = new File ("");
		File m = new File (file.getAbsolutePath() + "\\mapPrint.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(m));
			String tempLine = null;
			int i = 0;
			while(i < 80 && (tempLine = reader.readLine()) != null){
				tempLine = tempLine.replaceAll("\\s+", "");
				Pattern neg_3 = Pattern.compile("([0-3],){79}([0-3]{1})");
				Matcher neg_3m = neg_3.matcher(tempLine);
				if (neg_3m.find()){
					String []t = tempLine.split(",");
					for (int j = 0; j < 80 && j < t.length; j++){
						map[i][j] = Integer.parseInt(t[j]);
					}
					i++;
				}
				else {
					System.out.println("Wrong map !!!! Vice city~~~~"); 
					reader.close();
					return false;
				}
			}
			if (i != 80) {
				System.out.println("Wrong map !!!! Vice city~~~~"); 
				reader.close();
				return false;
			}
			else{
				System.out.println("Welcome to Gothom");
				reader.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			System.out.println("wrong map");
		} catch (IOException e) {
			System.out.println("wrong map");
		} 
		System.out.println("you shouldn't see this sentence.");
		return false;
	}//get the map from certain .txt
	//观察操作（一个中间步骤，用于其他的方法中，貌似不属于任何一种操作分类，所以强行分类）
	public static int pos2ver(int raw, int col){
		/*REQUESTS: Position's raw and column number
		 * MODIFIED: none
		 * EFFECT: Position transformed into linear number of vertexes and return
		 * */
		int ver = 0;
		ver = raw * 80 + col;
		return ver;
	}//position to vertex index
	//观察操作（同上，属于中间步骤）
	public static int[] ver2pos(int ver){
		/*REQUESTS: vertex number
		 * MODIFIED: none
		 * EFFECT: vertex transformed into Position's raw and column and return
		 * */
		int []cr = new int[2];
		cr[0] = ver / 80;
		cr[1] = ver % 80;
		return cr;
	}//vertex to position
	//更新操作
	public static synchronized void addVehicleNum(Position ori, Position des){
		/*REQUEST:origin position and destination position
		 * MODIFIED:vehicleNumTable
		 * EFFECT:corresponding register in vehicle numbers table will change.
		 * */
		int verOri = pos2ver(ori.getRaw(), ori.getCol());
		int verDes = pos2ver(des.getRaw(), des.getCol());
		vehicleNumTable[verOri][verDes]++;
	}
	//更新操作
	public static synchronized void clearVehicleNum(){
		/*REQUESTS: NONe
		 * MODIFIED: not explicit, but vehicleNumTable
		 * EFFECT: clear the vehicleNnumTable
		 * */
		vehicleNumTable = new short[6400][6400];
	}
	//更新操作
	public static synchronized void mapChanging(String str, int type){
		/*REQEUST:string, representing the command; type of che command
		 * MODIFIED:nothing explicit, but map and vlink and mapChangeTime
		 * EFFECT:change map and vlink and mapChangeTime if type is "changeMap(```)```" while change time is less than 5
		 * restore the map and vlink if type is "restoreDefaultMap"
		 * */
		if (type == 4){
			if (mapChangeTimes < 5){
				Pattern neg_3 = Pattern.compile("[0-9]+");
				Matcher neg_3m = neg_3.matcher(str);
				int []temp = new int[3];
				int j = 0;
				while (neg_3m.find()){
					temp[j] = Integer.parseInt(neg_3m.group());
					j++;  
				}
				if (map[temp[0]][temp[1]] != temp[2]){
					map[temp[0]][temp[1]] = temp[2];
					buildVlink();
					mapChangeTimes++;
					System.out.println("(" + temp[0] + ", " + temp[1] + ")" 
									  + "change to " + temp[2]);
				}
				else{
					System.out.println("(" + temp[0] + ", " + temp[1] + ")" 
							  		  + "no need to change");
				}
			}
			else {
				System.out.println(str + " request has been refused");
			}
		}
		else if (type == 5){
			buildMap();
			buildVlink();
			mapChangeTimes = 0;
			System.out.println("map has been restored");
		}
		
	}
	//更新操作
	public synchronized static void reverseLight(){
		for (int i = 0; i < 80; i++){
			for (int j = 0; j < 80; j++){
				if (trfLgtNS[i][j] != 0 && trfLgtWE[i][j] != 0){
					int temp = 0;
					temp = trfLgtWE[i][j];
					trfLgtWE[i][j] = trfLgtNS[i][j];
					trfLgtNS[i][j] = temp;
				}
			}
		}
	}
}