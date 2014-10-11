package fr.insa_lyon.citi.robotomy.gui.main;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


import fr.insa_lyon.citi.robotomy.R;
import fr.insa_lyon.citi.robotomy.gui.common.BaseActivity;
import fr.insa_lyon.citi.robotomy.tools.AppTools;
import fr.insa_lyon.citi.robotomy.tools.Constants;

public class MainActivity extends BaseActivity {
    private LinearLayout abstractView;
    private LinearLayout mainView;
    private TextView windowTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, Constants.MAIN_CONST);
        AppTools.info("on create MainActivity");
        initGraphicalInterface();
    }

    private void initGraphicalInterface() {
        // set layouts
        LayoutInflater mInflater = LayoutInflater.from(this);
        abstractView = (LinearLayout) findViewById(R.id.abstractLinearLayout);
        mainView = (LinearLayout) mInflater.inflate(R.layout.activity_main, null);
        abstractView.addView(mainView);


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

