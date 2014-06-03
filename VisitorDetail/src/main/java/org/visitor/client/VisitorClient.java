package org.visitor.client;

import java.util.List;

import org.visitor.bean.CountryBean;
import org.visitor.bean.VisitorBean;
import org.visitor.bean.VisitorInfoBean;
import org.visitor.exception.DataFetchException;

/**
 * @author Sanket
 *
 */
public interface VisitorClient {
	/**
	 * Fetch the country list
	 * @return
	 * @throws DataFetchException
	 */
	public List<CountryBean> getCountryList() throws DataFetchException;
	
	/**
	 * Fetch the visitor list for the particular country
	 * @param countryURL
	 * @return
	 * @throws DataFetchException
	 */
	public List<VisitorBean> getVisitorList(String countryURL) throws DataFetchException;
	/**
	 * Fetch the visitor detail information for particular visitor
	 * @param visitorURL
	 * @return
	 * @throws DataFetchException
	 */
	public VisitorInfoBean getVisitorInfo(String visitorURL) throws DataFetchException;
	
	
}
