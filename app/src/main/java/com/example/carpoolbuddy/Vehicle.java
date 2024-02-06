package com.example.carpoolbuddy;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class is the parent class of car, bicycle, segway and helicopter
 * It has common properties such as vehicle type, capacity, model, owner email
 * It also has an ArrayList that contains list of user who has ever ride on the vehicle
 * When a vehicle is newly created, its Open-For-Booking status is defauled to Open,
 * it's rating is defaulted to 5 (highest)
 * This class also contains several useful method such as updateOpenCloseStatus, reduceVehicleCapacity
 *
 * @author addison lee
 * @version 0.0
 */
public class Vehicle {
    private String vehicleType;
    private Integer capacity;
    private String model;
    private String bestPrice;
    private String ownerEmail;
    private Integer vehicleID;
    private String openStatus;
    private String rating;
    private ArrayList<String> riders;

    public Vehicle()
    {

    }

    public Vehicle(String someType, String someModel)
    {
        Random rand = new Random();
        int int_randome = rand.nextInt(99999);

        this.vehicleID = int_randome;
        this.vehicleType = someType;
        this.model = someModel;
        this.openStatus = "Open";
        this.rating = "5";
    }

    public Vehicle(String someType, Integer someCapacity, String someModel)
    {
        Random rand = new Random();
        int int_randome = rand.nextInt(99999);

        this.vehicleID = int_randome;
        this.vehicleType = someType;
        this.capacity = someCapacity;
        this.model = someModel;
        this.openStatus = "Open";
        this.rating = "5";
    }

    public Vehicle(String someType, Integer someCapacity, String someModel, String somePrice, String ownerEmail)
    {
        Random rand = new Random();
        int int_randome = rand.nextInt(99999);

        this.vehicleID = int_randome;
        this.vehicleType = someType;
        this.capacity = someCapacity;
        this.model = someModel;
        this.bestPrice = somePrice;
        this.ownerEmail = ownerEmail;
        this.openStatus = "Open";
        this.rating = "5";
    }

    public Vehicle(String someType, Integer someCapacity, String someModel, String somePrice)
    {
        Random rand = new Random();
        int int_randome = rand.nextInt(99999);

        this.vehicleID = int_randome;
        this.vehicleType = someType;
        this.capacity = someCapacity;
        this.model = someModel;
        this.bestPrice = somePrice;
        this.openStatus = "Open";
        this.rating = "5";
    }

    public String getVehicleType()
    {
        return this.vehicleType;
    }

    public String getModel()
    {
        return this.model;
    }

    public Integer getCapacity()
    {
        return this.capacity;
    }
    public String getBestPrice()
    {
        return this.bestPrice;
    }
    public String getOwnerEmail()
    {
        return this.ownerEmail;
    }

    public String getRating()
    {
        return this.rating;
    }

    public Integer getVehicleID()
    {
        return this.vehicleID;
    }

    /**
     * This method returns the Open status of the vehicle
     * @return
     */
    public String getOpenStatus()
    {
        if(this.openStatus.equals("Open"))
            return "Open";
        else
            return "Closed";
    }

    /**
     * When this method is called, it flips the Open/Close status of the vehicle
     */
    public void setOpenStatus()
    {
        if(this.openStatus.equals("Open"))
        {
            this.openStatus = "Closed";
        }
        else
        {
            this.openStatus = "Open";
        }

    }

    /**
     * This method reduce the capacity by 1
     * The method is called when a user book this vehicle
     */
    public void setVehicleCapacityReduceOne()
    {
        this.capacity= this.capacity-1;
    }

    public void setVehicleModel(String myVehicleModel)
    {
        this.vehicleType = myVehicleModel;
    }
}
