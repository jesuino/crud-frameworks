package org.jugvale.crudframeworks.client.presentation;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import org.jugvale.crudframeworks.client.business.Framework;
import org.jugvale.crudframeworks.client.controller.FrameworkClientService;

/**
 * TODO: Organize methods names and clean up the code, IT'S A MESS! SOLVE THE
 * MODIFY ISSUE SOLVE THE ISSUE WITH FORMATTED DATA
 * 
 * @author william
 */
public class CrudframeworksPresenter implements Initializable {

	public static SimpleDateFormat dataFormatter = new SimpleDateFormat(
			"MM/dd/yyyy");
	FrameworkClientService client;

	@FXML
	StackPane pnlRoot;
	// Components from the View Pane
	@FXML
	Label message;
	@FXML
	AnchorPane pnlTable;
	@FXML
	Label lblStatus;
	@FXML
	TextField txtFilter;
	@FXML
	RadioButton rbName;
	@FXML
	RadioButton rbPlatform;
	@FXML
	TableView<Framework> tblFrameworks;
	@FXML
	AnchorPane viewPane;
	@FXML
	TableColumn<Framework, String> columnLastRelease;

	// Components from the Edit Pane
	@FXML
	TitledPane titlePaneFramework;
	@FXML
	TextField txtName;
	@FXML
	TextField txtCreator;
	@FXML
	TextField txtReleaseDate;
	@FXML
	TextField txtHomePage;
	@FXML
	TextField txtPlatform;
	@FXML
	TextField txtCurrentVersion;
	@FXML
	TextArea txtDescription;
	@FXML
	Button btnPnlAction;
	@FXML
	AnchorPane editPane;
	@FXML
	Button btnCancelEditionMode;
	@FXML
	Label lblValidationError;

	// Actions
	EventHandler<ActionEvent> addAction = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evt) {
			Framework f = new Framework();
			if (setFrameworkFieldsAndValidate(f)) {
				client.add(f);
				refreshData();
				status("Framework '" + f.getName() + "' added.");
				setEditionMode(false);
			}
		}
	};

	EventHandler<ActionEvent> updateAction = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent evt) {
			Framework f = tblFrameworks.getSelectionModel().getSelectedItem();
			if (f != null) {
				if (setFrameworkFieldsAndValidate(f)) {
					client.update(f);
					refreshData();
					status("Framework with ID '" + f.getId() + "' modified.");
					setEditionMode(false);
				}
			} else {
				// should not get here;;;
				status("No Framework selected in the table...");
			}
		}
	};

	public CrudframeworksPresenter() {
		client = new FrameworkClientService();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		refreshData();
		formatLastReleaseDateColumn();
		setEditionMode(false);
		// TODO: Lambda
		btnCancelEditionMode.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				setEditionMode(false);
			}
		});
	}

	public void refreshTable() {
		refreshData();
	}

	public void filterTable() {
		String valueFilterText = txtFilter.getText();
		// Nothing to do here...
		if (valueFilterText.trim().isEmpty())
			return;
		// first we fill all the data again
		refreshData();
		List<Framework> data = tblFrameworks.getItems();
		List<Framework> filteredList = new ArrayList<>();
		// we could use lambda, but my eclipse still doesn't support it =/
		for (Framework framework : data) {
			if (rbPlatform.isSelected()) {
				String platform = framework.getPlatform();
				if (filterCondition(platform, valueFilterText))
					filteredList.add(framework);
			} else {
				String name = framework.getName();
				if (filterCondition(name, valueFilterText))
					filteredList.add(framework);
			}
		}
		tblFrameworks.getItems().setAll(filteredList);
		status("Table filtered, showing only frameworks which \'"
				+ (rbPlatform.isSelected() ? "platform" : "name")
				+ "\' starts with " + valueFilterText);
	}

	private boolean filterCondition(String value, String valueToFilter) {
		return value != null && value.startsWith(valueToFilter);
	}

	public void modifyFrameworkAction() {
		showFrameworkPane(Mode.UPDATE);

	}

	public void addFrameworkAction() {
		showFrameworkPane(Mode.NEW);
	}

	public void removeFrameworkAction() {
		Framework f = tblFrameworks.getSelectionModel().getSelectedItem();
		if (f != null) {
			client.remove(f.getId());
			refreshData();
			status("Framework '" + f.getName() + "' removed.");
		} else {
			status("No Framework selected in the table...");
		}
	}

	private void refreshData() {
		status("Filling data from server...");
		tblFrameworks.getItems().setAll(client.getAll());
		status("Data updated");
	}

	private void setEditionMode(boolean value) {
		viewPane.setVisible(!value);
		editPane.setVisible(value);
		lblValidationError
				.setText("Name, current version and platform are obligatory.");
	}

	private void showFrameworkPane(Mode mode) {
		setEditionMode(true);
		clearFields();
		// TODO: Improve this using binding...
		switch (mode) {
		case NEW:
			titlePaneFramework.setText("Add new Framework");
			btnPnlAction.setText("Add Framework");
			btnPnlAction.setOnAction(addAction);
			break;

		case UPDATE:
			Framework f = tblFrameworks.getSelectionModel().getSelectedItem();
			if (f != null) {
				titlePaneFramework.setText("Update Framewor ID '" + f.getId()
						+ "'");
				btnPnlAction.setText("Update Framework");
				btnPnlAction.setOnAction(updateAction);
				setFrameworkFields(f);
			} else {
				status("You need to select a framework in the table");
				setEditionMode(false);
			}
			break;
		}

	}

	// TODO GEZ, look at this :( change this ASAP
	private void setFrameworkFields(Framework f) {
		// the concatenations are to avoid null when getting the value..
		txtCreator.setText(f.getCreator() + "");
		txtCurrentVersion.setText(String.valueOf(f.getCurrentVersion()));
		txtDescription.setText(f.getDescription() + "");
		txtHomePage.setText(f.getHomePage() + "");
		txtName.setText(f.getName() + "");
		txtPlatform.setText(f.getPlatform() + "");
		Date date = f.getLastReleaseDate();
		if (date != null)
			txtReleaseDate.setText(dataFormatter.format(date));
	}

	private void clearFields() {
		txtCreator.setText("");
		txtCurrentVersion.setText("");
		txtDescription.setText("");
		txtHomePage.setText("");
		txtName.setText("");
		txtPlatform.setText("");
		txtReleaseDate.setText("");
	}

	// TODO Change to a better validation
	private boolean setFrameworkFieldsAndValidate(Framework f) {
		if (f == null) {
			f = new Framework();
		}
		String name = txtName.getText();
		String currentVersion = txtCurrentVersion.getText();
		String homePage = txtHomePage.getText();
		String creator = txtCreator.getText();
		String lastReleaseDate = txtReleaseDate.getText();
		String description = txtDescription.getText();
		String platform = txtPlatform.getText();

		if (name.isEmpty() || name.length() < 3) {
			validationError("Name lenght should be greater than 3");
		} else if (currentVersion.isEmpty()) {
			validationError("You need to inform the framework current version");
		} else if (creator.isEmpty()) {
			validationError("Creator is obligatory");
		} else if (platform.isEmpty()) {
			validationError("Platform is obligatory");
		}
		// passed all validations...
		else {
			try {
				f.setDescription(description);
				f.setHomePage(homePage);
				f.setName(name);
				f.setCreator(creator);
				f.setPlatform(platform);
				// can't fail the following fields, if so, it won't validate...
				f.setCurrentVersion(Double.valueOf(txtCurrentVersion.getText()));
				// date isn't obligatory.. but user needs to set a valid date.
				if (!lastReleaseDate.isEmpty()) {
					Date releaseDate = dataFormatter.parse(lastReleaseDate);
					f.setLastReleaseDate(releaseDate);
				}
				return true;
			} catch (ParseException e) {
				validationError("Check Last Release Date. Use format "
						+ dataFormatter.toPattern());
			} catch (NumberFormatException e) {
				validationError("Invalid version");
			}
		}
		return false;
	}

	private void validationError(String value) {
		lblValidationError.setText(value);
	}

	private void formatLastReleaseDateColumn() {
		columnLastRelease
				.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Framework, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(
							CellDataFeatures<Framework, String> cell) {
						Date lastReleaseData = cell.getValue()
								.getLastReleaseDate();
						String formatedDate = "";
						if (lastReleaseData != null) {
							formatedDate = dataFormatter
									.format(lastReleaseData);
						}
						return new SimpleStringProperty(formatedDate);
					}
				});
	}

	private void status(String status) {
		// TODO: Add level of severity to the status and change the label color
		lblStatus.setText(status);
	}

	enum Mode {
		UPDATE, NEW
	}
}