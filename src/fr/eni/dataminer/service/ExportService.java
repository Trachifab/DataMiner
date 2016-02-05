package fr.eni.dataminer.service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;

import fr.eni.dataminer.model.Entity;
import fr.eni.dataminer.service.tools.LoggingEngine;

public class ExportService {

	private static final LoggingEngine loggerEngine = LoggingEngine
			.getInstance();

	private static final String CSV_SEPARATOR = ";";

	private static final List<Entity> entityList = EntityService.getInstance()
			.getEntityList();

	/**
	 * Export all Entity and its personnes in a CSV file
	 * @throws IOException
	 */
	public static void exportCSV() throws IOException{
		loggerEngine.logger.log(Level.INFO, "Starting CSV export");

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("data.csv"), "UTF-8"));

		StringBuilder headers = new StringBuilder();
		headers.append("HOST");
		headers.append(CSV_SEPARATOR);
		headers.append("URL");
		headers.append(CSV_SEPARATOR);
		headers.append("TYPE");
		headers.append(CSV_SEPARATOR);
		headers.append("DATA");

		bw.write(headers.toString());
		bw.newLine();

		for (int i = 0; i < entityList.size(); i++) {

			StringBuilder line = new StringBuilder();

			line.append(entityList.get(i)
					.getFromSite(entityList.get(i).getFromURL()).trim()
					.length() == 0 ? "" : entityList.get(i).getFromSite(
					entityList.get(i).getFromURL()).toString());
			line.append(CSV_SEPARATOR);
			line.append((entityList).get(i).getFromURL().trim().length() == 0 ? ""
					: (entityList).get(i).getFromURL().toString());
			line.append(CSV_SEPARATOR);
			line.append((entityList).get(i).getDataType() == null ? ""
					: (entityList).get(i).getDataType().toString());
			line.append(CSV_SEPARATOR);
			line.append((entityList).get(i).getData().trim().length() == 0 ? ""
					: ((entityList).get(i).getData().toString()));
			line.append(CSV_SEPARATOR);

			bw.write(line.toString());

			bw.newLine();
		}

		bw.flush();
		bw.close();
		loggerEngine.logger.log(Level.INFO, "Finishing CSV export");
	}
}
