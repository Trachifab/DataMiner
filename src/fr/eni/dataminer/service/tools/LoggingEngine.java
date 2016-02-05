package fr.eni.dataminer.service.tools;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingEngine {

	public final static Logger logger = Logger.getLogger("AppLogger");
	 
	private static LoggingEngine instance = null;
	   
	/**
	 * Singleton pattern
	 * @return
	 */
	public static LoggingEngine getInstance(){
		if(instance == null) {
			try {
				initLogger();
			} catch (SecurityException | IOException e) {
				logger.log(Level.SEVERE, "Cannot get instance of LoggingEngine :" + e.getMessage());
			}
			instance = new LoggingEngine ();
		}
		return instance;
	}
	  
	/**
	 * Initalize the logger
	 * @throws SecurityException
	 * @throws IOException
	 */
	private static void initLogger() throws SecurityException, IOException {
			FileHandler fh = new FileHandler("logs.log");
	        fh.setFormatter(new SimpleFormatter());
	        logger.addHandler(fh);
	        logger.setUseParentHandlers(false);
	        fh.setLevel(Level.ALL);
	}
}
