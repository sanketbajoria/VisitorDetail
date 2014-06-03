/**
 * This class used to load the FXML view file and set the specific controller.
 */
package org.visitor.guice;

import java.io.InputStream;
import java.net.URL;

import javafx.fxml.FXMLLoader;

import com.google.inject.Injector;

/**
 * Uses Guice to inject model state. Basically you create an instance of
 * GuiceFXMLLoader supplying an Injector, and then call load. The load
 * method takes the FXML file to load, and the controller to create and
 * associate with the FXML file.
 */
public class GuiceFXMLLoader {
    private final Injector injector;
     
    public GuiceFXMLLoader(Injector injector) {
        this.injector = injector;
    }
     
    // Load some FXML file, using the supplied Controller, and return the
    // instance of the initialized controller...?
    public Object load(String url, Class<?> controller) {
        Object instance = injector.getInstance(controller);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(instance);
        InputStream in = null;
        try {
            try {
                URL u = new URL(url);
                in = u.openStream();
            } catch (Exception e) {
                in = controller.getResourceAsStream(url);
            }
            return loader.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) try { in.close(); } catch (Exception ee) { }
        }
        return null;
    }
}