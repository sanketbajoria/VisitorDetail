package org.junit.visitor.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.visitor.bean.CountryBean;
import org.visitor.bean.VisitorBean;
import org.visitor.bean.VisitorInfoBean;
import org.visitor.client.VisitorClient;
import org.visitor.client.VisitorClientImpl;
import org.visitor.exception.DataFetchException;

public class VisitorClientImplTest {
	private static VisitorClient visitorClient;

	@BeforeClass
	public static void testSetup() {
		visitorClient = new VisitorClientImpl();
	}

	@AfterClass
	public static void testCleanup() {
		visitorClient = null;
	}

	@Test(expected = DataFetchException.class)
	public void testGetVisitorListWithMalformedURL() throws DataFetchException {
		visitorClient.getVisitorList("");
	}

	@Test
	public void testGetVisitorListWithURL() throws DataFetchException {
		List<VisitorBean> visitorList = visitorClient
				.getVisitorList("http://www.thomas-bayer.com:80/restnames/namesincountry.groovy?country=U.S.A.");
		assertTrue(visitorList.size() > 0);
	}

	@Test
	public void testGetVisitorListWithIncorrectCountry() throws DataFetchException {
		List<VisitorBean> visitorList = visitorClient
				.getVisitorList("http://www.thomas-bayer.com:80/restnames/namesincountry.groovy?country=abc");
		assertNotNull(visitorList);
	}

	@Test(expected = DataFetchException.class)
	public void testGetVisitorInfoWithMalformedURL() throws DataFetchException {
		visitorClient.getVisitorInfo("It will break down");
	}

	@Test
	public void testGetVisitorInfoWithURL() throws DataFetchException {
		VisitorInfoBean visitorInfo =  visitorClient
				.getVisitorInfo("http://www.thomas-bayer.com:80/restnames/name.groovy?name=Lorraine");
		assertNotNull(visitorInfo);
		assertTrue(visitorInfo.getCountries().size()>0);
		assertTrue(!visitorInfo.getName().equalsIgnoreCase(""));
		assertTrue(!visitorInfo.getGender().equalsIgnoreCase(""));
	}

	@Test
	public void testGetVisitorInfoWithIncorrectVisitor() throws DataFetchException {
		VisitorInfoBean visitorInfo = visitorClient
				.getVisitorInfo("http://www.thomas-bayer.com:80/restnames/name.groovy?name=Sanket Bajoria");
		assertNotNull(visitorInfo);
		assertTrue(visitorInfo.getCountries().size()==0);
		assertTrue(visitorInfo.getName().equalsIgnoreCase(""));
		assertTrue(visitorInfo.getGender().equalsIgnoreCase(""));
	} 
	
	@Test
	public void testGetCountryList() throws DataFetchException {
		List<CountryBean> countryList = visitorClient.getCountryList();
		assertNotNull(countryList);
		assertTrue(countryList.size() > 0);
	}

}
