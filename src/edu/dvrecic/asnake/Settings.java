package edu.dvrecic.asnake;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Settings extends Activity implements OnClickListener{

	public static Typeface tf;
	private EditText name;
	boolean sensorOFF = false;
	private ToggleButton sensorsButton;
	public static final String PREF_IME = "PREF_IME";
	public static final String PREF_SENSORS = "PREF_SENSORS";
	ApplicationASnake app;
	SharedPreferences settings;
	private static final int ClEAN_DB_DIALOG=0; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		app = (ApplicationASnake) getApplication();
		settings =  PreferenceManager.getDefaultSharedPreferences(app);

		
		tf = Typeface.createFromAsset(getAssets(),"data/fonts/eye.TTF");
		TextView tv = (TextView) findViewById(R.id.textViewNamePlayer);
		tv.setTypeface(tf);
		TextView tv1 = (TextView) findViewById(R.id.textViewSensorsOFFON);
		tv1.setTypeface(tf);
		TextView tv2 = (TextView) findViewById(R.id.textViewBaza);
		tv2.setTypeface(tf);
		// text gumbek back
		TextView tv3 = (TextView) findViewById(R.id.textViewBack);
		tv3.setTypeface(tf);
		tv3.setOnTouchListener(new CustomTouchListener());   
		tv3.setOnClickListener(this);

		name = (EditText)findViewById(R.id.editText1);
		name.setText(settings.getString(PREF_IME, app.getName().toString()));		

		sensorsButton = (ToggleButton)findViewById(R.id.toggleButton1);
		sensorsButton.setOnClickListener(this);
		sensorsButton.setChecked(settings.getBoolean(PREF_SENSORS, app.isSensorOFF()));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.toggleButton1:
			if(sensorsButton.isChecked())
			{
				app.setSensorOFF(settings.getBoolean(PREF_SENSORS, true));
			} else{
				app.setSensorOFF(settings.getBoolean(PREF_SENSORS, false));
			}
			break;
		case R.id.buttonDeleteDB:
			showDialog(ClEAN_DB_DIALOG);
			break;
		case R.id.textViewBack:			
			app.setName(settings.getString(PREF_IME, name.getText().toString()));
			finish();
			break;
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		settings =  PreferenceManager.getDefaultSharedPreferences(app); 
		app.setName(settings.getString(PREF_IME, name.getText().toString()));
	}

	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case ClEAN_DB_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to delete?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Settings.this.setResult(RESULT_CANCELED);
					Settings.this.deleteDatabase("highscore.db");			
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Settings.this.setResult(RESULT_OK);
					dialog.cancel();
				}
			});
			return builder.create();
		}
		return null;
	}


}
