package Week12_Work10;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewTest {
	public static Taxi1 taxis[] = new Taxi1[100];
	public NewTest(Taxi1 t[]){
		taxis = t;
	}
	public static void addIterator(String str){
		Pattern sc = Pattern.compile("iterateTaxi[0-9]{1,3}service[0-9]+");
    	Matcher sca = sc.matcher(str.trim());
    	if (sca.find()){
    		Pattern neg_3 = Pattern.compile("[0-9]+");
    		Matcher neg_3m = neg_3.matcher(str.trim());
    		int []temp = new int[2];
    		int j = 0;
    		while (neg_3m.find()){
    			temp[j] = Integer.parseInt(neg_3m.group());
    			j++;  
    		}
    		DispatchingSystem.itrList.add(((Taxi2)taxis[temp[0] - 1]).trackOfThisTaxi(temp[1] - 1));
    		System.out.println("iterator add valid");
    	}
	}
	public static void seeAllStatus(){
		for (int k = 0; k < 100; k++){
			if (taxis[k].status == 3)
				taxis[k].readInfo();
		}
	}
}
