package com.example.carpoolbuddy;

import java.util.ArrayList;

/**
 * This class is a parent class for student, teacher, parent and alumni
 * It contains common properties of user such as email, user type and rider rating
 * It also contains two ArrayList of vehicles, one for vehicle owned by the user,
 * the other for vehicle rode by the user
 *
 * @author addison lee
 * @version 0.0
 */
public class User {
    private Integer uid;
    private String email;
    private String name = "Someone";
    private String type;
    private Double priceMultiplier;
    private ArrayList<Vehicle> ownedVehicles = new ArrayList<>();
    private Integer rider_rating;
    private ArrayList<Vehicle> rideVehicle = new ArrayList<>();

    public User(){}

    public User(Integer someUid, String someEmail, String someType)
    {
        this.uid = someUid;
        this.email = someEmail;
        this.type = someType;
        this.rider_rating = 5;
    }

    public User(Integer someUid, String someEmail, String someName, String someType, Double someMultiplier)
    {
        this.uid = someUid;
        this.email = someEmail;
        this.name = someName;
        this.type = someType;
        this.priceMultiplier = someMultiplier;
        this.rider_rating = 5;
    }

    /**
     * method to add vehicle owned by the user
     * @param v
     */
    public void addVehicle(Vehicle v)
    {
        System.out.println("***** at addVehicle method in User obj");
        ownedVehicles.add(v);
    }

    /**
     * method to return vehicles owned by the user
     * @return
     */
    public ArrayList<Vehicle> getVehicle()
    {
        return this.ownedVehicles;
    }

    /**
     * method to add vehicle rode by the user
     * @param v
     */
    public void addVehicleRode(Vehicle v)
    {
        System.out.println("***** at addVehicleRode method in User obj"+v);
        rideVehicle.add(v);
    }

    /**
     * method to return vehicles rode by the user
     * @return
     */
    public ArrayList<Vehicle> getVehicleRode()
    {
        return this.rideVehicle;
    }

    public Integer getUid(){
        return this.uid;
    }

    public String getEmail(){
        return this.email;
    }

    public String getType(){
        return this.type;
    }

    public String getName() { return this.name;}

    public Integer getRider_rating() {
        return this.rider_rating;
    }
}
