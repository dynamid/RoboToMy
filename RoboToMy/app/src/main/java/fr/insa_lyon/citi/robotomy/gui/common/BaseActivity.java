package fr.insa_lyon.citi.robotomy.gui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.util.logging.Level;

import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.tools.AppTools;
import fr.insa_lyon.citi.robotomy.tools.RoboToMyContext;

public class BaseActivity extends Activity {

	protected static final int ABSTRACT_CONST = 1000;

	private static final String PAUSED_STATE = "PAUSED_STATE";
	private static final String STARTING_ACTIVITY_STATE = "STARTING_ACTIVITY_STATE";

	protected int ACTIVITY_CONST = 1000;
	protected boolean paused;
	protected boolean startingActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		onCreate(savedInstanceState, ABSTRACT_CONST);
	}

	public void onCreate(Bundle savedInstanceState, int activityId) {
		super.onCreate(savedInstanceState);

		// set activity ID
		ACTIVITY_CONST = activityId;

		RoboToMyContext.setContext(this);

		AppTools.log(this.getClass().getSimpleName() + " is creating",
				Level.INFO);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.abstract_activity);

		if (savedInstanceState != null) {
			paused = savedInstanceState.getBoolean(PAUSED_STATE, false);
			startingActivity = savedInstanceState.getBoolean(
					STARTING_ACTIVITY_STATE, false);

			AppTools.log(this.getClass().getSimpleName()
					+ " is load from saved state with variables:", Level.INFO);

			AppTools.log(this.getClass().getSimpleName() + " : paused = "
					+ paused, Level.INFO);

			AppTools.log(this.getClass().getSimpleName()
					+ " : startingActivity = " + startingActivity, Level.INFO);
		} else {
			paused = false;
			startingActivity = false;
		}

		RoboToMyContext.setActiveActivity(this);

	}
	


	@Override
	public void onRestart() {
		super.onRestart();

		AppTools.log(this.getClass().getSimpleName() + " is restarting",
                Level.INFO);

	}

	@Override
	protected void onStart() {
		super.onStart();

		AppTools.log(this.getClass().getSimpleName() + " is starting",
                Level.INFO);
		RoboToMyContext.setActiveActivity(this);

	}

	@Override
	protected void onResume() {
		super.onResume();

		AppTools.log(this.getClass().getSimpleName() + " is resuming",
				Level.INFO);

		startingActivity = false;
		if (paused) {
			AppTools.log(this.getClass().getSimpleName()
					+ " has been resumed from a paused state", Level.INFO);
			paused = false;
			startupProcess();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		AppTools.log(this.getClass().getSimpleName() + " is restoring bundle",
				Level.INFO);

	}

	@Override
	protected void onStop() {
		super.onStop();

		AppTools.log(this.getClass().getSimpleName() + " is stopping...",
                Level.INFO);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppTools.log(this.getClass().getSimpleName() + " is dying...",
				Level.INFO);

		System.gc();

	}


	protected void startupProcess() {
		AppTools.debug(this.getClass().getSimpleName()
				+ " is launching startup process (Check appVersion / get IS / Statistics / Enrollment)");
	}


	@Override
	public void startActivity(Intent intent) {
		startingActivity = true;

		AppTools.log(this.getClass().getSimpleName() + " starts an activity",
				Level.INFO);
		super.startActivity(intent);
	}


	@Override
	public void onLowMemory() {
		super.onLowMemory();
		AppTools.info("*** LOW MEMORY ***");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
