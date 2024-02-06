package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * This class allows a rider to rate a vehicle
 * It receives vehicle information from VehicleInfoActivities via extras
 * Once rating is submitted and updated to firebase, it navigates to Vehicle Information screen
 *
 * @author addison lee
 * @version 0.0
 */
public class RateVehicleActivity extends AppCompatActivity {

    // define local variables
    private String model;
    private String type;
    private String owner;

    /**
     * This onCreate method receives extras from previous intent and setText with these values
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_vehicle);

        // link input to variables
        TextView modelText = findViewById(R.id.RateVehicleModel);
        TextView typeText = findViewById(R.id.RateVehicleType);
        TextView ownerText = findViewById(R.id.RateVehicleOwner);

        // receive bundle from previous intent
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            model = extras.getString("model");
            type = extras.getString("type");
            owner = extras.getString("owner");
            //System.out.println("rate vehicle debug 2: "+model); debugging
        }

        // set text with the data from bundle
        modelText.setText(model);
        typeText.setText(type);
        ownerText.setText(owner);
    }

    /**
     * after the user submits the rating navigate to VehicleInfoActivity
     * @param v
     */
    public void submitRateVehicle(View v)
    {
        Intent intent = new Intent(this, VehicleInfoActivity.class);
        startActivity(intent);
    }
}