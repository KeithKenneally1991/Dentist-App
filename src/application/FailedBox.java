
package application;

import javafx.scene.control.Label;

//import java.awt.Font;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.*;
import javafx.scene.text.Font;
public class FailedBox{
   // this class contains one method, which will be invoked everytime an error occurs in the application, it will inform the user they entered data incorrect
	public static void display(String str) {
		Stage window = new Stage();
		
		//we are gonna block user interaction with other windows until this one is taken care of i.e alert box
		window.initModality(Modality.APPLICATION_MODAL);
		//window.setTitle(title);
		window.setMinWidth(450);
		
		Label label = new Label("Action Failed..Error found");
		Label message = new Label();
		message.setText(str);
		label.setTextFill(Color.RED);
		label.setFont(new Font("Arial", 20));

		
		//label.setText(message);
		Button closeButton = new Button("Close the window");
		closeButton.setOnAction(e -> window.close());
		
		VBox layout = new VBox(10);
		layout.getChildren().addAll(label,message, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		

	}

}
