package com.example.carpoolbuddy;

/**
 * A child class of User
 * It has additional properties specific to Alumni  (e.g grad year)
 *
 * @author addison lee
 * @version 0.0
 */
public class Alumni extends User{
    private int gradYear;

    public Alumni(Integer uid, String name, String email, String userType, double priceMultiplier, int gradYear)
    {
        super(uid, name, email, userType, priceMultiplier);
        this.gradYear = gradYear;
    }

    /**
     * getter method
     * @param
     */
    public int getGradYear() {
        return gradYear;
    }

    /**
     * setter
     * @param gradYear
     */
    public void setGradYear(int gradYear) {
        this.gradYear = gradYear;
    }
}
