package com.example.carpoolbuddy;

/**
 * This class is a child class of Vehicle
 * It has additional properties for Segway type of Vehicle, such as range
 *
 * @author addison lee
 * @version 0.0
 */
public class Segway extends Vehicle{
    private int range;
    private int weightCapacity;

    public Segway(String vehicleType, int capacity, String model, String bestPrice, int range, int weightCapacity)
    {
        super(vehicleType, capacity, model, bestPrice);
        this.range = range;
        this.weightCapacity = weightCapacity;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
    }
}
