package edu.dvrecic.asnake;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class World extends SurfaceView{

	SurfaceHolder holder;
	boolean isItOK = false;
	
	public World(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawARGB(10,90,50,255);
		invalidate();
	}

}
