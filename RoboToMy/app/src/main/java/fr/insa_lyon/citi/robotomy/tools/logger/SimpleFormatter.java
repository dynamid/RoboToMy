package fr.insa_lyon.citi.robotomy.tools.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Simple implementation of the Formatter. Doing only the basic functions.
 * Basically this class is to set a uniform format to all log messages.
 * 
 * 
 */

public final class SimpleFormatter implements Formatter {

	private static SimpleDateFormat dateFormat;

	static {
		dateFormat = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss SSS");
	}

	public SimpleFormatter() {
		buffer = new StringBuffer(INITIAL_BUFFER_SIZE);
		delimiter = " - ";
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimeter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String format(String tag, Date time, Level level, String message) {
		if (buffer.length() > 0) {
			buffer.delete(0, buffer.length());
		}
		if (tag != null) {
			buffer.append('[');
			buffer.append(tag);
			buffer.append(']');
			buffer.append(' ');
		}
		buffer.append(dateFormat.format(time));
		buffer.append(':');
		if (level != null) {
			buffer.append('[');
			buffer.append(level);
			buffer.append(']');
		}
		if (message != null) {
			buffer.append(delimiter);
			buffer.append(message);
		}
		return buffer.toString();
	}

	public static final String DEFAULT_DELIMITER = "-";
	private static final int INITIAL_BUFFER_SIZE = 256;

	private StringBuffer buffer;
	private String delimiter;
	

}

