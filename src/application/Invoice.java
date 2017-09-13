//Keith Kenneally DNET2
package application;
import java.time.LocalDate;
import java.util.*;
import java.io.Serializable;

public class Invoice implements Serializable{

	
	private int invoiceNo;
	private double invoiceAmt;
	private Date date;
	LocalDate localDate;
//	private String date;
	private boolean isPaid;
	int count = 0;
	
	ArrayList<Procedure> in_procList;
	ArrayList<Payment> in_paymentList;


	Invoice(){
		in_procList = new ArrayList<Procedure>();
		in_paymentList= new ArrayList<Payment>();
		count++;
	}
	
	public LocalDate getLd(){
		return localDate;
	}
	public void setLocalDate(LocalDate local){
		this.localDate = local;
	}
	public int getInvoiceNo(){
		return invoiceNo;
	}
	public void setInvoiceNo(int num){
		this.invoiceNo= num;
	}
	
	public double getInvoiceAmt(){
		return invoiceAmt;
	}
	public void setInvoiceAmt(double num){
		this.invoiceAmt= num;
	}
	
	public boolean getIsPaid(){
		return isPaid;
	}
	public void setIsPaid(boolean valid){
		this.isPaid= valid;
	}
	
	public String toString(){
		String proclist = Arrays.toString(this.in_procList.toArray()).replace("[", "").replace("]", "").replace(",", "");

		return ("Invoice(s):\nNumber: "+invoiceNo+"  ||  Amount Due: "+invoiceAmt+""
				+ "  ||  Date: "+this.localDate+"  ||  Is Paid? = "+isPaid+"\n"+proclist+"Payments Made: "+in_paymentList+"\n\n");
		//String list = Arrays.toString(customers.toArray()).replace("[", "").replace("]", "");
	}

	public void print(){
		System.out.print(toString());
	}
	
	
//This method takes in the Payment Number, then returns the position of that payment which will eventually be removed
	public int findPosition(int paymentNo){
		
		int indexPayment = -1;
		for (int index = 0; index < in_paymentList.size();index++){
			//System.out.println(p_invoiceList.get(x));
			Payment p = this.in_paymentList.get(index);
			if(paymentNo == p.getPaymentNo()){
				//System.out.print("Postion in list is: "+index);
				indexPayment = index;
			}
			
		}
		return indexPayment;

	}
//This method removes a payment from the arraylist, the parameter is the position/index of the unwanted payment
	public void removePayment(int delete){
	//Payment p = this.in_paymentList.remove(delete);
	if(delete >= 0){
		this.in_paymentList.remove(delete);
		System.out.println("Payment successfully removed: Updated payments: ");
	}
	else{
		System.out.println("Payment number does not exist");
	}
			
	
	printPayments();
//	System.out.println(in_paymentList.get(index));
	
}
//Method to print out all the payments
	public void printPayments(){
	//Payment p = this.in_paymentList.remove();
	System.out.println("All current payments: ");
		for (int index = 0; index < in_paymentList.size();index++){
			System.out.println(index+") "+in_paymentList.get(index)+"\n----------------------------------------------------------------------------------------------------------|");

		}

}
	
}
