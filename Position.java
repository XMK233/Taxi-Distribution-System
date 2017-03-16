package Week12_Work10;

public class Position {
	/*Overview: it has two most important properties: the column and row of the 
	 * position. it contains some methods that can change the value of properties.
	 * */
	/*��ʾ����int raw, int col
	 * ������:AF(c) = (raw, col)
	 * ����ʽ: 79>= raw >= 0 && 0 <= col <= 79 
	 * */
	public int raw;
	public int col;
	//�۲��������һ��Ҳ��
	public int getRaw(){
		/*REQUEST:nothing 
		 * MODIFIED: nothing
		 * EFFECT: return the raw
		 * */
		return this.raw;
	}
	public int getCol(){
		/*REQUEST:nothing 
		 * MODIFIED: nothing
		 * EFFECT: return the col
		 * */
		return this.col;
	}
	//�����������һ��ͬ��
	public Position(){
		/*REQUEST: nothing
		 * MODIFIED: this raw and col
		 * EFFECT: initialize it to the beginning point
		 * */
		this.raw = 0;
		this.col = 0;
	}
	public boolean repOK(){
		if (this.raw > 79 || this.col < 0)  return false;
		return true;
	}
	public Position(int a, int b){
		/*REQUEST: int a and b
		 * MODIFIED: this raw and col
		 * EFFECT: raw and col set to a and b respectively
		 * */
		this.raw = a;
		this.col = b;
	}
	//���²���
	public void nearBy(int a, int b){
		/*REQUEST: int a and b
		 * MODIFIED: this raw and col
		 * EFFECT: move (raw,col) to (a,b)
		 * */
		this.raw += a;
		this.col += b;
	}
	//�۲����
	public boolean posEquals( Position a){
		/*REQUEST: Position a
		 * MODIFIED: nothing
		 * EFFECT: return true if Position a have the same col and raw with this Position
		 * */
		if ((this.raw == a.raw) && (this.col == a.col)) return true;
		else return false;
	}
	//���²���
	public void getCopy(Position a){
		/*REQUEST: Position a 
		 * MODIFIED: this raw and col
		 * EFFECT: change this raw and col into a's corresponding attributes
		 * */
		this.raw = a.raw;
		this.col = a.col;
	}
	//���ɲ�������ʵ��Ҳ���Ǻ��������������ɻ��ǹ۲죬���������ȷ������һ���������������ص��ַ���������������ɣ�
	public String toString(){
		/*REQUEST: nothing
		 * MODIFIED: nothing
		 * EFFECT: return this Request as a form of string 
		 * */
		return "(" + String.valueOf(this.raw) + ", " + String.valueOf(this.col) + ")";
	}
	//���²���
	public void hypothesicalMove(int n){
		/*REQUEST: direction information;
		 * 1:up 2:down 3:left 4:right
		 * MODIFIED:this raw and col
		 * EFFECT:this position moved, according to the input direction
		 * */
		if (n == 1){
			this.raw -= 1;
		}
		else if (n == 2){
			this.raw++;
		}
		else if (n == 3){
			this.col--;
		}
		else if (n == 4){
			this.col++;
		}
	}
}
