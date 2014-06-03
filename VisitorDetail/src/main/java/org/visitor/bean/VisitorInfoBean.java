/**
 * This Class represent the backing bean for visitor information
 * 
 */

package org.visitor.bean;

import java.util.ArrayList;
import java.util.List;

public class VisitorInfoBean {
	private String gender;
	private String name;
	private List<CountryBean> countries;
	
	public VisitorInfoBean(){
		this.gender = "";
		this.name = "";
		this.countries = new ArrayList<CountryBean>();
	}
	
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		if(gender != null){
			this.gender = gender;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name != null){
			this.name = name;
		}
	}
	public List<CountryBean> getCountries() {
		return countries;
	}
	public void setCountries(List<CountryBean> countries) {
		if(countries != null){
			this.countries = countries;
		}
	}
	
}
