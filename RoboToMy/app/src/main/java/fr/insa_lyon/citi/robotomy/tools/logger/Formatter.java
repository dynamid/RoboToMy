package fr.insa_lyon.citi.robotomy.tools.logger;

import java.util.Date;
import java.util.logging.Level;

public interface Formatter {

	/**
	 * Format a log line
	 * 
	 * @param tag
	 * @param time
	 * @param level
	 * @param message
	 * @return
	 */
	public String format(String tag, Date time, Level level, String message);
}