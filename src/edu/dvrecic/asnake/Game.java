package edu.dvrecic.asnake;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.dvrecic.asnake.database.DatabaseHelper;
import edu.dvrecic.asnake.sensors.OrientationListener;
import edu.dvrecic.asnake.sensors.OrientationManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class Game extends Activity implements OrientationListener {

	private Snake snakeView;
	DatabaseHelper databaseHelper;
	SQLiteDatabase db;
	Cursor cursor;
	ApplicationASnake app;
	private static Context CONTEXT;
	protected static final int LOSE_DIALOG=0; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		databaseHelper = new DatabaseHelper(this);
		db = databaseHelper.getWritableDatabase();
		snakeView = (Snake) findViewById(R.id.snake);
		app = (ApplicationASnake) getApplication();
		snakeView.setParentActivity(this);

		CONTEXT = this;
		
		if (OrientationManager.isSupported() && app.isSensorOFF() == true) {
			OrientationManager.startListening(this);
		}

	}

	// funkcija, ki doda ime in rezultat v bazo
	protected void insertResult(String ime, int rezultat){	
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm yyyy-MM-dd");

		Log.w("baza","zapis v bazo");
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.USERNAME, ime);
		cv.put(DatabaseHelper.SCORE, rezultat);	
		cv.put(DatabaseHelper.DATE, sdf.format(cal.getTime()));	
		db.insert("highscore", DatabaseHelper.USERNAME, cv);		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db.close();	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		snakeView.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		snakeView.resume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//snakeView.stop();
		insertResult(app.getName(), snakeView.maxRezultat); // klic funkcije za shranjevanje v bazo
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case LOSE_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT);
			builder.setMessage("Ali želite zapustiti igro?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					Game.this.finish();}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			return builder.create();
		}
		return null;
	}

	@Override
	public void onOrientationChanged(float azimuth, float pitch, float roll) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUp() {
		// TODO Auto-generated method stub
		if (snakeView.smer != snakeView.JUG) {
			snakeView.naslednjaSmer = snakeView.SEVER;
		}
	}

	@Override
	public void onDown() {
		// TODO Auto-generated method stub
		if (snakeView.smer != snakeView.SEVER) {
			snakeView.naslednjaSmer = snakeView.JUG;
		}

	}

	@Override
	public void onLeft() {
		// TODO Auto-generated method stub
		// desna je leva
		if (snakeView.smer != snakeView.VZHOD) {
			snakeView.naslednjaSmer = snakeView.ZAHOD;
		}
	}

	@Override
	public void onRight() {
		// TODO Auto-generated method stub
		// leva je desna
		if (snakeView.smer != snakeView.ZAHOD) {
			snakeView.naslednjaSmer = snakeView.VZHOD;
		}
	}

	public static Context getContext() {
		return CONTEXT;
	}

}
