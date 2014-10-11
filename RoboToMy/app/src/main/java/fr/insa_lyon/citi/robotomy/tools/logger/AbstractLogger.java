package fr.insa_lyon.citi.robotomy.tools.logger;

import java.util.logging.Level;


/**
 * @author A510944
 *
 */
public interface AbstractLogger {

	/**
	 * Writes to logcat or file depending on the implementation
	 * @param message
	 * 		to log
	 * @param level
	 * 		of the gravity of the message
	 */
	public void write(String message, Level level);
	/**
	 * prints the stackTrace to console
	 * @param e
	 */
	public void logStackTrace(Exception e);
	
}
