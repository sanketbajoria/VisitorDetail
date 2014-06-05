/**
 * @author Sanket Bajoria
 * This client provide the 
 *
 */

package org.visitor.client;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.visitor.bean.CountryBean;
import org.visitor.bean.VisitorBean;
import org.visitor.bean.VisitorInfoBean;
import org.visitor.core.VisitorInfo;
import org.visitor.core.VisitorsInfo;
import org.visitor.exception.DataFetchException;
import org.visitor.parser.DataParser;

import com.google.inject.Inject;

public class VisitorClientImpl implements VisitorClient {

	private final static Logger logger = Logger
			.getLogger(VisitorClientImpl.class.getName());

	public static final String COUNTRIES_URL = "http://www.thomas-bayer.com/restnames/countries.groovy";
	public static final String VISITORS_URL = "http://www.thomas-bayer.com:80/restnames/namesincountry.groovy";
	public static final String VISITOR_INFO_URL = "http://www.thomas-bayer.com:80/restnames/name.groovy";

	@Inject
	private DataParser dataParser;

	@Override
	public List<CountryBean> getCountryList() throws DataFetchException {
		String xmlStr = fetchData(COUNTRIES_URL);
		return dataParser.parseCountryList(xmlStr);

	}

	@Override
	public List<VisitorBean> getVisitorList(String countryURL)
			throws DataFetchException {
		String xmlStr = fetchData(countryURL);
		return dataParser.parseVisitorList(xmlStr);
	}

	@Override
	public VisitorInfoBean getVisitorInfo(String visitorURL)
			throws DataFetchException {
		String xmlStr = fetchData(visitorURL);
		return dataParser.parseVisitorInfo(xmlStr);
	}

	/**
	 * Fetch the data restfully from particular URL
	 * 
	 * @param url
	 * @return
	 * @throws DataFetchException
	 */
	private String fetchData(String url) throws DataFetchException {
		logger.log(Level.INFO, "Fetch the data from URL - " + url);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(encodeURL(url));
		try {
			Response response = target.request(MediaType.APPLICATION_XML).get();
			logger.log(Level.INFO,
					"HTTP Response Status Code - " + response.getStatus());
			if (response.getStatus() != 200) {
				throw new DataFetchException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			String rawData = response.readEntity(String.class);
			logger.log(Level.INFO, rawData);
			return rawData;
		} catch (ProcessingException processingException) {
			logger.log(Level.SEVERE, processingException.getMessage());
			throw new DataFetchException("Unable to establish connection - "
					+ processingException.getCause().getMessage());
		}
	}

	/**
	 * Main class written for testing purpose
	 * 
	 * @param args
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) {
		VisitorClientImpl visitorClient = new VisitorClientImpl();
		try {
			List<CountryBean> countryList = visitorClient.getCountryList();
			List<VisitorBean> visitorList = visitorClient
					.getVisitorList("http://www.thomas-bayer.com:80/restnames/namesincountry.groovy?country=U.S.A.89454");
			VisitorInfoBean visitorInfo = visitorClient
					.getVisitorInfo("http://www.thomas-bayer.com:80/restnames/name.groovy?name=Lorraineasdfs ");
			System.out.println(countryList + " " + visitorInfo + " "
					+ visitorList);
		} catch (DataFetchException ex) {
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * Encode the url string into http url format.
	 * 
	 * @param urlStr
	 * @return
	 * @throws DataFetchException
	 */
	private String encodeURL(String urlStr) throws DataFetchException {
		try {
			logger.log(Level.INFO, "To encode string - " + urlStr);
			URL url = new URL(urlStr);
			URI uri = new URI(url.getProtocol(), url.getUserInfo(),
					url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef());
			url = uri.toURL();
			logger.log(Level.INFO, "After Encoding - " + url.toString());
			return url.toString();
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.getMessage());
			throw new DataFetchException("Malformed URL is being used");
		} catch (URISyntaxException e) {
			logger.log(Level.SEVERE, e.getMessage());
			throw new DataFetchException("Malformed URL is being used");
		}
	}
}
