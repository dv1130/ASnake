package edu.dvrecic.service;

import edu.dvrecic.asnake.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ServiceSound extends Service
{	
	private static final String TAG = "MojServis";
	MediaPlayer sound1;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onCreate()
	{
;
	}

	@Override
	public void onDestroy() 
	{		
		sound1.stop();
	}
	
	@Override
	public void onStart(Intent intent, int startid)
	{
		Log.d(TAG, "onStart");
		 sound1 = MediaPlayer.create(this, R.raw.cowboy);
		 sound1.start();
		 sound1.setLooping(true);
	}
}