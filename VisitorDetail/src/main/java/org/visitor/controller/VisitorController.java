/**
 * This class handles the various action event for visitor application
 */
package org.visitor.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import org.visitor.bean.CountryBean;
import org.visitor.bean.VisitorBean;
import org.visitor.bean.VisitorInfoBean;
import org.visitor.client.VisitorClient;

import com.google.inject.Inject;

public class VisitorController implements Initializable{
	
	private final static Logger logger = Logger.getLogger(VisitorController.class.getName()); 
	
	
	private VisitorClient visitorClient;
	
	@FXML private Text errorText;
	
	
	@FXML private StackPane mainPane;
	
	
	@FXML private VBox appPane = new VBox();
	@FXML private ComboBox<CountryBean> countryComboBox;
	@FXML private ComboBox<VisitorBean> visitorComboBox;
	@FXML private StackPane visitorInfoPane;
	@FXML private Text nameText;
	@FXML private Text genderText;
	@FXML private FlowPane countriesVisitedPane;
	
	
	@FXML private VBox loadingPane = new VBox();
	
	/**
	 * Injecting VisitorClient via Google guice in VisitorModule class	
	 * @param visitorClient
	 */
	@Inject
	public VisitorController(VisitorClient visitorClient){
		this.visitorClient = visitorClient;
	}
	
	
	/**
	 * Show/Hide the progress indicator
	 */
	private void toggleProgressIndicator(boolean show){
		if(show){
			appPane.setOpacity(30);
			appPane.setDisable(true);
			if(!mainPane.getChildren().contains(loadingPane))
				mainPane.getChildren().add(loadingPane);
		}else{
			appPane.setOpacity(100);
			appPane.setDisable(false);
			mainPane.getChildren().remove(loadingPane);
		}
	}
	

	/**
	 * Clear the country combobox
	 */
	private void clearCountry() {
		countryComboBox.getSelectionModel().clearSelection();
		countryComboBox.getItems().clear();
		clearVisitor();
	}

	/**
	 * Clear the visitor combobox
	 */
	private void clearVisitor() {
		visitorComboBox.getSelectionModel().clearSelection();
		visitorComboBox.getItems().clear();
		clearVisitorInfo();
	}

	/**
	 * Clear the visitor Info Section
	 */
	private void clearVisitorInfo() {
		visitorInfoPane.setVisible(false);
		countriesVisitedPane.getChildren().clear();
		nameText.setText("");
		genderText.setText("");

	}

	/**
	 * Load the visitor combobox asynchronously. It will get called onchange event of country ComboBox
	 */
	@FXML
	private void populateVisitor() {
		logger.log(Level.INFO,"Populating the list of visitors for a particular country");
		toggleProgressIndicator(true);
		clearVisitor();
		final String href = countryComboBox.getSelectionModel().getSelectedItem().getHref();
		Service<List<VisitorBean>> service = new Service<List<VisitorBean>>() {

			@Override
			protected Task<List<VisitorBean>> createTask() {
				return new Task<List<VisitorBean>>() {

					@Override
					protected List<VisitorBean> call() throws Exception {
						return visitorClient.getVisitorList(href);
					}

				};
			}
		};
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			public void handle(WorkerStateEvent t) {
				logger.log(Level.INFO,"Visitor List get populated successfully");
				visitorComboBox.getItems().addAll(
						FXCollections
								.observableArrayList((List<VisitorBean>) (t
										.getSource().getValue())));
				toggleProgressIndicator(false);

			}
		});
		service.start();
	}

	/**
	 * Load the country combobox asynchronously. 
	 */
	private void populateCountry()  {
		logger.log(Level.INFO,"Populating the list of countries");
		toggleProgressIndicator(true);
		clearCountry();
		Service<List<CountryBean>> service = new Service<List<CountryBean>>() {

			@Override
			protected Task<List<CountryBean>> createTask() {
				return new Task<List<CountryBean>>() {

					@Override
					protected List<CountryBean> call() throws Exception {
						return visitorClient.getCountryList();
					}

				};
			}
		};
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			public void handle(WorkerStateEvent t) {
				logger.log(Level.INFO,"Country List get populated successfully");
				countryComboBox.getItems().addAll(
						FXCollections
								.observableArrayList((List<CountryBean>) (t
										.getSource().getValue())));
						toggleProgressIndicator(false);

			}
		});
		service.start();
	}

	/**
	 * Load the Visitor Info asynchronously. It will get called onchange event of visitor ComboBox
	 */
	@FXML
	private void populateVisitorInfo() {
		logger.log(Level.INFO,"Populating the Visitor Info");
		toggleProgressIndicator(true);
		clearVisitorInfo();
		final String href = visitorComboBox.getSelectionModel().getSelectedItem().getHref();
		Service<VisitorInfoBean> service = new Service<VisitorInfoBean>() {

			@Override
			protected Task<VisitorInfoBean> createTask() {
				return new Task<VisitorInfoBean>() {

					@Override
					protected VisitorInfoBean call() throws Exception {
						return visitorClient.getVisitorInfo(href);
					}

				};
			}
		};
		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			public void handle(WorkerStateEvent t) {
				logger.log(Level.INFO,"Visitor Info get populated successfully");
				VisitorInfoBean visitorInfoBean = (VisitorInfoBean) (t
						.getSource().getValue());
				nameText.setText(visitorInfoBean.getName());
				genderText.setText(visitorInfoBean.getGender());
				for (CountryBean country : visitorInfoBean.getCountries()) {
					Label countries = new Label(country.getValue());
					countries.getStyleClass().add("countries");
					countriesVisitedPane.getChildren().add(countries);
					visitorInfoPane.setVisible(true);
					toggleProgressIndicator(false);
				}
			}
		});
		service.start();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			populateCountry();
	}
	
	
}
