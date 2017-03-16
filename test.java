package Week12_Work10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



class Poly{
	// Rep ¡­
	public Iterator terms() {
		return new PolyGen(this);
	}
	// inner class
	//
	private static class PolyGen implements Iterator {
	       private Poly p; 	// the Poly being iterated
           private int n; 	// the next term to iterate
           
	       public PolyGen (Poly it){
			  //Requires: it !=null
			  p = it;
			  if(p.trms[0] == 0) 
				  n=1; 
			  else 
				  n= 0;
	       }
	   	public boolean hasNext() {
	   		return n<= p.deg;
	   	}
		public Object next () throws NSEE{
		       for(int e = n; e <= p.deg; e++) {
		            if (p.trms[e] != 0) {
				  	     n= e+1;
					     return new Integer(e);
		            }
		       }
		       throw new Exception(¡°Poly.terms¡±);
		}// end PolyGen
	}
}



class A extends Thread{

	public ArrayList<Position> Dijkstra(){
		ArrayList path = new ArrayList();
		
		return path;
	}
	
	public Integer name = 0;
	public A(int t){
		this.name = t;
	}
	public void getUpdate(Integer i){
		this.name = i;
	}
	public void run(){
		while(true){
			try {
				sleep(1000*1);
				Random rand = new Random();
				int index = rand.nextInt(4);
				System.out.println(this.name + " " + index);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
class B extends Thread{
	public Integer name = 1;
	public B(Integer i){
		name = i;
	}
	public void run(){
	while(true){
			try {
				sleep(1000 * 10);
				this.name++;
				System.out.println("B: " + this.name);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
}
class Test1{
	public int i = 250;
	public String nima = "nimabi!!!!!";
	public Test1(int k, String t){
		i = k;
		nima = t;
	}
	
}
class Test2 extends Test1{//
	public Test2(int k, String t) {
		super(k, t);
	}
	
}
public class test{
	public static int[][] buildMap(){
		int [][]map = new int[80][80];
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
				else return map;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("wrong map");
		} catch (IOException e) {
			System.out.println("wrong map");
		} finally{
			System.out.println("wrong map");
		}
		return map;
	}
	public static boolean validInput(String str){
		String newsc = str.replaceAll("\\s+", "");
		final String reqFormat = "^\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\).\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\)$";
		Pattern neg_x = Pattern.compile(reqFormat.trim());
    	Matcher neg_xn = neg_x.matcher(newsc.trim());
    	Pattern scan1 = Pattern.compile("(getAllFreeTaxis)|(seeStatusOfTaxi[0-9]+)");
    	Matcher scan1m = scan1.matcher(newsc.trim());
    	Pattern mapChange = Pattern.compile("changeMap\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\)[0-3]{0,1}");
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
    	else if (scan1m.find()) return true;
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
	}
	public static void main(String []args){
		Test1 []testSet = new Test1[10];
		for (int i = 0; i < 10; i++){
			if (i < 5){
				testSet[i] = new Test1(1, "jeje");
			}
			else{
				testSet[i] = new Test2(2, "haha");
			}
		}
		for (int i = 0; i < 10; i++){
			System.out.println(testSet[i].nima);
		}
		int [][]a = new int[2][2];
		int [][]b = new int[2][2];
		a[0][0] = 1; a[0][1] = 2; a[1][0] = 3; a[1][1] = 4;
		b = a.clone();
		System.out.println(a[0][0]);
		a[0][0] = 0;
		System.out.println(a[0][0] + " "  + b[0][0] + " " + b[0][1]);
		Random rand = new Random();
		boolean [][]k = new boolean[10][10];
		System.out.println("heiheihei" + k[5][5]);
//		String str = new Scanner(System.in).nextLine();
		for (int i = 0;i < 20; i++)
		System.out.println(rand.nextBoolean());
	}
}