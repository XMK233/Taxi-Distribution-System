package Week12_Work10;
/*百分百继承，跟MapInfoPri除了名字变了,构造方法有些许区别一样之外完全一样，没有丝毫变化，所有的文档均相同，不再写了*/
public class MapInfoPri extends MapInfoOrd{
	//构造操作
	public MapInfoPri(String k){
		/*REQUEST: nothing
		 * MODIFIED: this map and vlink and crossMap and traffic lights
		 * EFFECT: initialize the maps and lingjie matrix
		 * */
		//MapInfoOrd.buildMap();
		//MapInfoOrd.buildVlink();
		//MapInfoOrd.buildCrossMap();
		for (int i = 0; i < 6400; i++){
			for (int j = 0; j < 6400; j++){
				vlink[i][j] = MapInfoOrd.vlink[i][j];
			}
		}
		for (int i = 0; i < 80; i++){
			for (int j = 0; j < 80; j++){
				map[i][j] = MapInfoOrd.map[i][j];
				crossMap[i][j] = MapInfoOrd.crossMap[i][j];
				trfLgtNS[i][j] = MapInfoOrd.trfLgtNS[i][j];
				trfLgtWE[i][j] = MapInfoOrd.trfLgtWE[i][j];
			}
		}//copy from MapInfoOrd to MapInfoPri. 
		//map, vlink, and crossmap can create respectively while traffic light must resemble, or traffic light system
		//will be in vain
		//MapInfoOrd.buildTrafficLight();
	}
}
