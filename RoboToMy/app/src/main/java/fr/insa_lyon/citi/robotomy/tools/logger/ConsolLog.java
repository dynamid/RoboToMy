package fr.insa_lyon.citi.robotomy.tools.logger;

import java.util.logging.Level;

import android.content.Context;
import android.util.Log;

import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.tools.Constants;
import fr.insa_lyon.citi.robotomy.tools.RoboToMyContext;

/**
 * @author Stefan
 * Implements consol loggin
 */
public class ConsolLog implements AbstractLogger {

	public void write(String message, Level level) {
		// Logcat
		if (level.intValue() == Level.WARNING.intValue()) {
			Log.w(Constants.TAG, message);
		}
		if (level.intValue() == Level.SEVERE.intValue()) {
			Log.e(Constants.TAG, message);
		}
		if (level.intValue() == Level.INFO.intValue()) {
			Log.i(Constants.TAG, message);
		} else {
			// Debug log
			Log.d(Constants.TAG, message);
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
