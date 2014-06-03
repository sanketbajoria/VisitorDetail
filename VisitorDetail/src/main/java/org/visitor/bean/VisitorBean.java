/**
 * This class represents the backing bean for visitor
 */
package org.visitor.bean;

public class VisitorBean {
	private String href;
	private String value;
	
	public VisitorBean(String href,String value){
		this.href = href;
		this.value = value;
	}
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString(){
		return this.value;
	}
}
