package edu.dvrecic.asnake.sensors;

import java.util.List;

import edu.dvrecic.asnake.About;
import edu.dvrecic.asnake.Game;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class OrientationManager {
	private static Sensor sensor;
	private static SensorManager sensorManager;
	// you could use an OrientationListener array instead
	// if you plans to use more than one listener
	private static OrientationListener listener;

	/** indicates whether or not Orientation Sensor is supported */
	private static Boolean supported;
	/** indicates whether or not Orientation Sensor is running */
	private static boolean running = false;

	/** Sides of the phone */
	enum Side {
		UP,
		DOWN,
		LEFT,
		RIGHT;
	}
	/**
	 * Returns true if the manager is listening to orientation changes
	 */
	public static boolean isListening() {
		return running;
	}

	/**
	 * Unregisters listeners
	 */
	public static void stopListening() {
		running = false;
		try {
			if (sensorManager != null && sensorEventListener != null) {
				sensorManager.unregisterListener(sensorEventListener);
			}
		} catch (Exception e) {}
	}

	/**
	 * Returns true if at least one Orientation sensor is available
	 */
	public static boolean isSupported() {
		if (supported == null) {
			if (Game.getContext() != null) {
				sensorManager = (SensorManager) Game.getContext()
						.getSystemService(Context.SENSOR_SERVICE);
				List<Sensor> sensors = sensorManager.getSensorList(
						Sensor.TYPE_ORIENTATION);
				supported = new Boolean(sensors.size() > 0);
			} else {
				supported = Boolean.FALSE;
			}
		}
		return supported;
	}

	/**
	 * Registers a listener and start listening
	 */
	public static void startListening(
			OrientationListener orientationListener) {
		sensorManager = (SensorManager) Game.getContext()
				.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager.getSensorList(
				Sensor.TYPE_ORIENTATION);
		if (sensors.size() > 0) {
			sensor = sensors.get(0);
			running = sensorManager.registerListener(
					sensorEventListener, sensor, 
					SensorManager.SENSOR_DELAY_NORMAL);
			listener = orientationListener;
		}
	}

	/**
	 * The listener that listen to events from the orientation listener
	 */
	private static SensorEventListener sensorEventListener = 
			new SensorEventListener() {

		/** The side that is currently up */
		private Side currentSide = null;
		private Side oldSide = null;
		private float azimuth;
		private float pitch;
		private float roll;

		public void onAccuracyChanged(Sensor sensor, int accuracy) {}

		/*
		 * dodati senzorje za nagib levo,desno
		 */
		public void onSensorChanged(SensorEvent event) {

			azimuth = event.values[0];
			pitch = event.values[1];
			roll = event.values[2];

			if(roll < 0) {
				currentSide = Side.UP;
			} else if (pitch < -18){
				currentSide = Side.RIGHT;
			} else if (pitch > 20){
				currentSide = Side.LEFT;
			} else if (roll > 25){
				currentSide = Side.DOWN;
			}

			if (currentSide != null && !currentSide.equals(oldSide)) {
				switch (currentSide) {
				case UP : 
					listener.onUp();
					break;
				case DOWN : 
					listener.onDown();
					break;
				case RIGHT: 
					listener.onRight();
					break;
				case LEFT: 
					listener.onLeft();
					break;
				}
				oldSide = currentSide;
			}

			// forwards orientation to the OrientationListener
			listener.onOrientationChanged(azimuth, pitch, roll);
		}

	};
}
