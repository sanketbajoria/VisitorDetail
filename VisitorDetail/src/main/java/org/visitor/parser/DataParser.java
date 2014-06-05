package org.visitor.parser;

import java.util.List;

import org.visitor.bean.CountryBean;
import org.visitor.bean.VisitorBean;
import org.visitor.bean.VisitorInfoBean;
import org.visitor.exception.DataFetchException;

public interface DataParser {
	
	 /** Parse the country list
	 * @return
	 * @throws DataFetchException
	 */
	public List<CountryBean> parseCountryList(String s) throws DataFetchException;
	
	/**
	 * Parse the visitor list for the particular country
	 * @param countryURL
	 * @return
	 * @throws DataFetchException
	 */
	public List<VisitorBean> parseVisitorList(String s) throws DataFetchException;
	/**
	 * Parse the visitor detail information for particular visitor
	 * @param visitorURL
	 * @return
	 * @throws DataFetchException
	 */
	public VisitorInfoBean parseVisitorInfo(String s) throws DataFetchException;

}
