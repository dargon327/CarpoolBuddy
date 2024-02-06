package com.example.carpoolbuddy;

/**
 * This class is a child class of Vehicle
 * It has additional properties for car type of vehicle, such as range
 *
 * @author addison lee
 * @version 0.0
 */
public class Car extends Vehicle{
    private int range;

    public Car(String vehicleType, int capacity, String model, String bestPrice, int range)
    {
        super(vehicleType, capacity, model, bestPrice);
        this.range = range;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}
