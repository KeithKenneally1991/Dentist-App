package application;

public class Catch {

	public int isInt(String input){
		//String y = "";
		int x = 0;
		boolean isValid = true;
		
		try{
			x = Integer.parseInt(input);
			return x;
		}catch(NumberFormatException e){
			
			return -1;
		}
		
	}
	
	public double isDouble(String input){
		//String y = "";
		double x = 0;
		boolean isValid = true;
		
		try{
			x = Double.parseDouble(input);
			isValid = true;
		}catch(NumberFormatException e){
			isValid = false;
			
		}
		if(isValid){
			return x;
		}
		else{
			return -1;
		}
	}
	
	//test to see if a textfield was left empty
	public boolean emptyString(String str){
		
		
		if(str.isEmpty() || str.matches(".*\\d+.*")){
			return false;
		}
		else{
			return true;
		}
	}
	public void indexOutOfBounds (){
		
		try{
			
		}catch(IndexOutOfBoundsException e){
			
		}
	}
	
}

