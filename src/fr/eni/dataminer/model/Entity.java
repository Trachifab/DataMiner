package fr.eni.dataminer.model;

import java.net.MalformedURLException;
import java.net.URL;

public class Entity {

	private String fromURL = null;
	private DataType dataType = null;
	private String data = null;
	private EntityParser entityParser = null;
	
	private String domain = null;
	
	public Entity(String fromURL, DataType dataType, String data) throws MalformedURLException {
		this.fromURL = fromURL;
		this.dataType = dataType;
		this.data = data;
		this.domain = getFromSite(fromURL);
	}
	

	public Entity() {

	}

	public String getFromSite(String fromURL) throws MalformedURLException{
		URL url = new URL(fromURL);
		return url.getHost();
	}

	public String getFromURL() {
		return fromURL;
	}

	public void setFromURL(String fromURL) {
		this.fromURL = fromURL;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	

	
}
