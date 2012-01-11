package edu.dvrecic.asnake;

import android.app.Application;

public class ApplicationASnake  extends Application{
	
	private String name = "Player1";
	private boolean sensorOFF = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSensorOFF() {
		return sensorOFF;
	}

	public void setSensorOFF(boolean sensorOFF) {
		this.sensorOFF = sensorOFF;
	}
}
