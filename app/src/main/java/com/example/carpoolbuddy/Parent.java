package com.example.carpoolbuddy;

import java.util.ArrayList;

/**
 * This class is a child class of User
 * It has additional properties for Parent type of user, such as children's UID
 *
 * @author addisonlee
 * @version 0.0
 */
public class Parent extends User {
    private ArrayList<String> childrenUIDS;

    public Parent(Integer uid, String name, String email, String userType, double priceMultiplier,
                  ArrayList<String> myChildUID)
    {
        super(uid, name, email, userType, priceMultiplier);
        this.childrenUIDS = myChildUID;
    }

    public ArrayList<String> getChildrenUIDS() {
        return childrenUIDS;
    }

    public void setChildrenUIDS(ArrayList<String> childrenUIDS) {
        this.childrenUIDS = childrenUIDS;
    }
}
