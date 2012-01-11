package edu.dvrecic.asnake;

import edu.dvrecic.asnake.sensors.OrientationListener;
import edu.dvrecic.asnake.sensors.OrientationManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class About extends Activity implements OrientationListener{
	/** Called when the activity is first created. */
	private static Context CONTEXT;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		CONTEXT = this;
		if (OrientationManager.isSupported()) {
			OrientationManager.startListening(this);
		}
	}

	public static Context getContext() {
		// TODO Auto-generated method stub
		return CONTEXT;
	}

	@Override
	public void onOrientationChanged(float azimuth, 
			float pitch, float roll) {
	}
	@Override
	public void onBottomUp() {
		System.out.println("bottomUP");
			
	}

	@Override
	public void onLeftUp() {
		System.out.println("leftUP");
	}

	@Override
	public void onRightUp() {
		System.out.println("rightUP");
	}

	@Override
	public void onTopUp() {
		System.out.println("topUP");
	}
}
