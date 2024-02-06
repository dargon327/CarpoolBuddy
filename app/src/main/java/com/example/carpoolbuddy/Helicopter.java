package com.example.carpoolbuddy;

/**
 * This class is a child class of Vehicle
 * It has additional properties for helicopter type of vehicle, such as max altitude
 *
 * @author addison lee
 * @version 1.0
 */
public class Helicopter extends Vehicle{
    private int maxAltitude;
    private int maxAirspeed;

    public Helicopter(String vehicleType, int capacity, String model, String bestPrice,
                      int maxAltitude, int maxAirspeed)
    {
        super(vehicleType, capacity, model, bestPrice);
        this.maxAltitude = maxAltitude;
        this.maxAirspeed = maxAirspeed;
    }

    public int getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(int maxAltitude) {
        this.maxAltitude = maxAltitude;
    }

    public int getMaxAirspeed() {
        return maxAirspeed;
    }

    public void setMaxAirspeed(int maxAirspeed) {
        this.maxAirspeed = maxAirspeed;
    }
}
