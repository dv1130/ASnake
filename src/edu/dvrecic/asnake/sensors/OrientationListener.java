package edu.dvrecic.asnake.sensors;

public interface OrientationListener {
	public void onOrientationChanged(float azimuth, float pitch, float roll);
	 
    /**
     * Top side of the phone is up
     * The phone is standing on its bottom side
     */
    public void onUp();
 
    /**
     * Bottom side of the phone is up
     * The phone is standing on its top side
     */
    public void onDown();
 
    /**
     * Right side of the phone is up
     * The phone is standing on its left side
     */
    public void onLeft();
 
    /**
     * Left side of the phone is up
     * The phone is standing on its right side
     */
    public void onRight();
 
}

