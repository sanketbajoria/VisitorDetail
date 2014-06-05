/**
 * This class used to bind the dependency via Google guice
 */
package org.visitor.guice;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.visitor.client.VisitorClient;
import org.visitor.client.VisitorClientImpl;
import org.visitor.parser.DataParser;
import org.visitor.parser.XMLDataParser;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class VisitorModule extends AbstractModule {

	private final static Logger logger = Logger.getLogger(VisitorModule.class.getName());
	
	 @Override
	    protected void configure() {
		 
			Properties configProperty = loadProperties("config.properties");
			Names.bindProperties(binder(),configProperty);
			//bind(VisitorClient.class).to(VisitorClientMockUpImpl.class);
		 	bind(VisitorClient.class).to(VisitorClientImpl.class);
		 	bind(DataParser.class).to(XMLDataParser.class);
	    }
	 
	private static Properties loadProperties(String path){
		Properties properties = new Properties();
		ClassLoader loader = VisitorModule.class.getClassLoader();
		URL url = loader.getResource(path);
		try {
			properties.load(url.openStream());
			logger.log(Level.INFO, "Loaded property file successfully - " + path);
		} catch (IOException e) {
			logger.log(Level.SEVERE,"Error while loading the property file - " + path);
			e.printStackTrace();
		}
		return properties;
	}

}
