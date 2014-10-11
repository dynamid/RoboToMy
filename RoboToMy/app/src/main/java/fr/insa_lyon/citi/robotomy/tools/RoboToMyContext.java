package  fr.insa_lyon.citi.robotomy.tools;

import java.util.logging.Level;

import android.content.Context;
import fr.insa_lyon.citi.robotomy.gui.common.BaseActivity;

public abstract class RoboToMyContext {

	private static Context context = null;
	private static BaseActivity activeActivity = null;


	public static void setContext(Context context) {

		if (RoboToMyContext.context == null) {
			AssertUtils.notNull(context, "context is null");
			RoboToMyContext.context = context;
		}
	}

	public static Context getContext() {

		return context;
	}


	public static void setActiveActivity(BaseActivity activity) {
		AssertUtils.notNull(activity, "activity is null");
		if (activeActivity != activity) {
			AppTools.log("Application's active activity has been set", Level.INFO);
			RoboToMyContext.activeActivity = activity;
		}
	}


	public static BaseActivity getActiveActivity() {
		return activeActivity;
	}

	public static String getStringResource(int resId) {
		return context.getString(resId);
	}


	public static boolean getBooleanResource(int resId) {
		return context.getResources().getBoolean(resId);
	}

	public static int getIntegerResource(int resId) {
		return context.getResources().getInteger(resId);
	}

}
