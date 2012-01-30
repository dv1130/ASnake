package edu.dvrecic.asnake;

import edu.dvrecic.service.ServiceSound;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ASnakeActivity extends Activity implements OnClickListener{

	ImageButton newGame, highScore, about, quit;
	Menu myMenu;
	public static Typeface tf;
	private static final int EXIT_DIALOG=0; 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		 tf = Typeface.createFromAsset(getAssets(),"data/fonts/eye.TTF");
	        
	        TextView tv = (TextView) findViewById(R.id.start);
	        TextView tv1 = (TextView) findViewById(R.id.highscore);
	        TextView tv2 = (TextView) findViewById(R.id.settings);
	        TextView tv3 = (TextView) findViewById(R.id.about);
	        TextView tv4 = (TextView) findViewById(R.id.exit);
	        
	        startService(new Intent(this, ServiceSound.class)); // zaženemo servis
	        
	        tv.setTypeface(tf);
	        tv1.setTypeface(tf);
	        tv2.setTypeface(tf);
	        tv3.setTypeface(tf);
	        tv4.setTypeface(tf);
	        
	        tv.setOnTouchListener(new CustomTouchListener());
	        tv1.setOnTouchListener(new CustomTouchListener());
	        tv2.setOnTouchListener(new CustomTouchListener());
	        tv3.setOnTouchListener(new CustomTouchListener());
	        tv4.setOnTouchListener(new CustomTouchListener());
	        
	        tv.setOnClickListener(this);
	        tv1.setOnClickListener(this);
	        tv2.setOnClickListener(this);
	        tv3.setOnClickListener(this);
	        tv4.setOnClickListener(this);        
	        
	        Toast.makeText(this, "Press Menu to Start Game", Toast.LENGTH_LONG).show();
	}
	@Override
	public void onClick(View v) {
		
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.start:
			stopService(new Intent(this, ServiceSound.class));
			Intent i1 = new Intent();
			i1.setClass(this, Game.class);
			startActivity(i1);
			break;
		case R.id.highscore:
			Intent i2 = new Intent();
			i2.setClass(this, HighScoreList.class);
			startActivity(i2);
			break;
		case R.id.settings:
			Intent i4 = new Intent();
			i4.setClass(this, Settings.class);
			startActivity(i4);
			break;
		case R.id.about:
			Intent i3 = new Intent();
			i3.setClass(this, About.class);
			startActivity(i3);
			break;
		case R.id.exit:
			showDialog(EXIT_DIALOG);
			break;
		}	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		myMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, myMenu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_quit:
			showDialog(EXIT_DIALOG);
			return true;
		case R.id.menu_options:
			Intent i2 = new Intent();
			i2.setClass(this, MenuOptions.class);
			startActivity(i2);
			return true;
		case R.id.menu_newgame:
			stopService(new Intent(this, ServiceSound.class));
			Intent i3=new Intent(this,Game.class);
			startActivity(i3);
			break;
		case R.id.menu_highscore:
			Intent i4=new Intent(this,HighScoreList.class);
			startActivity(i4);
			break;
		}
		return false;
	}
	
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case EXIT_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Do you want to quit?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					ASnakeActivity.this.setResult(RESULT_CANCELED);
					ASnakeActivity.this.finish();}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					ASnakeActivity.this.setResult(RESULT_OK);
					dialog.cancel();
				}
			});
			return builder.create();
		}
		return null;
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println("start");
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopService(new Intent(this, ServiceSound.class));
		System.out.println("destroy");
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopService(new Intent(this, ServiceSound.class));
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startService(new Intent(this, ServiceSound.class));
	}
	
}