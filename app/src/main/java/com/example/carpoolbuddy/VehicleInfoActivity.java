package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class displays all vehicles on screen using recyclerview
 * It connects to firebase to retrieve all vehicles and save in an ArrayList
 * It then uses recyclerView with onClickListener to display model and capacity of all vehicles
 * User may click on a vehicle to see details, the vehicle info is passed to next intent via extras
 * There is a Refresh button that allows users to refresh content of recyclerview after update
 *
 * @author addison lee
 * @version 0.0
 */
public class VehicleInfoActivity extends AppCompatActivity {
    // define local variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String TAG= "myTag";
    private RecyclerView myRecyclerView;
    private ArrayList<Vehicle> vehList;
    private VehicleAdaptor.RecyclerViewClickListener listener;  // for RV click
    private String vehicleDocID;

    /**
     * This onCreate method connects to firebase, retrieves current user and call method
     * getAndPopulateData() to populate vehicle data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);

        // retrieve current user and link Firebase
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // link the recyclerView layout item to variable
        myRecyclerView = findViewById(R.id.recyclerView);

        // call method to populate vehicle data to RV
        getAndPopulateData();
    }

    /**
     * This method populates all vehicles from firebase to recyclerview
     */
    public void getAndPopulateData(){

        // retrieve all Vehicles from Firebase
        firestore.collection("Vehicles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // retrieve data from firebase and put in arraylist
                            vehList = new ArrayList<Vehicle>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                vehList.add(document.toObject(Vehicle.class));
                            }

                            // set RV to display contents from arraylist
                            setOnClickListener();  // for RV click, initialize the listener
                            VehicleAdaptor myAdaptor = new VehicleAdaptor(vehList, listener);
                            // include onClick listener
                            myRecyclerView.setAdapter(myAdaptor);
                            myRecyclerView.setLayoutManager(new LinearLayoutManager
                                    (VehicleInfoActivity.this));
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * This method is onClickListener for RV click to show vehicle profile
     */
    private void setOnClickListener()
    {
        System.out.println("*** at setOnclickListener #1");
        listener = new VehicleAdaptor.RecyclerViewClickListener()
        {
        @Override
        public void onClick (View v,int position)
        {
            // vehicle information to pass to VehicleProfileActivity intent
            System.out.println("*** at setOnclickListner1.5");
            Intent intent = new Intent(getApplicationContext(), VehicleProfileActivity.class);
            intent.putExtra("model", vehList.get(position).getModel());
            intent.putExtra("capacity", vehList.get(position).getCapacity().toString());
            intent.putExtra("owner", vehList.get(position).getOwnerEmail());
            intent.putExtra("type", vehList.get(position).getVehicleType());
            intent.putExtra("rating", vehList.get(position).getRating());
            intent.putExtra("openStatus", vehList.get(position).getOpenStatus());
            intent.putExtra("price", vehList.get(position).getBestPrice());
            intent.putExtra("vID", vehList.get(position).getVehicleID());
            System.out.println("***** Open status#1: "+vehList.get(position).getOpenStatus());
            startActivity(intent);
            finish();
        }
    };
    }

    /**
     * This method allows user to navigate to Add Vehicle screen
     * @param v
     */
    public void addVehicles(View v)
    {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    /**
     * This method allows user to force refresh data in recyclerview
     * @param v
     */
    public void clickToRefresh (View v){
        startActivity(new Intent(this, VehicleInfoActivity.class));
        finish();
    }

    /**
     * This method allows user to navigate to UserProfileActivity screen
     * @param v
     */
    public void goToUserProfile(View v)
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
}