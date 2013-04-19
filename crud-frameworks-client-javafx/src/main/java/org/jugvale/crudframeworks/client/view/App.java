package org.jugvale.crudframeworks.client.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.jugvale.crudframeworks.client.presentation.CrudframeworksView;

import com.airhacks.afterburner.injection.InjectionProvider;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		CrudframeworksView appView = new CrudframeworksView();
		Scene scene = new Scene(appView.getView());
		stage.setTitle("A Simple CRUD Application");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		InjectionProvider.forgetAll();
	}

	public static void main(String[] args) {
		launch(args);
	}
}