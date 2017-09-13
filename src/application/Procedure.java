//Keith Kenneally DNET2
package application;
import java.io.Serializable;
public class Procedure implements Serializable{

	private int procNo;
	private String procName;
	private double procCost;
	
	Procedure(String x, double y){
		this.procName = x;
		this.procCost = y;
		
	}
	
	public int getProcNo(){
		return procNo;
	}
	public void setProcNo(int num){
		this.procNo = num;
	}
	
	public String getProcName(){
		return procName;
	}
	public void setProcName(String n){
		this.procName= n;
	}
	
	public double getProcCost(){
		return procCost;
	}
	public void setProcCost(double c){
		this.procCost = c;
	}
	
	public String toString(){
		return ("Procedure Number:"+procNo+" || Type: "+procName+" || Procedure Cost: "+procCost+"\n");
		
	}
	public void print(){
		System.out.print(toString());
	}
	
}
