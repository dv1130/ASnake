package edu.dvrecic.asnake;

import edu.dvrecic.asnake.database.DatabaseHelper;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class HighScoreList extends Activity{

	protected SQLiteDatabase db;
	protected Cursor cursor;
	protected ListAdapter adapter;
	protected ListView highscore_lista;
	public static Typeface tf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.highscore_main);
		
		tf = Typeface.createFromAsset(getAssets(),"data/fonts/eye.TTF");
		TextView tv = (TextView) findViewById(R.id.textViewHighScores);
		tv.setTypeface(tf);
		TextView tv1 = (TextView) findViewById(R.id.textViewPodatkiHS);
		tv1.setTypeface(tf);
		
        db = (new DatabaseHelper(this)).getWritableDatabase();
        highscore_lista = (ListView) findViewById (R.id.list);
        getDataDB();
	}
	
    public void getDataDB() {
    	// || is the concatenation operation in SQLite
		cursor = db.rawQuery("SELECT * FROM highscore ORDER BY score DESC LIMIT 10", 
						null);

		adapter = new SimpleCursorAdapter(
				this, 
				R.layout.highscore_list, 
				cursor, 
				new String[] {"username", "score", "date"}, 
				new int[] {R.id.name, R.id.score, R.id.date});
		
		highscore_lista.setAdapter(adapter);
		db.close();
    }
}
