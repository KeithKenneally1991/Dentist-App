package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseDentist {

	
	private static String dbURL = "jdbc:derby:C:\\Users\\wwwke\\MyDbTest;create=true;";
	private static Connection conn = null;
    private static Statement stmt = null;
    
    public static String getInvoiceData(int pid){
    	
    	String str = "";
		 try {
			 
			 createConnection();
			 stmt = conn.createStatement();
			 ResultSet results = stmt.executeQuery("select invId, invAmount, invDate, paidStatus from invoice where pid = " + pid);
			 ResultSetMetaData rsmd = results.getMetaData();
			 int numberCols = rsmd.getColumnCount();
			
			 for (int i=1; i<=numberCols; i++) {
	                //print Column Names
				 str+= rsmd.getColumnLabel(i) + "\t\t\t\t";
			 }
			 
			 while(results.next()) {
				 int invid = results.getInt(1);
				 double amount = results.getDouble(2);
				 String invDate = results.getString(3);
				 boolean ispaid = results.getBoolean(4);
				 str += "\n" + invid + "\t\t\t\t" + amount + "\t\t\t\t\t" + invDate + "\t\t\t\t\t"+ ispaid +"\n";
	            }

	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
		 shutdown();
		 return str;
    	
    	
    	//return "myinvoice..";
    }
    // this method finds the maximum id of an invoice for a specific patient
    // this id can then be used for adding new invoices, just by incrementing the id by 1.. e.g new invoices id
    public static int getMaxInvId(int pid) throws SQLException{
    	 createConnection();
		 String tableName = "invoice";
		 PreparedStatement maxInv = null; 
		 maxInv=   conn.prepareStatement( "SELECT COALESCE(MAX(invId), 0) FROM " + tableName + " where pid = "+pid);
		 ResultSet resultSet = null;
		 resultSet = maxInv.executeQuery();
		 
		int x = -1;	
		if (resultSet.next()) {
			  x = resultSet.getInt(1);
			}
		shutdown();
		 return x;
    }
    public static void addInvoice(int pid, double invAmount, LocalDate invDate, boolean paidStatus){
    	 createConnection();		
    	
    	 try {
 			String tableName = "invoice";
 			stmt = conn.createStatement();
 			String sql = "insert into " + tableName + " (pid, invAmount, invDate, paidStatus) values ("+pid +", " + invAmount +", '"+invDate+"', " + "'"+paidStatus+"')";
 			System.out.println(sql);
 			stmt.execute(sql);
 			stmt.close();
 			System.out.println("Insert ok");
 	       }
 		catch (SQLException sqlExcept) {
 			sqlExcept.printStackTrace();
 			}
 		catch(NullPointerException c){
 		System.out.println("Error caught here");
 		}
 		 shutdown();

    }
    // this is where patient listviews get the information from, this method returns an arraylist of Strings conataining patient id, name
    // this method retrieves names and ids which displays them in a list so the user can chose specific patients
    public  ArrayList getPatientListView(){
    	createConnection();
    	ArrayList<String> results = null;
    	PreparedStatement selectAllPeople = null; 
        ResultSet resultSet = null;
        try 
        {
            selectAllPeople =  conn.prepareStatement( "SELECT pid, name FROM patient" );
           // executeQuery returns ResultSet containing matching entries
           resultSet = selectAllPeople.executeQuery(); 
           results = new ArrayList< String >();
           while ( resultSet.next() )
           {
              results.add((new String(
            		  resultSet.getInt("pid") + " \t " +
            		  resultSet.getString("name"))
            		));
           } // end while
        } // end try
        catch ( SQLException sqlException )
        {
           sqlException.printStackTrace();         
        } // end catch
        finally
        {
           try 
           {
              resultSet.close();
           } // end try
           catch ( SQLException sqlException )
           {
              sqlException.printStackTrace();         
              try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           } // end catch
        } // end finally
    
        shutdown();
        return results;
    }
    
    public ArrayList getInvoiceList(){
    	createConnection();
    	ArrayList<String> results = null;
    	PreparedStatement selectAllPeople = null;  
        ResultSet resultSet = null;
        System.out.println("here "+selectAllPeople);
        try 
        {
            selectAllPeople = 
               conn.prepareStatement( "SELECT invId, name FROM patient p, invoice i where p.pid = i.pid" );
            System.out.println("here "+selectAllPeople);
           resultSet = selectAllPeople.executeQuery(); 
           results = new ArrayList< String >();
           while ( resultSet.next() )
           {
              results.add((new String(
            		  resultSet.getInt("invId") + " \t " +
            		  resultSet.getString("name"))
            		));//  res
           } // end while
        } // end try
        catch ( SQLException sqlException )
        {
           sqlException.printStackTrace();         
        } // end catch
        finally
        {
           try 
           {
              resultSet.close();
           } // end try
           catch ( SQLException sqlException )
           {
              sqlException.printStackTrace();         
              try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
           } // end catch
        } // end finally
    
        shutdown();
        return results;
    }
    
    // this method is for the  display patient tab. it searches for a patient by using either their id or their name
    // this method allows the user to find a patient by either name or pid
    public static String findPatient(String name, int id) throws SQLException{
    	createConnection();		
    	 String str = "";		
    	 // 2 querys.. one for searching by name the other searching by pid
    	 String sql = "select * from patient where pid = "+ id;
    	 String sql2 = "select * from patient where name = '" + name +"'";
    	 
		 stmt = conn.createStatement();
		  ResultSet results = null;
		  ResultSet r2 = null;
		  ResultSetMetaData rsmd = null;
		  ResultSetMetaData rsmd2 = null;

		 if (id == -1){ // the user entered a name to search
			 		 results = stmt.executeQuery(sql2);	 
			 		 rsmd = results.getMetaData();
					 System.out.println("Made it here for name");
		 }
		 else{ // the user entered a patient id to search
			 r2 = stmt.executeQuery(sql);
			 rsmd2 = r2.getMetaData();
		 }
	int numberCols = 0;
	int numberCols2 = 0;
		 try{
			  numberCols = rsmd.getColumnCount();
			  numberCols2 = rsmd2.getColumnCount();
		 }catch(NullPointerException p){
			 
		 }
		 
		  if (id == -1){ // the user has searched by name..
			  for (int i=1; i<=numberCols; i++) {
	                //print Column Names
				 str+= rsmd.getColumnLabel(i) + "\t\t\t";
			 }
			
			 try{
			 while(results.next()) {
				 
				 int pid2 = results.getInt(1);
				 String n2= results.getString(2);
				 String address2 = results.getString(3);
				 String contact2 = results.getString(4);
				 str += "\n" + pid2 + "\t\t\t" + n2 + "\t\t\t\t" + address2 + "\t\t\t\t"+ contact2 +"\n";	
				 str+="\n-----------------------------------------------------------------------------------------\n";
				 str+= findInvoiceByName(name);
				 str+="\n-----------------------------------------------------------------------------------------\n";
				 str+=findProceduresByNameOrId(name,-1);
				 str+="\n-----------------------------------------------------------------------------------------\n";
				 str+=findPaymentsByNameOrId(name, -1);
				 str+="\n";

	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        } 
		 }
		 
		  else{ // do the same thing except for a search by id instead of name
			 
			  for (int x=1; x<=numberCols2; x++) {
				 str+= rsmd2.getColumnLabel(x) + "\t\t\t";
			 }
			 
			 try{
			 while(r2.next()) {
				 System.out.println("in the second loop");

				 int pid = r2.getInt(1);
				 String n= r2.getString(2);
				 String address = r2.getString(3);
				 String contact = r2.getString(4);
				 str += "\n" + pid + "\t\t\t" + n + "\t\t\t\t" + address + "\t\t\t\t"+ contact +"\n";
				 str+="\n-----------------------------------------------------------------------------------------\n";
				 str+= findInvoiceById(id);
				 str+="\n-----------------------------------------------------------------------------------------\n";
				 str+=findProceduresByNameOrId(null, id);
				 str+="\n-----------------------------------------------------------------------------------------\n";
				 str+=findPaymentsByNameOrId(null, id);

	            }
	            r2.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        } 
			 }
			  
 		 shutdown();
 		 return str;
		
    }
   public static String findProceduresByNameOrId(String name, int pid) throws SQLException{
     	
	   stmt = conn.createStatement();
	   ResultSet results;	
	   // there is 2 different queries here. The first will find procedures by using a patients name. The second is by using an id.
	   if (pid < 0){ // this will run a query to search for all procedures belonging to a chosen patient
	   	  results = stmt.executeQuery("select * from procedures proc where proc.procId IN(select pl.procId from"
			 		+ " procLink pl where pl.invId IN(select i.invId from invoice i where i.pid IN(select p.pid from patient p where p.name = '"+name+"')))");
	   	}else{
	   	  results = stmt.executeQuery("select * from procedures proc where proc.procId IN(select pl.procId from"
			 		+ " procLink pl where pl.invId IN(select i.invId from invoice i where i.pid IN(select p.pid from patient p where p.pid = "+pid+")))");
	   	}
    	
     	String str = "";
 		  ResultSetMetaData rsmd = null;
 		  rsmd = results.getMetaData();	
 		  int numberCols = rsmd.getColumnCount();
 			  for (int i=1; i<=numberCols; i++) {
 				 str+= rsmd.getColumnLabel(i) + "\t\t\t";
 			 }
 	 
 			 try{
 			 while(results.next()) {
 				 int id = results.getInt(1);
				 String procname = results.getString(2);
				double cost = results.getDouble(3);
				 str += "\n" + id + "\t\t\t\t" + procname + "\t\t\t\t" + cost + "\t\t\t\t";
 	            }
 	            results.close();
 	        }
 	        catch (SQLException sqlExcept)
 	        {
 	            sqlExcept.printStackTrace();
 	        }
     	return str;
	}
   // this finds payments of a patient, can be searhced by patient name or patient id
   public static String findPaymentsByNameOrId(String name, int pid) throws SQLException{    	
	   stmt = conn.createStatement();
	   ResultSet results;	
	   // there is 2 different querires here. The first will find procedures by using a patients name. The second is by using an id.
	   if (pid < 0){ // this will run a query to searhc by name
	   	  results = stmt.executeQuery("select * from payment pay where pay.payId IN(select pl.payId from"
			 		+ " payLink pl where pl.invId IN(select i.invId from invoice i where i.pid IN(select p.pid from patient p where p.name = '"+name+"')))");
	   	}else{
	   	  results = stmt.executeQuery("select * from payment pay where pay.payId IN(select pl.payId from"
			 		+ " payLink pl where pl.invId IN(select i.invId from invoice i where i.pid IN(select p.pid from patient p where p.pid = "+pid+")))");
	   	}
   	
    	String str = "\n";
		  ResultSetMetaData rsmd = null;
		  rsmd = results.getMetaData();	
		  int numberCols = rsmd.getColumnCount();
			  for (int i=1; i<=numberCols; i++) {
	                //print Column Names
				 str+= rsmd.getColumnLabel(i) + "\t\t\t";
			 }
	 
			 try{
			 while(results.next()) {
				 int id = results.getInt(1);
					double amount = results.getDouble(2);
					String date = results.getString(3);
					 str += "\n" + id + "\t\t\t\t" + amount + "\t\t\t\t\t" + date + "\t\t\t\t";
	            }
	            results.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
    	return str;
	}
	public static String findInvoiceByName(String name) throws SQLException{
    	
   	 String invoices ="select  invId, invAmount, invDate, paidStatus from invoice join patient on invoice.pid = patient.pid where patient.name = '" +name+"'";
    	String str = "";
    	 stmt = conn.createStatement();
		  ResultSet results = null;
		  ResultSetMetaData rsmd = null;
		  results = stmt.executeQuery(invoices);	 		 
		  rsmd = results.getMetaData();	
		  
		  int numberCols = rsmd.getColumnCount();
			  for (int i=1; i<=numberCols; i++) {
	                //print Column Names
				 str+= rsmd.getColumnLabel(i) + "\t\t\t";
			 }
			 try{
			 while(results.next()) {
				 int invid = results.getInt(1);
				 double amount = results.getDouble(2);
				 Date invDate = results.getDate(3);
				 boolean ispaid = results.getBoolean(4);
				 str += "\n" + invid + "\t\t\t\t" + amount + "\t\t\t\t" + invDate + "\t\t\t\t"+ ispaid +"\n";
	            }
	            results.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
    	return str;
    }
    public static String findInvoiceById(int pid) throws SQLException{
    	createConnection();
    	String invoices ="select  invId, invAmount, invDate, paidStatus from invoice where pid = " +pid;
    	 String str = "";
    	 
    	 stmt = conn.createStatement();
		  ResultSet results = null;
		  ResultSetMetaData rsmd = null;
		  
		  results = stmt.executeQuery(invoices); 
		  rsmd = results.getMetaData();	
		  
		  int numberCols = rsmd.getColumnCount();
			  for (int i=1; i<=numberCols; i++) {
	                //print Column Names
				 str+= rsmd.getColumnLabel(i) + "\t\t\t";
			 } 
			 try{
			 while(results.next()) {
				 int invid = results.getInt(1);
				 double amount = results.getDouble(2);
				 Date invDate = results.getDate(3);
				 boolean ispaid = results.getBoolean(4);
				 str += "\n" + invid + "\t\t\t\t" + amount + "\t\t\t\t\t" + invDate + "\t\t\t\t"+ ispaid +"\n";
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
			 
    	return str;
    }

    public  static void addPatient( String name, String address,String contactNum) {
		 createConnection();		
		try {
			String tableName = "patient";
			stmt = conn.createStatement();			
			String sql = "insert into " + tableName + "(name, address, contactNum) values ('" + name + "',"+" '" + address + "', '"+contactNum+"'" + ")";
			System.out.println(sql);
			stmt.execute(sql);
			stmt.close();
			System.out.println("Insert ok");
	       }
		catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			}
		catch(NullPointerException c){
		}
		 shutdown();
	    }
	public static String getAllPatients() {
		String str = "";
		 try {

			 createConnection();
			 String tableName = "patient";
			 stmt = conn.createStatement();
			 
			 ResultSet results = stmt.executeQuery("select PID, name, address, contactNum from " + tableName);
			 ResultSetMetaData rsmd = results.getMetaData();
			 int numberCols = rsmd.getColumnCount();
			 for (int i=1; i<=numberCols; i++) {
	                //print Column Names
				 str+= rsmd.getColumnLabel(i) + "\t\t\t\t";
			 }
			 			 
			 while(results.next()) {
				 int id = results.getInt(1);
				 String name = results.getString(2);
				 String address = results.getString(3);
				 String contact = results.getString(4);
				 str += "\n" + id + "\t\t\t\t" + name + "\t\t\t\t\t" + address + "\t\t\t\t\t"+ contact +"\n";
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
		 shutdown();
		 return str;
	    }
	// delete a patient from the database
	public static void deletePatient(int pid){
		 
		 System.out.println("trying to delete pid of "+pid);
		 createConnection();
		 try {
				String tableName = "patient";
				stmt = conn.createStatement();
	            // here there are many delete queries, because deleting a patient I must delete every invoice, procedure, payment associated with that patient
				String sql1 = "delete from PROCEDURES where procid IN(select procid FROM proclink l where l.invid IN(select invoice.invid from invoice where invoice.pid IN(SELECT pa.pid from patient pa where pa.pid ="+ pid+")))";
				String sql2 = "delete from payment where payid IN(select payid FROM paylink l where l.invid IN(select invoice.invid from invoice where invoice.pid IN(SELECT pa.pid from patient pa where pa.pid = "+pid+")))";
				String sql3 = "delete from proclink where invId IN(select invId FROM invoice where invoice.pid IN(select patient.pid from patient where patient.pid =" +pid+"))";
				String sql4 = "delete from paylink where invId IN(select invId FROM invoice where invoice.pid IN(select patient.pid from patient where patient.pid = "+pid+"))";
				String sql5 = "delete from invoice "
	            		+ "\nwhere PID  = "+ pid;
	            String sql6  = "delete from patient where pid = "+pid;  
	            
				stmt.execute(sql1);
				stmt.execute(sql2);
				stmt.execute(sql3);
				stmt.execute(sql4);
				stmt.execute(sql5);
				stmt.execute(sql6);

				stmt.close();
		       }
			catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
				}
			catch(NullPointerException c){
			}
		 
		 shutdown();
	 }
	
	public static void createConnection() {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        	conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
	
    public static void shutdown()
    {
        try
        {
            if (stmt != null)
            {
                stmt.close();
            }
            if (conn != null)
            {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                conn.close();
            }           
        }
        catch (SQLException sqlExcept)
        {
            
        }

    }
	public static void addProcedure(String procName, double procCost) {
		 createConnection();		
		try {
			String tableName = "procedures";
			stmt = conn.createStatement();
			String sql = "insert into " + tableName + "(procName, procCost) values ('" + procName + "'," + procCost + ")";
			System.out.println(sql);
			stmt.execute(sql);
			stmt.close();
			System.out.println("Insert ok");
	       }
		catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			}
		catch(NullPointerException c){
		}
		 shutdown();
	}
	public static void addToLink(int invId) {
		// TODO Auto-generated method stub
		 createConnection();		
			try {
				String tableName = "proclink";
				stmt = conn.createStatement();
				//String sql = "insert into " + tableName + "(PID, name, address) values (" +
	                 //   pid + ", '" + name + "',"+" '"  + address + "')";
			//	String sql = "insert into " + tableName + "(name, address, contactNum) values ('" + name + "',"+" '" + address + "', '"+contactNum+"'" + ")";
				String sql = "insert into " + tableName + "(invId) values (" + invId + ")";

				System.out.println(sql);
				stmt.execute(sql);
				stmt.close();
				System.out.println("Insert ok");
		       }
			catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
				//System.out.println("Error caught");
				}
			catch(NullPointerException c){
			System.out.println("Error caught here");
			}
			 shutdown();
		
		
	}
	public static ArrayList filterPatientInvoices(int filterPat) {
		createConnection();
    	ArrayList<String> results = null;
    	 PreparedStatement selectAllPeople = null; 
    	 
        ResultSet resultSet = null;
        System.out.println("here "+selectAllPeople);
        try 
        {
            selectAllPeople = 
               conn.prepareStatement( "SELECT invId, paidStatus FROM invoice  where pid = "+filterPat );
            System.out.println("here "+selectAllPeople);

           // executeQuery returns ResultSet containing matching entries
           resultSet = selectAllPeople.executeQuery(); 
           results = new ArrayList< String >();
           while ( resultSet.next() )
           {
              results.add((new String(
            		  resultSet.getInt("invId") + " : Is Paid = " +
            		  resultSet.getString("paidStatus"))
            		));
           } // end while
        } // end try
        catch ( SQLException sqlException )
        {
           sqlException.printStackTrace();         
        } // end catch
        finally
        {
           try 
           {
              resultSet.close();
           } // end try
           catch ( SQLException sqlException )
           {
              sqlException.printStackTrace();         
              try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           } // end catch
        } // end finally
    
        shutdown();
        return results;
	}
	// this method returns an arraylist of strings, each string has a patient ID along with their name. Will be used as an interactive listview on the GUI
	public static ArrayList filterPatientProcedures(int pid) {
		createConnection();
    	ArrayList<String> results = null;
    	 PreparedStatement selectAllPeople = null;  
        ResultSet resultSet = null;
        try 
        {
            selectAllPeople = 
            conn.prepareStatement("SELECT procid, procName FROM procedures where procid IN(select procID FROM proclink where invid IN(select invid from invoice where pid = "+pid+"))");
            System.out.println("here "+selectAllPeople);

           resultSet = selectAllPeople.executeQuery(); 
           results = new ArrayList< String >();
           while ( resultSet.next() )
           {
              results.add((new String(
            		  resultSet.getInt("procId") + " \t " +
            		  resultSet.getString("procName"))
            		));
           } // end while
        } // end try
        catch ( SQLException sqlException )
        {
           sqlException.printStackTrace();         
        } // end catch
        finally
        {
           try 
           {
              resultSet.close();
           } // end try
           catch ( SQLException sqlException )
           {
              sqlException.printStackTrace();         
              try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
           } // end catch
        } // end finally
    
        shutdown();
        return results;	
	}
	public static void addPayment(double payAmt, LocalDate date) {
		 createConnection();		
			try {
				String tableName = "payment";
				stmt = conn.createStatement();
				String sql = "insert into " + tableName + "(payAmount, payDate) values (" + payAmt + ",'" + date + "')";
				System.out.println(sql);
				stmt.execute(sql);
				stmt.close();
				System.out.println("Insert ok");
		       }
			catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
				}
			catch(NullPointerException c){
			System.out.println("Error caught here");
			}
			 shutdown();
		
	}
	public static void addToLinkPayment(int invId) {
		createConnection();		
		try {
			String tableName = "paylink";
			stmt = conn.createStatement();
			String sql = "insert into " + tableName + "(invId) values (" + invId + ")";
			System.out.println(sql);
			stmt.execute(sql);
			stmt.close();
			System.out.println("Insert ok");
	       }
		catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
			}
		catch(NullPointerException c){
		System.out.println("Error caught here");
		}
		 shutdown();
		
	}
	
	public static void deleteInvoice(int invId) {
		 createConnection();
		 try {
				stmt = conn.createStatement();
	            
				String sql1 = "delete from PROCEDURES where procid IN(select procid FROM proclink l where l.invid IN(select invoice.invid from invoice where invoice.invId = "+invId+"))";
				String sql2 = "delete from payment where payid IN(select payid FROM paylink l where l.invid IN(select invoice.invid from invoice where invoice.invId = "+invId+"))";
				String sql3 = "delete from proclink where invId = "+invId;
				String sql4 = "delete from paylink where invId = "+invId;
				String sql5 = "delete from invoice "
	            		+ "\nwhere invId  = "+ invId;
	            
				stmt.execute(sql1);
				stmt.execute(sql2);
				stmt.execute(sql3);
				stmt.execute(sql4);
				stmt.execute(sql5);
				stmt.close();
				System.out.println("invoice removed successfully");
		       }
			catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
				}
			catch(NullPointerException c){
			System.out.println("Error caught here");
			}
			// shutdown();
		 
		 shutdown();
		
	}
	public static void deleteProcedure(int procId) {
		 System.out.println("trying to delete invoice number  "+procId);
		 createConnection();
		 try {
				stmt = conn.createStatement(); 
				String sql1 = "delete from PROCEDURES where procid  = "+procId;
				String sql2 = "delete from proclink where procId = "+procId;
				
				stmt.execute(sql1);
				stmt.execute(sql2);
				stmt.close();
		       }
			catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
				}
			catch(NullPointerException c){
			System.out.println("Error caught here");
			}
		
		 shutdown();
		
	}
	public ArrayList<String> getSortedPatients() {
		createConnection();
    	ArrayList<String> results = null;
    	PreparedStatement selectAllPeople = null;  
        ResultSet resultSet = null;
        try 
        {
            selectAllPeople =  conn.prepareStatement( "SELECT pid, name FROM patient order by name" );

           // executeQuery returns ResultSet containing matching entries
           resultSet = selectAllPeople.executeQuery(); 
           results = new ArrayList< String >();
           while ( resultSet.next() )
           {
              results.add((new String(
            		  resultSet.getInt("pid") + " \t " +
            		  resultSet.getString("name"))
            		));
           } // end while
        } // end try
        catch ( SQLException sqlException )
        {
           sqlException.printStackTrace();         
        } // end catch
        finally
        {
           try 
           {
              resultSet.close();
           } // end try
           catch ( SQLException sqlException )
           {
              sqlException.printStackTrace();         
              try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
           } // end catch
        } // end finally
    
        shutdown();
        return results;
	}
	public static String findProceduresByPatientId(int pid) {
		String str = "";
		 try {
			 createConnection();
			 stmt = conn.createStatement();
			 ResultSet results = stmt.executeQuery("select procId, procName, procCost from procedures proc where proc.procId IN(select pl.procId from"
			 		+ " procLink pl where pl.invId IN(select i.invId from invoice i where i.pid = "+pid+"))");

			 ResultSetMetaData rsmd = results.getMetaData();
			 int numberCols = rsmd.getColumnCount();
			 for (int i=1; i<=numberCols; i++) {
				 str+= rsmd.getColumnLabel(i) + "\t\t\t";
			 }
			 
			 while(results.next()) {
				 int id = results.getInt(1);
				 String name = results.getString(2);
				double address = results.getDouble(3);
				 str += "\n" + id + "\t\t\t\t" + name + "\t\t\t\t\t" + address + "\t\t\t\t";
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
		 shutdown();
		 return str;
	}
	
	public static String findPaymentsByPatientId(int pid) {
		String str = "";
		 try {
			 createConnection();
			 String tableName = "payment";
			 stmt = conn.createStatement();
			 
			 ResultSet results = stmt.executeQuery(""
			 		+ "select payId, payAmount, payDate from payment where payid IN (select pay.payid from paylink pay where pay.invId IN(select i.invId from invoice i"
			 		+ " where i.pid = "+pid+"))");

			 ResultSetMetaData rsmd = results.getMetaData();
			 int numberCols = rsmd.getColumnCount();
			 for (int i=1; i<=numberCols; i++) {
				 str+= rsmd.getColumnLabel(i) + "\t\t\t";
			 }
			 
			 while(results.next()) {
				 int id = results.getInt(1);
				double amount = results.getDouble(2);
				String date = results.getString(3);
				 str += "\n" + id + "\t\t\t\t" + amount + "\t\t\t\t\t" + date + "\t\t\t\t";
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
		 shutdown();
		 return str;
	}
	// the invoice amount can change when procedures are added(increases) or when a payment is made(decreases)
	public static void alterInvoiceCost(int invId, double amount) {
		 createConnection();		

			try {
				stmt = conn.createStatement();
				String tableName = "invoice";
				String sql = "update " + tableName + " set invAmount = "+amount+" where invId = "+invId; 
				System.out.println(sql);
				stmt.execute(sql);
				stmt.close();
				System.out.println("Insert ok");
		       }
			catch (SQLException sqlExcept) {
				sqlExcept.printStackTrace();
				}
			catch(NullPointerException c){
			System.out.println("Error caught here");
			}
			 shutdown();
		
		
	}
	public static double getCurrentInvoiceCost(int invId) {
		createConnection();
		 String tableName = "invoice";
		ResultSet resultSet = null;
		 PreparedStatement maxInv = null;
		 double x = 0;
		 try {
			maxInv=   conn.prepareStatement( "SELECT invAmount from " + tableName + " where invId = "+invId);
			 resultSet = maxInv.executeQuery(); 
			 x = -1;	
			if (resultSet.next()) {
				  x = resultSet.getDouble(1);
				}
			shutdown(); 
		 } catch (SQLException e) {
			e.printStackTrace();
		}
		  return x;
	}
	public static void updatePaidStatus(int invId, boolean paid) {
		 createConnection();		
				try {
					stmt = conn.createStatement();
					String tableName = "invoice";
					String sql = "update " + tableName + " set paidStatus = "+paid+" where invId = "+invId; 
					System.out.println(sql);
					stmt.execute(sql);
					stmt.close();
					System.out.println("Insert ok");
			       }
				catch (SQLException sqlExcept) {
					sqlExcept.printStackTrace();
					}
				catch(NullPointerException c){
				System.out.println("Error caught here");
				}
				 shutdown();
		
	}
	public String findPatientForSortPage(int pid) {
		String str = "";
		 try {

			 createConnection();
			 stmt = conn.createStatement(); 
			 ResultSet results = stmt.executeQuery("select * from patient where pid = "+pid);
			 ResultSetMetaData rsmd = results.getMetaData();
			 int numberCols = rsmd.getColumnCount();
			 for (int i=1; i<=numberCols; i++) {
				 str+= rsmd.getColumnLabel(i) + "\t\t\t\t";
			 }
			 
			 while(results.next()) {
				 int id = results.getInt(1);
				 String name = results.getString(2);
				String  address = results.getString(3);
				String contactNum = results.getString(4);
				 str += "\n" + id + "\t\t\t\t" + name + "\t\t\t\t\t" + address + "\t\t\t\t" + contactNum;
	            }
	            results.close();
	            stmt.close();
	        }
	        catch (SQLException sqlExcept)
	        {
	            sqlExcept.printStackTrace();
	        }
		 shutdown();
		 return str;
	}
	
}
