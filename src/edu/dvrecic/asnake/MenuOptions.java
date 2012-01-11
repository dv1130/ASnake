package edu.dvrecic.asnake;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class MenuOptions  extends PreferenceActivity {
	public static final String TAG = "MenuOptions";
	ApplicationASnake app;
	public static final String PREF_IME = "PREF_IME";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (ApplicationASnake) getApplication();
		addPreferencesFromResource(R.xml.menu_options);
	}
	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences settings =  PreferenceManager.getDefaultSharedPreferences(app); 
		app.setName(settings.getString(PREF_IME," "));
	}
}