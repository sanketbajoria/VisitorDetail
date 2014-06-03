/**
 * This class is the starting point of visitor app.
 */
package org.visitor.app;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.visitor.controller.VisitorController;
import org.visitor.guice.GuiceFXMLLoader;
import org.visitor.guice.VisitorModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class VisitorApp2 extends Application {

	/**
	 * Used to launch this as simple Java application
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
	 /**
     * Create the Guice Injector, which is going to be used to supply
     * the Person model.
     */
    private final Injector injector = Guice.createInjector(new VisitorModule());
	
	/*
	 * This is the starting point for JavaFX appication (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
    
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Create a new Guice-based FXML Loader
        GuiceFXMLLoader loader = new GuiceFXMLLoader(injector);
        // Ask to load the Sample.fxml file, injecting an instance of a SampleController
        Parent root = (Parent) loader.load("/visitor2.fxml", VisitorController.class);
         
        // Finish constructing the scene
        final Scene scene = new Scene(root, 400, 380);
        // Load up the CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/visitor.css").toString());
        // Show the window
        primaryStage.setScene(scene);
        primaryStage.setTitle("Visitor Details");
        primaryStage.setResizable(false);
    	primaryStage.show();
	}

}
