package fr.insa_lyon.citi.robotomy.tools;

import java.util.logging.Level;

import android.content.Context;

import  fr.insa_lyon.citi.robotomy.R;
import  fr.insa_lyon.citi.robotomy.tools.logger.AbstractLogger;
import  fr.insa_lyon.citi.robotomy.tools.logger.ConsolLog;
import  fr.insa_lyon.citi.robotomy.tools.logger.FileLog;

public abstract class AppTools {

	public static final String PREFS_NAME = "RoboToMy";
	private static AbstractLogger myLog;


	public static void info(String message) {
		log(message, Level.INFO);
	}

	public static void debug(String message) {
		log(message, Level.FINEST);
	}

	public static void warn(String message) {
		log(message, Level.WARNING);
	}

	public static void error(String message) {
		log(message, Level.SEVERE);
	}

	public static void logTest(String message) {
		log("[TEST]" + message, Level.INFO);
	}

	public static void log(String message, Level level) {
		Context context = RoboToMyContext.getContext();
		if (myLog == null && context != null) {
			if (context.getString(R.string.prop_logging).equals("console")) {
				// Console logcat logging
				myLog = new ConsolLog();
			} else if (context.getString(R.string.prop_logging).equals("file")) {
				// File logging
				myLog = new FileLog();
			} else if ((context.getString(R.string.prop_logging).equals("no"))) {
				// no logging (production configuration)
				return;
			}
		}
		String messageLog = message;
		if (RoboToMyContext.getActiveActivity() != null) {
			messageLog = "["
					+ RoboToMyContext.getActiveActivity().getClass().getSimpleName()
					+ "]" + message;
		}

		myLog.write(messageLog, level);
	}
}
