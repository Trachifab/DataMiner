package fr.eni.dataminer.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.eni.dataminer.exceptions.MalFormedProxyException;
import fr.eni.dataminer.model.Entity;
import fr.eni.dataminer.service.tools.LoggingEngine;

public class URLAnalyserService {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>();
	private Document htmlDocument;
	private String ipProxy;
	private String portProxy;

	private static URLAnalyserService instance = null;
	
	private static final LoggingEngine loggerEngine = LoggingEngine
			.getInstance();
	
	/**
	 * Singleton pattern
	 * @return
	 */
	public static URLAnalyserService getInstance(){
		if(instance == null) {
			instance = new URLAnalyserService ();
		}
		return instance;
	}
	
	/**
	 * Get all link from the page
	 * 
	 * @param url
	 * @return
	 */
	public boolean getAllLinks(String url) {
		loggerEngine.logger.log(Level.INFO, "Getting all links from url");
		try {
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;
			if (connection.response().statusCode() == 200) {

			}
			if (!connection.response().contentType().contains("text/html")) {

				return false;
			}
			Elements linksOnPage = htmlDocument.select("a[href]");
			for (Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}
			return true;
		} catch (IOException ioe) {
			return false;
		}
	}

	public List<String> getLinks() {
		return this.links;
	}

	public Document getHtmlDocument() {
		return htmlDocument;
	}

	public void setHtmlDocument(Document htmlDocument) {
		this.htmlDocument = htmlDocument;
	}

	public static String getUserAgent() {
		return USER_AGENT;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public void setProxy(String ip, String port) throws MalFormedProxyException {
		if(validateProxy(ip, port)){
			this.ipProxy = ip;
			this.portProxy = port;
			
			System.setProperty("http.proxyHost", this.ipProxy);
			System.setProperty("http.proxyPort", this.portProxy);
			System.setProperty("https.proxyHost", this.ipProxy);
			System.setProperty("https.proxyPort", this.portProxy);
		}else{
			throw new MalFormedProxyException();
		}
	}

	private boolean validateProxy(String ip, String port) {
		Matcher matcherIp = Pattern.compile("^(?:[0-9]{1,3}.){3}[0-9]{1,3}$").matcher(ip);
		if(!matcherIp.matches()){
			return false;
		}
		Matcher matcherPort = Pattern.compile("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$").matcher(port);
		if(!matcherPort.matches()){
			return false;
		}
		return true;
	}

}
