package Week12_Work10;
/*�ٷְټ̳У���MapInfoPri�������ֱ���,���췽����Щ������һ��֮����ȫһ����û��˿���仯�����е��ĵ�����ͬ������д��*/
public class MapInfoPri extends MapInfoOrd{
	//�������
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
