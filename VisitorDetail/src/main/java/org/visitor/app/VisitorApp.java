/**
 * This class is the starting point of visitor app.
 */
package org.visitor.app;

import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;

import org.visitor.bean.CountryBean;
import org.visitor.bean.VisitorBean;
import org.visitor.bean.VisitorInfoBean;
import org.visitor.client.VisitorClient;
import org.visitor.client.VisitorClientImpl;

public class VisitorApp extends Application {

	private VisitorClient visitorClient = new VisitorClientImpl();

	private final Text nameText = new Text();
	private final Text genderText = new Text();
	private final ProgressIndicator progressIndicator = new ProgressIndicator(
			ProgressIndicator.INDETERMINATE_PROGRESS);
	private final ComboBox<CountryBean> countryComboBox = new ComboBox<CountryBean>();
	private final ComboBox<VisitorBean> visitorComboBox = new ComboBox<VisitorBean>();
	private final FlowPane countriesVisitedPane = new FlowPane();
	private final StackPane visitorInfoPane = new StackPane();
	private final StackPane mainPane = new StackPane();
	private final VBox appPane = new VBox(); 
	private final VBox loadingPane = new VBox();
	/*
	 * Used to launch this as simple Java application
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/*
	 * Show/Hide the progress indicator
	 */
	public void toggleProgressIndicator(boolean show){
		if(show){
			appPane.setOpacity(30);
			appPane.setDisable(true);
			mainPane.getChildren().add(loadingPane);
		}else{
			appPane.setOpacity(100);
			appPane.setDisable(false);
			mainPane.getChildren().remove(loadingPane);
		}
	}
	

	/*
	 * Clear the country combobox
	 */
	private void clearCountry() {
		countryComboBox.getSelectionModel().clearSelection();
		countryComboBox.getItems().clear();
		clearVisitor();
	}

	/*
	 * Clear the visitor combobox
	 */
	private void clearVisitor() {
		visitorComboBox.getSelectionModel().clearSelection();
		visitorComboBox.getItems().clear();
		clearVisitorInfo();
	}

	/*
	 * Clear the visitor Info Section
	 */
	private void clearVisitorInfo() {
		visitorInfoPane.setVisible(false);
		countriesVisitedPane.getChildren().clear();
		nameText.setText("");
		genderText.setText("");

	}

	/*
	 * Load the visitor combobox asynchronously
	 */
	private void populateVisitor(final String href) throws JAXBException {
		toggleProgressIndicator(true);
		clearVisitor();
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
				visitorComboBox.getItems().addAll(
						FXCollections
								.observableArrayList((List<VisitorBean>) (t
										.getSource().getValue())));
				toggleProgressIndicator(false);

			}
		});
		service.start();
	}

	/*
	 * Load the country combobox asynchronously
	 */
	private void populateCountry() throws JAXBException {
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
				countryComboBox.getItems().addAll(
						FXCollections
								.observableArrayList((List<CountryBean>) (t
										.getSource().getValue())));
						toggleProgressIndicator(false);

			}
		});
		service.start();
	}

	/*
	 * Load the Visitor Info asynchronously
	 */
	private void populateVisitorInfo(final String href) throws JAXBException {
		toggleProgressIndicator(true);
		clearVisitorInfo();
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
				VisitorInfoBean visitorInfoBean = (VisitorInfoBean) (t
						.getSource().getValue());
				nameText.setText(visitorInfoBean.getName());
				genderText.setText(visitorInfoBean.getGender());
				for (CountryBean country : visitorInfoBean.getCountries()) {
					Label countries = new Label(country.getValue());
					countries
							.setStyle("-fx-background-color: green;-fx-text-fill: white;-fx-padding: 2;");
					countriesVisitedPane.getChildren().add(countries);
					visitorInfoPane.setVisible(true);
					toggleProgressIndicator(false);
				}
			}
		});
		service.start();
	}

	/*
	 * This is the starting point for JavaFX appication (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Visitor Details");
		Scene scene = new Scene(mainPane, 400, 350);
		mainPane.getChildren().add(appPane);
		
		progressIndicator.setMinWidth(50);
		progressIndicator.setMinHeight(50);
		loadingPane.setAlignment(Pos.CENTER);
		loadingPane.getChildren().add(progressIndicator);
		loadingPane.getChildren().add(new Text("Loading..."));
		

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label country = new Label("Country:");
		country.setMinWidth(130);
		grid.add(country, 0, 1);

		countryComboBox.setMinWidth(220);
		countryComboBox.setPrefWidth(220);
		grid.add(countryComboBox, 1, 1);

		Label visitor = new Label("Visitor:");
		visitor.setMinWidth(130);
		grid.add(visitor, 0, 2);

		visitorComboBox.setMinWidth(220);
		visitorComboBox.setPrefWidth(220);
		grid.add(visitorComboBox, 1, 2);

		
		Rectangle rect = new Rectangle(380, 220);
		rect.setStyle("-fx-stroke:black;-fx-stroke-width:1;-fx-fill:white");

		visitorInfoPane.getChildren().add(rect);
		GridPane visitorInfoGrid = new GridPane();
		visitorInfoGrid.setMinSize(350, 210);
		visitorInfoGrid.setHgap(10);
		visitorInfoGrid.setVgap(5);
		visitorInfoGrid.setPadding(new Insets(5, 25, 5, 25));

		Label name = new Label("Name:");
		name.setMinWidth(100);
		visitorInfoGrid.add(name, 0, 1);

		visitorInfoGrid.add(nameText, 1, 1);

		Label gender = new Label("Gender:");
		gender.setMinWidth(100);
		visitorInfoGrid.add(gender, 0, 2);

		visitorInfoGrid.add(genderText, 1, 2);

		Label countriesVisited = new Label("Countries Visited:");
		countriesVisited.setMinWidth(100);
		visitorInfoGrid.add(countriesVisited, 0, 3);
		countriesVisitedPane.setHgap(5);
		countriesVisitedPane.setVgap(5);
		visitorInfoGrid.add(countriesVisitedPane, 1, 3);

		visitorInfoPane.getChildren().add(visitorInfoGrid);

		appPane.getChildren().add(grid);
		appPane.getChildren().add(visitorInfoPane);
		
		populateCountry();

		countryComboBox.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<CountryBean>() {
					public void changed(
							ObservableValue<? extends CountryBean> arg0,
							CountryBean arg1, CountryBean arg2) {
						if (arg2 != null) {
							System.out.println("Selected Country: "
									+ arg2.getHref());
							try {
								populateVisitor(arg2.getHref());
							} catch (JAXBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});

		visitorComboBox.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<VisitorBean>() {
					public void changed(
							ObservableValue<? extends VisitorBean> arg0,
							VisitorBean arg1, VisitorBean arg2) {
						if (arg2 != null) {
							System.out.println("Selected visitor: "
									+ arg2.getHref());
							try {
								populateVisitorInfo(arg2.getHref());
							} catch (JAXBException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

		primaryStage.setResizable(false);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

}
