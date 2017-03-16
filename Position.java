package Week12_Work10;

public class Position {
	/*Overview: it has two most important properties: the column and row of the 
	 * position. it contains some methods that can change the value of properties.
	 * */
	/*表示对象：int raw, int col
	 * 抽象函数:AF(c) = (raw, col)
	 * 不变式: 79>= raw >= 0 && 0 <= col <= 79 
	 * */
	public int raw;
	public int col;
	//观察操作，下一个也是
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
	//构造操作，下一个同。
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
	//更新操作
	public void nearBy(int a, int b){
		/*REQUEST: int a and b
		 * MODIFIED: this raw and col
		 * EFFECT: move (raw,col) to (a,b)
		 * */
		this.raw += a;
		this.col += b;
	}
	//观察操作
	public boolean posEquals( Position a){
		/*REQUEST: Position a
		 * MODIFIED: nothing
		 * EFFECT: return true if Position a have the same col and raw with this Position
		 * */
		if ((this.raw == a.raw) && (this.col == a.col)) return true;
		else return false;
	}
	//更新操作
	public void getCopy(Position a){
		/*REQUEST: Position a 
		 * MODIFIED: this raw and col
		 * EFFECT: change this raw and col into a's corresponding attributes
		 * */
		this.raw = a.raw;
		this.col = a.col;
	}
	//生成操作（其实我也不是很清楚这个算是生成还是观察，但是这个的确生成了一个和这个类密切相关的字符串，姑且这样算吧）
	public String toString(){
		/*REQUEST: nothing
		 * MODIFIED: nothing
		 * EFFECT: return this Request as a form of string 
		 * */
		return "(" + String.valueOf(this.raw) + ", " + String.valueOf(this.col) + ")";
	}
	//更新操作
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
