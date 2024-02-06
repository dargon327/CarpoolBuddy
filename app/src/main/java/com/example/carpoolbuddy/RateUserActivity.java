package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * This class allows a vehicle owner user to rate a rider
 * Once rating is submitted and updated to firebase, it navigates to User Profile screen
 *
 * @author addison lee
 * @version 0.0
 */
public class RateUserActivity extends AppCompatActivity {

    /**
     * another oncreate method that sets up layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);
    }

    /**
     * after the user submits rating, update average rating
     * @param v
     */
    public void submitRiderRating(View v)
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}