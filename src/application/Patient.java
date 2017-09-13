//Keith Kenneally DNET2
package application;
import java.util.ArrayList;
import java.util.Arrays;

public class Patient extends Person implements Cloneable{

	private int patientNo ;
	private String password;
	//Invoice i = new Invoice();

	Dentist one = new Dentist(password);
	
	ArrayList<Invoice> p_invoiceList = new ArrayList<Invoice>();
	
	Patient( String name, String address){
		super(name, address);
		
	}
	Patient(int patientNo, String name, String address){
		super(name, address);
		
	}
	//Patient (int patientNo, String name){
		
	//}
	public int getPatientNo(){
		return patientNo;
	}
	public void setPatientNo(int num){
		this.patientNo= num;
	}
	
	public ArrayList<Invoice> getp_invoiceList(){
		return  p_invoiceList;
	}
	public void setp_invoiceList(ArrayList<Invoice> list){
		this.p_invoiceList= list;
	}
	
	
	public void set_invoice(Invoice new_In){
		this.p_invoiceList.add( new_In );
		
	}
	
	
	public String toString(){
		String list = Arrays.toString(this.p_invoiceList.toArray()).replace("[", "").replace("]", "").replace(",", "");
		return ("--------------------------------------------------------------------------------------"
				+ "--------------------|\nPatient Number: "+patientNo+""
				+ "  ||  Name: "+name+"  ||   Address: "+address+"  ||  Phone Number: "+contactNo+"\n---------"
		+ "-------------------------------------------------------------------------------------------------|\n"+list+""
						+ "----------------------------------------------------------------------------------------------------------|\n");

	}
	public void print(){
		System.out.print(toString());
	}
	
public String getDentistPassword(){
		
		return password;
	}
	
	public void setDentistPassword(String p){
		
		this.password = p;
	}
	
	//print list of invoices
	public void printInvoices(){
		for (int x = 0; x < p_invoiceList.size();x++){
			System.out.println(p_invoiceList.get(x));
		}
		}

	public void deletePatient(){
		
	}
	
	public int findPatient(ArrayList patients, int del){
		int found = 0;
		// loop through the array to find if the patient chosen to delete exists
		for(int index = 0; index < patients.size(); index++){
			
				if(((Patient) patients.get(index)).getPatientNo() == del){
					found = index;
					break;
				}	
				else{
						found = -1;
					}
	}
		return found;
}

}