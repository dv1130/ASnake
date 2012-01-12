package edu.dvrecic.asnake;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

public class About extends Activity{

	public static Typeface tf, tf1;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		tf = Typeface.createFromAsset(getAssets(),"data/fonts/eye.TTF");
		TextView tv = (TextView) findViewById(R.id.textViewAsnake);
		tv.setTypeface(tf);

		tf1 = Typeface.createFromAsset(getAssets(),"data/fonts/fdfruta.ttf");
		TextView tv1 = (TextView) findViewById(R.id.textViewMyName);
		tv1.setTypeface(tf1);

		TextView tv2 = (TextView) findViewById(R.id.textViewPT);
		tv2.setTypeface(tf1);

		TextView tv3 = (TextView) findViewById(R.id.textViewOpis);
		tv3.setTypeface(tf1);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// Only process DOWN action, so it responds as soon as the
		// screen is touched.
		if (event.getAction()==MotionEvent.ACTION_DOWN)
		{
			finish();
		}
		return false;
	}
}
