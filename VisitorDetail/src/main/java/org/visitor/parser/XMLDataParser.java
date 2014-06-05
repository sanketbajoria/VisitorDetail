package org.visitor.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.visitor.bean.CountryBean;
import org.visitor.bean.VisitorBean;
import org.visitor.bean.VisitorInfoBean;
import org.visitor.core.CountriesInfo;
import org.visitor.core.VisitorInfo;
import org.visitor.core.VisitorsInfo;
import org.visitor.exception.DataFetchException;

public class XMLDataParser implements DataParser {

	@Override
	public List<CountryBean> parseCountryList(String s)
			throws DataFetchException {
		try {
			List<CountryBean> countriesList = new ArrayList<CountryBean>();
			JAXBContext jaxbContext = JAXBContext
					.newInstance(CountriesInfo.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			CountriesInfo restnames = (CountriesInfo) jaxbUnmarshaller
					.unmarshal(new StreamSource(new StringReader(s.toString())));
			if (restnames != null && restnames.getCountries() != null
					&& restnames.getCountries().getCountry() != null) {
				List<CountriesInfo.Countries.Country> countryList = restnames
						.getCountries().getCountry();
				for (CountriesInfo.Countries.Country country : countryList) {
					if (country != null) {
						countriesList.add(new CountryBean(country.getHref(),
								country.getValue()));
					}
				}
			}
			return countriesList;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new DataFetchException("Invalid Data is being returned");
		}

	}

	@Override
	public List<VisitorBean> parseVisitorList(String s)
			throws DataFetchException {
		try {
			List<VisitorBean> visitorsList = new ArrayList<VisitorBean>();
			JAXBContext jaxbContext = JAXBContext
					.newInstance(VisitorsInfo.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			VisitorsInfo restnames = (VisitorsInfo) jaxbUnmarshaller
					.unmarshal(new StreamSource(new StringReader(s.toString())));
			if (restnames != null && restnames.getNameinfo() != null
					&& restnames.getNameinfo().getName() != null) {
				List<VisitorsInfo.Nameinfo.Name> visitorList = restnames
						.getNameinfo().getName();
				for (VisitorsInfo.Nameinfo.Name visitor : visitorList) {
					if (visitor != null) {
						visitorsList.add(new VisitorBean(visitor.getHref(),
								visitor.getValue()));
					}
				}
			}
			return visitorsList;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new DataFetchException("Invalid Data is being returned");
		}

	}

	@Override
	public VisitorInfoBean parseVisitorInfo(String s) throws DataFetchException {
		try {
			VisitorInfoBean visitorInfo = new VisitorInfoBean();
			JAXBContext jaxbContext = JAXBContext
					.newInstance(VisitorInfo.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			VisitorInfo restnames = (VisitorInfo) jaxbUnmarshaller
					.unmarshal(new StreamSource(new StringReader(s.toString())));
			if (restnames != null && restnames.getNameinfo() != null) {
				visitorInfo.setName(restnames.getNameinfo().getName());
				if ("true".equalsIgnoreCase(restnames.getNameinfo().getMale())) {
					visitorInfo.setGender("Male");
				} else if ("true".equalsIgnoreCase(restnames.getNameinfo()
						.getFemale())) {
					visitorInfo.setGender("Female");
				}
				if (restnames.getNameinfo().getCountries() != null
						&& restnames.getNameinfo().getCountries().getCountry() != null) {
					List<VisitorInfo.Nameinfo.Countries.Country> countryList = restnames
							.getNameinfo().getCountries().getCountry();
					for (VisitorInfo.Nameinfo.Countries.Country country : countryList) {
						if (country != null) {
							visitorInfo.getCountries().add(
									new CountryBean(country.getHref(), country
											.getValue()));
						}
					}
				}
			}
			return visitorInfo;
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new DataFetchException("Invalid Data is being returned");
		}
	}
}
