package Week12_Work10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
class dijkstra{
	public int [][]map = new int[80][80];
	public short [][]vlink = new short[6400][6400];
	public void buildVlink(){
		for(int i = 0; i < 80; i++){
			for (int j = 0; j < 80; j++){
				short a = (short) this.pos2ver(i, j);
				short b = (short)this.pos2ver(i, j + 1);
				short c = (short)this.pos2ver(i + 1, j);
				if (map[i][j] == 0) ;
				else if (map[i][j] == 1) this.vlink[a][b] = this.vlink[b][a] = 1;
				else if (map[i][j] == 2) this.vlink[a][c] = this.vlink[c][a] = 1;
				else if (map[i][j] == 3){
					this.vlink[a][b] = this.vlink[b][a] = 1;
					this.vlink[a][c] = this.vlink[c][a] = 1;
				}
			}
		}
	}// vertex table
	public boolean buildMap(){
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
						this.map[i][j] = Integer.parseInt(t[j]);
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
				System.out.println("map is right in format!!!! Nice city~~~~");
				reader.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			System.out.println("wrong map");
		} catch (IOException e) {
			System.out.println("wrong map");
		} /*finally{
			System.out.println("wrong map");
		}*/
		System.out.println("you shouldn't see this sentence.");
		return false;
	}//get the map from certain .txt
	public int pos2ver(int raw, int col){
		int ver = 0;
		ver = raw * 80 + col;
		return ver;
	}//position to vertex index
	public static int[] ver2pos(int ver){
		int []cr = new int[2];
		cr[0] = ver / 80;
		cr[1] = ver % 80;
		return cr;
	}//vertex to position
	public ArrayList<Position> findShortestPath(Position a, Position b){
		ArrayList<Position> way = new ArrayList<Position>();
		short [][]cost = this.vlink;
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
		/*for (i = 0; i < path[n].length; i++){//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
			int raw = ver2pos(path[n][i])[0];
			int col = ver2pos(path[n][i])[1];
			way.add(new Position(raw, col));
		}//add all the path to the way*/
		return getSRPoint(path[n], n);
	}//find the shortest way^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	public static ArrayList<Position> getSRPoint(short[] indexList, short index) {
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
	public short MINDIST(short []s, short [] dist){
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
	public short SERCH_VER(short []s, short[]dist, short u){
		for (int i = 0; i < s.length; i++){
			if (s[i] == 0){
				short[]temp = this.vlink[u];
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
	public dijkstra(){
		this.buildMap();
		for (int i = 0; i < 6400; i++){
			for (int j = 0; j < 6400; j++){
				if (i != j)
					this.vlink[i][j] = Short.MAX_VALUE;
				else
					this.vlink[i][j] = 0;
			}
		}
		this.buildVlink();   
	}//(14,18).(17,49)
	public static void main(String [] args){
		dijkstra dj = new dijkstra();
		ArrayList<Position> a = dj.findShortestPath(new Position(14, 18), new Position(17, 49));
		System.out.println(a.size());
		for(int i = 0; i < a.size(); i++){
			System.out.println(a.get(i));
		}
	}
}