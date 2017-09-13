//Keith Kenneally DNET2 - R00142850
package application;
//import java.sql.Date;

import java.util.*;
import java.io.Serializable;
import java.time.LocalDate;
public class Payment implements Serializable{

	private int paymentNo;
	double paymentAmount;
	LocalDate paymentDate;
	
	public int getPaymentNo(){
		return paymentNo;
	}
	public void setPaymentNo(int num){
		this.paymentNo = num;
	}
	
	public double getPaymentAmount(){
		return paymentAmount;
	}
	public void setPaymentAmount(double num){
		this.paymentAmount= num;
	}
	
	public LocalDate getPaymentDate(){
		return paymentDate;
	}
	public void setPaymentDate(LocalDate ld){
		this.paymentDate = ld;
	}
	public String toString(){
		return ("\nPayment number: "+paymentNo+"  ||  Payment Amount: "+paymentAmount+"  ||  Date Date: "+paymentDate);
		
	}
	public void print(){
	
		System.out.print(toString());
	}
	
	
}
