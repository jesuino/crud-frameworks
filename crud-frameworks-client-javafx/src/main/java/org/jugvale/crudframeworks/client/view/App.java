package org.jugvale.crudframeworks.client.view;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {		
		URL filePath = getClass().getResource(
				"/fxml/crudframeworks.fxml");
		Pane mainPane = (Pane) FXMLLoader.load(filePath);
		Scene scene = new Scene(mainPane);
		stage.setTitle("A Simple CRUD Application");
		stage.setScene(scene);
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}