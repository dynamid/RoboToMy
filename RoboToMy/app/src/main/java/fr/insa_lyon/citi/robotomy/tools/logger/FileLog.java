package fr.insa_lyon.citi.robotomy.tools.logger;

import java.io.IOException;
import java.util.logging.Level;

import android.content.Context;

import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.tools.Constants;
import fr.insa_lyon.citi.robotomy.tools.RoboToMyContext;


/**
 * @author A510944
 * Implemenation of AbstractLogger
 */
public class FileLog implements AbstractLogger {

	/* (non-Javadoc)
	 * @see eu.chitic.syp.tools.logger.AbstractLogger#write(java.lang.String, java.util.logging.Level)
	 */
	public void write(String message, Level level) {
		try {
			FileLogger file = new FileLogger();
			file.open();
			file.setAppend(true);
			file.setFormatter(new SimpleFormatter());
			file.doLog(Constants.TAG, level, message);
			file.close();

		} catch (IOException e) {

			logStackTrace(e);
		}
	}

	public void logStackTrace(Exception e) {

		Context context = RoboToMyContext.getContext();
		if (context.getString(R.string.prop_logging).equals("console")
				|| context.getString(R.string.prop_logging).equals("file")) {
			e.printStackTrace();
		}
	}

}
