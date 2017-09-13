package application;

import javafx.scene.control.Label;

//import java.awt.Color;
//import com.sun.prism.paint.Color;
//import java.awt.Font;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.*;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.*;
public class Confirm {

	public static void display() {
		Stage window = new Stage();
		
		//we are gonna block user interaction with other windows until this one is taken care of i.e alert box
		window.initModality(Modality.APPLICATION_MODAL);
		//window.setTitle(title);
		window.setMinWidth(450);
		
		Label label = new Label("Action Success!");
		//label.setTextFill(Color.RED);
		label.setTextFill(Color.GREEN);
		label.setFont(new Font("Arial", 20));

		//label.setTextFill(Color.web("#0076a3"));

		//label.setText(message);
		Button closeButton = new Button("Close the window");
		closeButton.setOnAction(e -> window.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		//window.show();
		///Display the window and wait till 
		window.showAndWait();
		

	}

}
