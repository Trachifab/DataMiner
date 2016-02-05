package fr.eni.dataminer.model;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import fr.eni.dataminer.exceptions.NoDocumentToAnalyzeException;
import fr.eni.dataminer.service.tools.LoggingEngine;

public class EntityParser {

	private String REGEX = null;
	private Document htmlDocument;
	private DataType dataType;
	private String searchString;

	private static final LoggingEngine loggerEngine = LoggingEngine
			.getInstance();
	
	public EntityParser(DataType type) {
		this.dataType = type;
		switch (type) {

		case PHONE_NUMBER:

			break;

		case EMAIL:
			this.REGEX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
			break;

		case TEXT:

			break;

		case IMG:

			break;
		}
	}

	/**
	 * Parse source code with regex
	 * @param currentUrl 
	 * 
	 * @param source
	 * @return
	 * @throws MalformedURLException 
	 */
	public List<Entity> searchForDataType(String currentUrl) throws MalformedURLException {
		loggerEngine.logger.log(Level.INFO, "Searching for data from "+currentUrl);
		List<Entity> foundEntities = new ArrayList<Entity>();
		Matcher matcher = Pattern.compile(REGEX).matcher(
				this.htmlDocument.body().text());
		while (matcher.find()) {
			foundEntities.add(new Entity(currentUrl,this.dataType, matcher.group()));
		}
		loggerEngine.logger.log(Level.INFO, "Returnin results");
		return foundEntities;
	}

	/**
	 * 
	 * @param searchWord
	 * @return
	 * @throws MalformedURLException 
	 * @throws NoDocumentToAnalyzeException 
	 */
	public List<Entity> searchForString(String currentUrl) throws MalformedURLException, NoDocumentToAnalyzeException {
		List<Entity> foundEntities = new ArrayList<Entity>();
		if (this.htmlDocument == null) {
			loggerEngine.logger.log(Level.SEVERE, "Document is null");
			throw new NoDocumentToAnalyzeException();
		}
		String bodyText = this.htmlDocument.body().text();
		if(bodyText.toLowerCase().contains(this.searchString.toLowerCase())){
			loggerEngine.logger.log(Level.INFO, "Entity found");
			foundEntities.add(new Entity(currentUrl,this.dataType, searchString));
		};
		loggerEngine.logger.log(Level.INFO, "Returning results");
		return foundEntities;
	}

	public String getREGEX() {
		return REGEX;
	}

	public void setREGEX(String rEGEX) {
		REGEX = rEGEX;
	}

	public Document getHtmlDocument() {
		return htmlDocument;
	}

	public void setHtmlDocument(Document htmlDocument) {
		this.htmlDocument = htmlDocument;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

}
