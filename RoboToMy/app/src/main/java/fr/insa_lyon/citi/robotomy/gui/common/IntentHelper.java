package fr.insa_lyon.citi.robotomy.gui.common;


import android.content.Intent;

import java.io.Serializable;

import fr.insa_lyon.citi.robotomy.tools.Constants;
import fr.insa_lyon.citi.robotomy.tools.RoboToMyContext;

public class IntentHelper {


	public static void openNewActivity(Class cls, Serializable params,boolean forceExit) {
	
		Intent intent = new Intent(RoboToMyContext.getContext(), cls);
		intent.putExtra(Constants.PARAMNAME,params);
        RoboToMyContext.getContext().startActivity(intent);
	}
	
	

	public static <T> T getActiveIntentParam(Class<T> clazz) {
		Object param = RoboToMyContext.getActiveActivity().getIntent().getExtras().get(Constants.PARAMNAME);
		return clazz.cast(param);
	}

}
