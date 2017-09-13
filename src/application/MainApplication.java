package application;
import javafx.scene.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

//Keith Kenenally DNET2 
//Version: Database

public class MainApplication extends Application  {
 
	DatabaseDentist db  = new DatabaseDentist();

	Stage window;
	Button button1,button2,button3,button4,button5;
	//these listviews are for displaying a list of patient names/ids in 5 tabs
	
	ListView<Patient> patlist = new ListView<Patient>();   //add invoice

	//these lists displays patients name with patient id, user can the click on the patient they intend to modify
	ListView<String> list = new ListView<String>();   //add invoice
	ListView<String> listTwo = new ListView<String>(); // add procedure
	ListView<String> listThree = new ListView<String>();  // add payment
	ListView<String> listFour = new ListView<String>();   // delete invoice
	ListView<String> listFive = new ListView<String>();   // procedure
	ListView<String> listSix = new ListView<String>();   // procedure
	ListView<String> listDeletePatient = new ListView<String>();//add invoice//displays a list of patients names, which will be selected to be deleted
	ListView<String> sortedPatientList = new ListView<String>();   

	// interactive lists which will display invoice id, paid status, each one is for a different tab, eg add procedure, add payment
	ListView<String> invList = new ListView<String>();   // an interactive list which will display invoices. used on the add procedure page
	ListView<String> invListTwo = new ListView<String>(); // this list view is for the add payment page
	ListView<String> invListThree = new ListView<String>();// list displaying invoices in the delete invoice tab
	ListView<String> invListFour = new ListView<String>(); // list displaying invoices in the delete procedure tab
	
	ListView<String> procListViewOne= new ListView<String>(); // a list displaying procedures for a patient
	Catch validate = new Catch(); // a class which has methods to validate data
	boolean valid = false;

	ArrayList<Patient> patients= new ArrayList<Patient>();
	ArrayList<String> p= new ArrayList<String>();
	ArrayList<String> invoiceList= new ArrayList<String>();
	ArrayList<String> procedureList= new ArrayList<String>();
	ArrayList<String> paymentList= new ArrayList<String>();

	int patientID = 0;
	int invoiceNumCount = 0;
	int procID = 0;
	int paymentID = 0;

	Tab tab1 = new Tab("Add Patient");
	Tab tab2 = new Tab("Remove Patient");
	Tab tab3 = new Tab("Display Patient");
	Tab tab4 = new Tab("Manage Procedures");
	Tab tab5 = new Tab("Delete Procedure");
	Tab tab6 = new Tab("Add Payment");
	Tab tab7 = new Tab("Manage Invoices");
	Tab tab8 = new Tab("Report/Exit");
	//Borderpane for the login window
	BorderPane borderPane = new BorderPane();

	// GridPane for the log in window
	GridPane gridlog = new GridPane();
	
	Scene scene;
	//private Object //DateFormat;

	public static void main(String [] args){

launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		procListViewOne.setMaxHeight(200);
		procListViewOne.setPrefHeight(70);
		try {
			populateLists();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		window = primaryStage;
		window.setTitle("Dentist Application");
		
		//****This class  has many methods, every tab has its own method and the login window also *****
		loginWindow();
		addPatientTab();
		deletePatientTab();
		manageInvoices();
		displayPatientTab();
		addProcedureTab();
		addPaymentTab();
		saveAndQuitTab();

		//Create a tab with tabpanes seperating each section
		TabPane tab = new TabPane();
		// this removes the option to delete a tab through 
		tab.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tab.setMinHeight(650);
		tab.getTabs().addAll(tab1,tab2,tab3,tab7,tab4,tab6,tab8);
		tab.setTabMinWidth(116);
		
		borderPane.setTop(tab);

		scene = new Scene(borderPane, 730, 730);
		scene.getStylesheets().add(MainApplication.class.getResource("application.css").toExternalForm());
        Scene scenelogin = new Scene(gridlog, 300, 275);
        scenelogin.getStylesheets().add(MainApplication.class.getResource("application.css").toExternalForm());
		tab.setId("imagetest");
        window.setScene(scenelogin);
        window.setMinHeight(700);
        window.show();
	}
	
	public void addPatientTab(){
		
		GridPane grid = new GridPane();
		TextField nameField = new TextField();
		TextField addressField = new TextField();
		TextField contactField = new TextField();

		Label nameLabel = new Label("Name");
		Label addressLabel = new Label("Address");
		Label contactLabel = new Label("Contact Number");

		grid.add(nameLabel, 0, 0);
		grid.add(nameField, 1, 0);
		
		grid.add(addressLabel, 0, 2);
		grid.add(addressField, 1, 2);
		
		grid.add(contactLabel, 0, 4);
		grid.add(contactField, 1, 4);
		
		grid.setPadding(new Insets(24,25,25,25));
		grid.setVgap(20);
		
		Button submitPatient = new Button("Submit");
		Button clear = new Button("Clear");
		//when a the user clicks submit new patient..
		submitPatient.setOnAction(e->{
			
			boolean validAddress;
			boolean validName;
			//validate is a seperate class which has methods used for validating data. 
			validAddress = validate.emptyString(nameField.getText());//if one of the  boolean validations return false display an error box 
			validName = validate.emptyString(addressField.getText());
			int contactNo = 0;
			contactNo =  validate.isInt(contactField.getText());
			// add a new patient to the database, only if the correct information is recieved in every field	
			if(contactNo >= 0 && validName && validAddress){
					DatabaseDentist.addPatient(nameField.getText().toUpperCase(), addressField.getText().toUpperCase(), contactField.getText());
					DatabaseDentist.getAllPatients();
					Confirm.display();
			}
				else{
					FailedBox.display("Failed to add a new Patient. Please enter valid information");
				}
			
			//this populates the listViews lists every time a new patient is added. The listviews are interactive lists displaying patient info
			try {
				populateLists();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		clear.setOnAction(e-> {
			nameField.clear();addressField.clear();contactField.clear();
		});
		grid.add(clear, 0, 8);
		grid.add(submitPatient, 1, 8);
		
		grid.setHgap(20);
		grid.setAlignment(Pos.CENTER);
		tab1.setContent(grid);
	}
	
	// creates  and enables the user to remove a patient from the patient arraylist
	public void deletePatientTab() {
		
		//set the size of the listview which displays patients names
		listDeletePatient.setMaxWidth(200);
		listDeletePatient.setPrefHeight(170);
		
		BorderPane bp = new BorderPane();
		VBox vb1 = new VBox();
		TextField delNumber= new TextField();
		
		//this text area is used to show a list of patients along with their numbers
		TextArea ta = new TextArea();
		ta.setMaxWidth(200);
		ta.setMaxHeight(800);
		ta.setPadding(new Insets(50,30,30,30));
		bp.setCenter(vb1);
		
		//use setPromptText to make the writing faded grey
		delNumber.setPromptText("Patient Number");
		delNumber.setMaxWidth(200);
		
		Button delBtn = new Button("Remove Patient");
		// action to remove the patient
		delBtn.setOnAction(e->{
			String p = "";
			try{   // in the list of names and ID's, extract the ID'S only, this can be then used to find a specific patient
				 // I use this many times throughout the project, extracting a unique id from a listview of patient information.
				p = listDeletePatient.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
			}catch(NullPointerException c){
				p = null;
			}
			 valid = true;
			 
			 try{
				 valid = validate.emptyString(p);
			}catch(NullPointerException c){
				valid = false;
			}
			
			int pid =0;
			pid = validate.isInt(p);
			if (pid > 0){ // if the it less than 1 then a patient was not chosen form th list view
					// this is a method which runs queries to delete a patient and all their information including invoices, procedures, payments
					DatabaseDentist.deletePatient(pid);
					Confirm.display();					
			}
			else{
					FailedBox.display("Please chose a patient to remove");
			}
			// this re-populates the listviews being used in other methods which show patients currently stored
			try {
				populateLists();
			} catch (InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		vb1.setAlignment(Pos.CENTER);
		vb1.getChildren().addAll(listDeletePatient,delBtn);	
		vb1.setSpacing(20);
		bp.setCenter(vb1);
		tab2.setContent(bp);
	}
	
	public void displayPatientTab(){
		
		BorderPane tab3Pane = new BorderPane();
		VBox tab3VBox = new VBox();
		
		TextField displayName= new TextField();
		TextField displayNumber= new TextField();
		
		TextArea ta = new TextArea();
		 ta.getStyleClass().add("textarea");
		//use setPromptText to make the writing faded grey
		displayName.setPromptText("Search by Patient Name");
		displayNumber.setPromptText("Search by Patient Number");
		displayName.setMaxWidth(200);
		displayNumber.setMaxWidth(200);
		
		Button searchDisplay = new Button("Search");
		// this will search the patient array class either by name or by number
		searchDisplay.setOnAction(e->{
	
			int num = -1;
			String pNo = "";
			pNo = displayNumber.getText();
			num = validate.isInt(pNo);
			boolean name = false;  // used to determine if a valid name is searched
			System.out.println("num is "+num);
			try{
				String pname = displayName.getText();
				name = validate.emptyString(pname);
				if ( num > 0){
			// this will invoke a method which returns a string containing a patients details. the null parameter indicates to search by ID, not by name
					ta.setText(DatabaseDentist.findPatient(null, num));
					displayNumber.setText(null); // this needs to clear the field after searching, otherwise it causes problems with searching again
				}
				else if (name == true){
			// this will invoke a method which returns a string containing a patients details. the -1 parameter indicates to search by name, not by ID
					ta.setText(DatabaseDentist.findPatient(pname.toUpperCase(), -1));
				}
			
			else{
				FailedBox.display("Invalid information entered");
				}
			 		
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
		});

		Button displayAll = new Button("Display All Patients");
		displayAll.setOnAction(e->{
			showPatients(); // this invokes a method which displays all patients stored in the database
		});
		
		tab3VBox.setAlignment(Pos.CENTER);		
		final ToggleGroup group = new ToggleGroup();
		// radio buttons which give the option to search by patient name or by patient ID
		RadioButton rb1 = new RadioButton("Name");
		rb1.setToggleGroup(group);
		rb1.setSelected(true);

		RadioButton rb2 = new RadioButton("Number");
		rb2.setToggleGroup(group);
	
		rb1.getStyleClass().add("label");
		rb2.getStyleClass().add("label");
		//set the toggle buttons to display either name box or number box
		displayNumber.setVisible(false);
		
		rb1.setOnAction(e->{ // When search by name is chosen, the id field text box will disappear an vice versa
		displayNumber.setVisible(false);
		displayName.setVisible(true);	
		});
		
		rb2.setOnAction(e->{
			displayNumber.setVisible(true);
			displayName.setVisible(false);	
			});
		Button clear2 = new Button("Refresh"); // clear the fields and text area of data
		clear2.setOnAction(e-> {
			displayName.clear();
			displayNumber.clear();
			ta.clear();
		});
		VBox vb = new VBox(); //vbox for setting to textarea to sit to the right of the screen
		vb.getChildren().add(ta);
		vb.setPadding(new Insets(1,2,1,390));
		ta.setText("Displaying patient info..");
		tab3VBox.getChildren().addAll(rb1,rb2,displayName,displayNumber,searchDisplay,clear2,vb,displayAll);
		tab3VBox.setSpacing(20);
		tab3Pane.setCenter(tab3VBox);
		tab3.setContent(tab3Pane);
	}
	
	public void addProcedureTab(){
		
				//listTwo displays a list of patients which is interactive
				listTwo.setMaxWidth(200);
				listTwo.setPrefHeight(70);
				//invList displays invoices for the patient chosen 
				invList.setMaxWidth(200);
				invList.setPrefHeight(70);
				
		//Details for tab 4. This is the add procedure tab ***************************************************************
				BorderPane tab4pane = new BorderPane();
				VBox vb4 = new VBox();
				HBox hbox = new HBox();
				
				Button add = new Button("Add Procedure");
				add.setOnAction(e->{
					vb4.setVisible(true); // this will display the form to be filled in on screen
				});
				Button del = new Button("Delete Procedure");
				
				hbox.getChildren().add(add);
				hbox.getChildren().add(del);
				hbox.setSpacing(20);
				hbox.setPadding(new Insets(70,0,0,0));
				hbox.setMaxHeight(10);
				hbox.getStyleClass().add("procedureHbox");
				
				TextField procCost= new TextField();
				//TextField procNum= new TextField();
				TextField invoiceNum= new TextField();
				
				Button filterButton = new Button("Find invoices");
				
				ComboBox cmbox = new ComboBox();
				String a = "abc, def";
				cmbox.getItems().addAll("Bonding","Braces","Dentures","Filling","Implants","Root Canals","Sealants","Teeth Whitening","Veneers");
				cmbox.setVisibleRowCount(3);
				cmbox.setMinWidth(200);
				TextField newProc = new TextField();
				Button addNewProc = new Button();
				addNewProc.setOnAction(e->{
					cmbox.getItems().addAll("Bonding","Braces","Dentures","Filling","Implants","Root Canals","Sealants","Teeth Whitening","Veneers",newProc.getText());

				});
								
				//use setPromptText to make the writing faded grey
				procCost.setPromptText("Procedure Cost");
				invoiceNum.setPromptText("Invoice Number");
		
				filterButton.setOnAction(e-> {
					try {
						String plistview = "";	
						 int filterPat;
						
						try{ // extract the patient ID from the listview, can be used to find the correct invoices
							plistview = listTwo.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
						}catch(NullPointerException i){
							
						}catch(IndexOutOfBoundsException c){
						FailedBox.display("Please chose a patient");
				     	}
						filterPat = Integer.parseInt(plistview);
						System.out.println("filtered pat is "+ filterPat);
						filterInvoices(filterPat);
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
					}
				});
				
				Button addProc = new Button("Confirm");
				addProc.setOnAction(e-> {
					
					double amount = 0.00; 
					String y,procName;
					procName = "";
					int invId = 0;
					y = procCost.getText();

					String invlistview = " ";					
					try{  
					procName = cmbox.getSelectionModel().getSelectedItem().toString();					
					// extract the numbers only, this will give back the required ID which can be converted to an int
					invlistview = invList.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
					valid = validate.emptyString(procName);
					boolean isvalid = false;
					isvalid = validate.emptyString(invlistview);
					amount = validate.isDouble(y);
					 invId = Integer.parseInt(invlistview);					

					}catch(NullPointerException i){
						
					}catch(IndexOutOfBoundsException c){
					FailedBox.display("Please chose a patient");
			     	}catch(NumberFormatException k){
			     		FailedBox.display("Invalid information");
			     	}
				
					if (amount > 0 && valid == true){
						DatabaseDentist.addProcedure( procName, amount ); // this is adds a procedure carried out on a patient.
						DatabaseDentist.addToLink( invId );// there is a table linking procedure to invoice, this must be updated also
						Confirm.display();
						
						// after adding a procedure, we now need to update the total invoice cost
						double currentInvoiceAmount = DatabaseDentist.getCurrentInvoiceCost(invId); // first get the current invoice amount , then add
						amount+= currentInvoiceAmount;												// the cost of the new procedure to it	
						DatabaseDentist.alterInvoiceCost(invId, amount);
						
						if (amount > 0){
							DatabaseDentist.updatePaidStatus(invId, false);
						}
					}else{
						FailedBox.display("Invalid information recieved");
					}	
				});

				vb4.setAlignment(Pos.CENTER);				
				VBox v = new VBox();
				HBox v2 = new HBox();
				v2.getChildren().addAll(newProc, addNewProc);
				v2.setPadding(new Insets(1,300,1,50));
				v2.setSpacing(10);
				v.getChildren().addAll(invList);
				v.setAlignment(Pos.CENTER);
				v.setSpacing(0);
				vb4.getChildren().addAll(cmbox,procCost,listTwo,filterButton,invList,addProc);
				vb4.setSpacing(20);
				tab4pane.setTop(hbox);
				tab4pane.setCenter(vb4);
				vb4.setVisible(false);
				
		//I have a method which deletes a procedure. When the delProcedure button is pressed, I will call this method which will set the details on the current tab (tab 4)
				del.setOnAction(e->{
					deleteProcedureTab();
				});
				tab4.setContent(tab4pane);
	}
	
	public void deleteProcedureTab(){
		
		listFive.setMaxWidth(200); // listview displaying patient ID and patient name
		listFive.setPrefHeight(70);
		procListViewOne.setMaxWidth(200); // this is a listview of stored procedures. displays each ID with the name
		procListViewOne.setPrefHeight(70);

		BorderPane tab5pane = new BorderPane();
		VBox vb5 = new VBox();
		TextField invoiceNo = new TextField();
		TextField remProcNum = new TextField();
		Button delProc = new Button("Delete");
		Button goBackOnePage = new Button("Back");
		Button filterButton = new Button("Find procedures"); 
		// button to filter out procedures stored for a chosen patient
		filterButton.setOnAction(e-> {
			try {
				String plistview = "";	
				 int filterPat;
				try{
					plistview = listFive.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
				}catch(NullPointerException i){
					
				}catch(IndexOutOfBoundsException c){
				FailedBox.display("Please chose a patient");
		     	}
				filterPat = Integer.parseInt(plistview);
				filterProcedures(filterPat); //this method populates the procedures listview with appropiate procedures, ie the ones owned by that patient
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
		});
		// when the delete button is pressed..
		delProc.setOnAction(e->{
			
			boolean validInv = true;
			String p = "";
			String procString = "";
			if(validInv){
			try{				
				p = listFive.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
				procString= procListViewOne.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
			}catch(IndexOutOfBoundsException s){
				}
			 catch(NullPointerException c){
			}catch(NumberFormatException c){
			}}
			else{
				validInv = false;
			}
			
			int pid = validate.isInt( p);
			int procId = 0;
			procId = validate.isInt(procString);

			try {
				if (procId >= 0 ){
					DatabaseDentist.deleteProcedure(procId);
					filterProcedures(pid);
					Confirm.display();
				}
				else{
					FailedBox.display("Error, you must chose a procedure to delete");
				}
			} catch (InvocationTargetException e2) {
				e2.printStackTrace();
			}
		});

		invoiceNo .setPromptText("Invoice Number");
		remProcNum.setPromptText("Delete procedure number");
		vb5.getChildren().addAll(listFive,filterButton,procListViewOne,delProc,goBackOnePage);
		goBackOnePage.setOnAction(e-> {
			addProcedureTab();
		});
		vb5.setAlignment(Pos.CENTER);
		vb5.setSpacing(20);
		tab5pane.setCenter(vb5);
		tab4.setContent(tab5pane);	
	};

	public void addPaymentTab(){
		
		listThree.setMaxWidth(200);
		listThree.setPrefHeight(70);
		invListTwo.setMaxWidth(200);
		invListTwo.setPrefHeight(70);
		BorderPane tab6pane = new BorderPane();
		VBox vb6 = new VBox();
		
		TextField paymentAmount= new TextField();
		Button filterButton = new Button("Find Invoices");
		paymentAmount.setPromptText("Payment Amount");
		VBox v = new VBox();
		DatePicker ss=new DatePicker(LocalDate.now()); // here is a widget which displays an interactive calendar to chose dates
		DatePickerSkin datePickerSkin = new DatePickerSkin(ss);
        Node popupContent = datePickerSkin.getPopupContent();
        v.getChildren().addAll(paymentAmount,popupContent);
        v.setSpacing(10);
        v.setMaxSize(280, 10);
        
		// find and display invoices for a chosen patient..
        filterButton.setOnAction(e-> {
			try {
				String plistview = "";	
				 int filterPat = 0;
				try{
					plistview = listThree.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
					filterPat = Integer.parseInt(plistview);

				}catch(NullPointerException i){
					System.out.println("Error");
					FailedBox.display("Please chose a patient first");
				}catch(IndexOutOfBoundsException c){
				FailedBox.display("Please chose a patient");
		     	}
				filterInvoices(filterPat); // invoke the method which populates/filteres the listview
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
		});
		Button addPay = new Button("Add Payment");
		addPay.setOnAction(e-> {
			
			int invId = 0;
			double payAmt = 0.00;			
			String y;
			y = paymentAmount.getText();
			payAmt = validate.isDouble(y);
			
			String invlistview = "";					
			try{  
				invlistview = invListTwo.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
			
			}catch(NullPointerException i){
				
			}catch(IndexOutOfBoundsException c){
			FailedBox.display("Please chose a patient");
	     	}

			invId = validate.isInt(invlistview);
			// if a valid amount is recieved, and an invoice is chosen to add a payment to, then proceed ..
			if (payAmt >= 0 && invId >= 0){
				DatabaseDentist.addPayment( payAmt, ss.getValue() ); // add the payment and date to the database
				DatabaseDentist.addToLinkPayment( invId );    // also update the table which links to the invoice table
				
				// after adding a payment, we now need to update the invoice amount, this will be decreased by the payment recieved
				double currentInvoiceAmount = DatabaseDentist.getCurrentInvoiceCost(invId); // first get the current invoice amount , then add
				double newInvoiceAmount = currentInvoiceAmount - payAmt;
															// the cost of the new procedure to it	
				DatabaseDentist.alterInvoiceCost(invId, newInvoiceAmount);	
				
				if (newInvoiceAmount <= 0){
					DatabaseDentist.updatePaidStatus(invId, true);
				}
				Confirm.display();

			}
			else{
				FailedBox.display("Error, invalid information recieved");
			}	
		});
		vb6.setAlignment(Pos.CENTER);
		vb6.getChildren().addAll(listThree,filterButton,invListTwo,addPay);
		vb6.setSpacing(20);
		tab6pane.setCenter(v);	
		tab6pane.setRight(vb6);
		vb6.setPadding(new Insets(100,200,100,1));
		tab6.setContent(tab6pane);
	}
	
	public void saveAndQuitTab(){
		
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
		Button reports = new Button("Reports");
		Button exit = new Button("Exit");
		Label sort = new Label("Sort stored patients by name");
		sort.setMinWidth(400);
		Button sortList = new Button("Sort");
		sortList.setMinSize(90, 40);
		VBox one = new VBox();
		one.getChildren().addAll(sort);
		one.setSpacing(20);
		VBox two = new VBox();
		two.getChildren().addAll(sortList);
		two.setSpacing(10);
		FlowPane flow = new FlowPane();
	    flow.setPadding(new Insets(1,333, 333, 1));
	    flow.setVgap(30);
	    flow.setHgap(50);
	    flow.getChildren().addAll(one,two);
	    flow.setAlignment(Pos.CENTER);
	    bp.setCenter(flow);
	    
	    flow.setVisible(false);
		reports.setOnAction(e-> {
		    flow.setVisible(true);
		});
		hbox.getChildren().addAll(reports,exit);
		hbox.setSpacing(20);
		hbox.setPadding(new Insets(90,0,0,0));
		hbox.getStyleClass().add("procedureHbox");
		
		sortList.setOnAction(e-> {
			sortPatientList("name");
		});
		exit.setOnAction(e-> {
			window.close();
		});
		bp.setTop(hbox);
		tab8.setContent(bp);
	}
	// this method asks to chose add or delete invoice, it also has the code for all the "Add Invoice" form.
	// The delete form is inside a different method. ie. "deleteProcedure"
	public void manageInvoices(){
		
		
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
	
		// two buttons to Chose add or delete section
		Button add = new Button("Add Invoice");
		Button del = new Button("Delete Invoice");
		Button goBack = new Button("Back");
		Button submit = new Button("Submit");
		
		hbox.getChildren().add(add);
		hbox.getChildren().add(del);
		hbox.setSpacing(20);
		hbox.setPadding(new Insets(90,0,0,0));
		hbox.getStyleClass().add("procedureHbox");
		bp.setTop(hbox);
		tab7.setContent(bp);
		
		//add invoice form
		BorderPane addPane = new BorderPane();
		VBox vb6 = new VBox();
	
		TextField invoiceDate= new TextField();
		TextField patientNum= new TextField();
		//use setPromptText to make the writing faded grey
		invoiceDate.setPromptText("Invoice Date");
		patientNum.setPromptText("Patient Number");

		VBox v = new VBox();
		DatePicker ss=new DatePicker(LocalDate.now());
		DatePickerSkin datePickerSkin = new DatePickerSkin(ss);
		//DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();
        v.getChildren().add(popupContent);
        v.setMaxSize(280, 10);
        
		final ToggleGroup group = new ToggleGroup();
		RadioButton rb1 = new RadioButton("Paid");
		rb1.setToggleGroup(group);
		rb1.setSelected(true);
		
		RadioButton rb2 = new RadioButton("Not Paid");
		rb2.setToggleGroup(group);
		rb1.getStyleClass().add("label");
		rb2.getStyleClass().add("label");

		//this list is a listview displaying patient name with their id
		list.setMaxWidth(200);
		list.setPrefHeight(70);

		vb6.setAlignment(Pos.CENTER);
		vb6.getChildren().addAll(v,list,rb1,rb2,submit,goBack);
	
		// go back one page
		goBack.setOnAction(e-> {
			manageInvoices();
		});
		vb6.setSpacing(20);
		addPane.setCenter(vb6);	
		addPane.setVisible(false);
		add.setOnAction(e->{
			//when the user chooses to add a new procedure, populate the listviews and show the form to add
		try {
			populateLists(); // a method which populates patient list views with patient id + name
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		addPane.setVisible(true);	
		tab7.setContent(addPane);	
		});
		//if the user choses the delete invoice button display the form on screen (calls the deleteInvoice Method)
		del.setOnAction(e-> {
			deleteInvoice();
		});
		// if the user clicks on submit new invoice..
		submit.setOnAction(e-> {
			//convert the invoice#,patientNo, and invoiceAmount text fields from Strings to  int/double..
			String p = "";
			int pid = 0;
			try{   // in the list of names and ID's, extract the ID'S only, this can be then used to find a specific patient
				p = list.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
			}catch(NullPointerException c){
				p = null;
			}
			 valid = true;
			 try{
				 valid = validate.emptyString(p);
				pid = Integer.parseInt(p);
			}catch(NullPointerException c){
				valid = false;
			}
			
			pid = validate.isInt(p);
			boolean paidStatus;
			// set the is paid true or false, chosen by radio buttons
			if (rb1.isSelected()){
				paidStatus = true;
			}
			else{
				paidStatus = false;
			}
			
			if (pid >= 0){
				DatabaseDentist.addInvoice(pid, 0.00, ss.getValue(), paidStatus );
				Confirm.display();
			}else{
				FailedBox.display("Invalid information recieved");
			}
					try {
						populateLists();
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
					}
		});
	}
	
	public void deleteInvoice(){
		
		listFour.setMaxWidth(200);
		listFour.setPrefHeight(70);
		invListThree.setMaxWidth(200);
		invListThree.setPrefHeight(70);
		BorderPane bp = new BorderPane();
		VBox vbox = new VBox();
		TextField remInvoiceNo = new TextField();
		
		Button delInvoice = new Button("Delete");
		Button goBackOnePage = new Button("Back");
		Button filterButton = new Button("Find invoices");
		filterButton.setOnAction(e-> {
			//filteredInvoices = DatabaseDentist.filterPatientInvoices(filterPat);
			try {
				String plistview = "";	
				 int filterPat;
				try{
					plistview = listFour.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
				}catch(NullPointerException i){
					
				}catch(IndexOutOfBoundsException c){
				FailedBox.display("Please chose a patient");
		     	}
				filterPat = Integer.parseInt(plistview);
				filterInvoices(filterPat);
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
		});
		delInvoice.setOnAction(e->{
			int invId = 0;
			String p = "";
			String invoiceString = "";
			try{				
				p = listFour.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
				invoiceString = invListThree.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
			}catch(IndexOutOfBoundsException s){
				FailedBox.display("Please chose a patient");}
			catch(NullPointerException c){
			} 
			catch(NumberFormatException c){	
			}
			valid = validate.emptyString(p);
			int pid = validate.isInt( p);
			invId = validate.isInt(invoiceString);
			System.out.println("Patient number is: "+pid +"\nInvoice chosen is: "+invId);
			DatabaseDentist.deleteInvoice(invId);
			try {
				filterInvoices(pid);
			} catch (InvocationTargetException e2) {
				e2.printStackTrace();
			}	
		});

		remInvoiceNo.setPromptText("Delete invoice Number");
		remInvoiceNo.setMaxWidth(150);
		vbox.getChildren().addAll(listFour,filterButton,invListThree,delInvoice,goBackOnePage);
		goBackOnePage.setOnAction(e-> {
			manageInvoices();
		});
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(20);
		bp.setCenter(vbox);
		tab7.setContent(bp);
		
	}
	
	public void loginWindow() throws FileNotFoundException{
		
		  borderPane.setId("loginPage"); 
		  // footer for the main scene 
		Label footer = new Label("keith.kenneally@mycit.ie\t\t\t\t\t\t\t\t\tDNET2\t\t\t\t\t\t\t\t\tR00142850");
		footer.getStyleClass().add("footer");
		footer.setPadding(new Insets(10,10,35,135));
		borderPane.setBottom(footer);
		//borderPane.getStyleClass().add("border-pane");
		window.setMinWidth(1000);
		window.setMaxWidth(1000);

		// Create a login/welcome screen. This will be on a new scene
        gridlog.setAlignment(Pos.CENTER);
        gridlog.setHgap(10);
        gridlog.setVgap(10);
        gridlog.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Verdana", FontPosture.ITALIC, 60));
        scenetitle.setFill(Color.MIDNIGHTBLUE);
        gridlog.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        gridlog.add(userName, 0, 1);

        TextField userTextField = new TextField();
        gridlog.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        gridlog.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        gridlog.add(pwBox, 1, 2);
        gridlog.setId("loginPage");
                
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        gridlog.add(hbBtn, 1, 4);

        btn.setOnAction(e->
        {     //User logged in therefore switch to main scene
                window.setScene(scene);
        });
	}

	
	public void showPatients(){ // a method displaying patients stored in the database
		
		BorderPane bp = new BorderPane();
		Scene infoPat = new Scene(bp,500,500);
		 infoPat.getStylesheets().add(MainApplication.class.getResource("application.css").toExternalForm());
		
		TextArea ta = new TextArea("Invoices..");
		Button goBack = new Button("Return");
		goBack.getStyleClass().add("return");
		bp.setCenter(ta);
		bp.setBottom(goBack);
		ta.setText(DatabaseDentist.getAllPatients());

		//if the return button is pressed go back to the main scene
		goBack.setOnAction(e-> {
			window.setScene(scene);
		});
		window.setScene(infoPat);
	}

	// this method gets patient id, patient name, saves them as a string, then populates a listview with every different patient found
	public void populateLists() throws InvocationTargetException {
		// must clear them before re-populating or else it will create duplicates
		list.getItems().clear();
		listTwo.getItems().clear();	
		listThree.getItems().clear();
		listFour.getItems().clear();
		listFive.getItems().clear();
		listSix.getItems().clear();
		listDeletePatient.getItems().clear();
	
		DatabaseDentist d = new DatabaseDentist();
		String x = "";
				//int id = 0;
				try{	 // the lists will show patient name and id, which will be displayed in a vertical list and be interactive
					p  = d.getPatientListView();  // p is an arraylist, getPatientListView() returns an arraylist of strings displaying patient id + name
					for (int i = 0; i < p.size(); i++){
						x = p .get(i);
						//list.getItems().addAll(p);  note to self this also works
						list.getItems().add(x);
						listTwo.getItems().add(x);
						listThree.getItems().add(x);
						listFour.getItems().add(x);
						listFive.getItems().add(x);
						listSix.getItems().add(x);
						listDeletePatient.getItems().add(x);
						}
				}
				catch(IndexOutOfBoundsException c){
				}
								
	}
	// this method retrieves the invoices stored for a specific patient and displays them in a listview
	public void filterInvoices(int patientNumber) throws InvocationTargetException {
		invList.getItems().clear();
		invListTwo.getItems().clear();
		invListThree.getItems().clear();
		invListFour.getItems().clear();

		DatabaseDentist d = new DatabaseDentist();
		String y = "";
		try{	 // the lists will show patient name and id, which will be displayed in a vertical list and be interactive
			invoiceList = d.filterPatientInvoices(patientNumber);
			for (int i = 0; i < invoiceList.size(); i++){
				y = invoiceList .get(i);
				invList.getItems().add(y);
				invListTwo.getItems().add(y);
				invListThree.getItems().add(y);
				invListFour.getItems().add(y);

				}
		}
		catch(IndexOutOfBoundsException c){
		}	
	}
	// this method retrieves the procedures stored for a specific payient and displays them in a listview
	public void filterProcedures(int patientNumber) throws InvocationTargetException {
		
		procListViewOne.getItems().clear();
		DatabaseDentist d = new DatabaseDentist();
		String y = "";
		try{	 // the lists will show patient name and id, which will be displayed in a vertical list and be interactive
			procedureList = d.filterPatientProcedures(patientNumber);
			for (int i = 0; i < procedureList.size(); i++){
				y = procedureList .get(i);
				procListViewOne.getItems().add(y);
				}
		}
		catch(IndexOutOfBoundsException c){
		}	
	}
	
// this method retrieves a listview of patients sorted by name. The listview will be interactive, the user can click a patient then search their invoice ect..
	public void sortPatientList(String sortMethod) {
		
		sortedPatientList.setMaxWidth(200);
		sortedPatientList.setMinHeight(572);
		sortedPatientList.setMaxHeight(1000);

		BorderPane bp = new BorderPane();
		Scene infoPat = new Scene(bp,500,500);
		VBox vbox = new VBox();
		Button showDetails = new Button("Show details");
		 infoPat.getStylesheets().add(MainApplication.class.getResource("application.css").toExternalForm());
		
		TextArea ta = new TextArea();
		Button goBack = new Button("Return");
		goBack.getStyleClass().add("return");
		//bp.setCenter(ta);
		bp.setLeft(vbox);
		bp.setCenter(ta);
		ta.setMaxSize(700, 700);
		ta.setStyle("-fx-border-color: blue; -fx-border-width: 5; "
                + "-fx-border-radius: 16; -fx-focus-color: blue; -fx-background-color: blue; -fx-text-area-background: black");
		ta.setText("Displaying details for this patient..");
			    
			sortedPatientList.getItems().clear();	
			DatabaseDentist d = new DatabaseDentist();
			String x = "";
					try{	 // the lists will show patient name and id, which will be displayed in a vertical list and be interactive
						p  = d.getSortedPatients(); 
						for (int i = 0; i < p.size(); i++){
							x = p .get(i);
							sortedPatientList.getItems().add(x);
							}
					}
					catch(IndexOutOfBoundsException c){
					}
	
					vbox.getChildren().addAll(sortedPatientList, showDetails, goBack);
		
					showDetails.setOnAction(e->{
						String p = "";
						int pid = 0;
						try{   // in the list of names and ID's, extract the ID'S only, this can be then used to find a specific patient
							p = sortedPatientList.getSelectionModel().getSelectedItem().replaceAll("[^0-9]", "");
						}catch(NullPointerException c){
							p = null;
						}
						 valid = false;
						 try{
							 valid = validate.emptyString(p);
							pid = Integer.parseInt(p);
							pid = validate.isInt(p);
						}catch(NullPointerException c){
							valid = false;
						}
						 String patient = "";
						 String invoices = "";
						 String procedures = "";
						 String payments = "";
						 if (pid >= 0 ){
							 try {
								 patient = d.findPatientForSortPage( pid);
								invoices = d.findInvoiceById(pid);
								procedures = d.findProceduresByPatientId(pid);
								payments = d.findPaymentsByPatientId( pid);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							 // this text box will display every detail of the patient chosen, including all invoices, procedures and payments.
							 ta.setText(patient+"\n--------------------------------------------------------------------"
							 		+ "----------------------------------------------\n"+invoices+"\n--------------------"
								 		+ "------------------------------------------------------------------------------------------"
								 		+ "\n"+procedures + "\n----------------------------------------------------------------------------------"
								 				+ "------------------------------------\n"+payments); 
							
						 }	
					});

		//if the return button is pressed go back to the main scene
		goBack.setOnAction(e-> {
			window.setScene(scene);
		});
		window.setScene(infoPat);
}
}

