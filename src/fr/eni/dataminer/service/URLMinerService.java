package fr.eni.dataminer.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import fr.eni.dataminer.exceptions.NoDocumentToAnalyzeException;
import fr.eni.dataminer.model.DataType;
import fr.eni.dataminer.model.Entity;
import fr.eni.dataminer.model.EntityParser;
import fr.eni.dataminer.service.tools.LoggingEngine;

public class URLMinerService {

	private static int MAX_PAGES_TO_SEARCH;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	private static final LoggingEngine loggerEngine = LoggingEngine
			.getInstance();
	
	private static URLMinerService instance = null;

	/**
	 * Singleton pattern
	 * 
	 * @return
	 */
	public static URLMinerService getInstance() {
		if (instance == null) {
			instance = new URLMinerService();
		}
		return instance;
	}

	/**
	 * 
	 * @param url
	 * @param parser
	 * @return
	 */
	public List<Entity> search(String url, EntityParser parser) {

		boolean success = false;
		List<Entity> resultList = new ArrayList<Entity>();
		String currentUrl = null;

		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {

			URLAnalyserService URLanalyser = new URLAnalyserService();
			if (this.pagesToVisit.isEmpty()) {
				currentUrl = url;
				this.pagesVisited.add(url);
			} else {
				currentUrl = this.nextUrl();
			}
			URLanalyser.getAllLinks(currentUrl);
			parser.setHtmlDocument(URLanalyser.getHtmlDocument());
			if (parser.getHtmlDocument() != null) {
				if (parser.getDataType().equals(DataType.TEXT)) {
					try {
						resultList.addAll(parser.searchForString(currentUrl));
					} catch (MalformedURLException | NoDocumentToAnalyzeException e) {
						loggerEngine.logger.log(Level.SEVERE, "Error while searching for string entity :"+e.getMessage());
					}
				} else {
					try {
						resultList.addAll(parser.searchForDataType(currentUrl));
					} catch (MalformedURLException e) {
						loggerEngine.logger.log(Level.SEVERE, "Error while searching for entity :"+e.getMessage());
					}
				}

				this.pagesToVisit.addAll(URLanalyser.getLinks());
			}
		}
		return resultList;
	}

	/**
	 * Go to next url and remove the current from list
	 * 
	 * @return
	 */
	private String nextUrl() {
		String nextUrl;

		do {
			nextUrl = this.pagesToVisit.remove(0);
		} while (this.pagesVisited.contains(nextUrl));

		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}

	public static int getMAX_PAGES_TO_SEARCH() {
		return MAX_PAGES_TO_SEARCH;
	}

	public static void setMAX_PAGES_TO_SEARCH(int mAX_PAGES_TO_SEARCH) {
		MAX_PAGES_TO_SEARCH = mAX_PAGES_TO_SEARCH;
	}

	public Set<String> getPagesVisited() {
		return pagesVisited;
	}

	public void setPagesVisited(Set<String> pagesVisited) {
		this.pagesVisited = pagesVisited;
	}

	public List<String> getPagesToVisit() {
		return pagesToVisit;
	}

	public void setPagesToVisit(List<String> pagesToVisit) {
		this.pagesToVisit = pagesToVisit;
	}

}
