package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This class allows user to enter vehicle information for others to book.
 * It uses a spinner item to list types of vehicles that user can add
 * It then add the new Vehicle object onto FireBase Vehicle collection
 * It also update the Firebase User to add this vehicle to user's Arraylist of vehicles
 * It hide Booking and Rate Vehicle button if owner is current user, else hide Open button
 * The Book Vehicle button updates vehicle's capacity and save on firebase
 * The Rate Vehicle button updates vehicle's rating and save on firebase
 * The Open/close Vehicle button updates vehicle's OpenForBooking status and save on firebase
 *
 * @author addison lee
 * @version 0.0
 */
public class VehicleProfileActivity extends AppCompatActivity
{

    // define local variables
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String vehicleModel;
    private String vehicleCapacity;
    private String vehicleOwner;
    private String vehicleType;
    private String vehiclePrice;
    private String vehicleRating;
    private String vehicleOpenStatus;
    private Integer vehicleID;
    private FirebaseFirestore firestore;
    private String TAG= "myTag";
    private Vehicle myVehObj;
    private User myUserObj;

    /**
     * This onCreate method connects to firebase, retrieve current user and link layout items to
     * parameter for displaying vehicle information
     * It receives vehicle information via extras from VehicleInformationActivities
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        // retrieve current user
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // link layout items to parameter
        TextView nameText = findViewById(R.id.VehicleProfileMdel);
        TextView capacityText = findViewById(R.id.VehicleProfileCapacity);
        TextView ownerText = findViewById(R.id.VehicleProfileOwner);
        TextView typeText = findViewById(R.id.VehicleProfileType);
        TextView priceText = findViewById(R.id.VehicleProfilePrice);
        TextView ratingText = findViewById(R.id.VehicleProfileRating);
        TextView OpenStatusText = findViewById(R.id.VehicleProfileOpenStatus);
        Button bookingButton = findViewById(R.id.VehicleProfileBookButton);
        Button openButton = findViewById(R.id.VehicleProfileOpenButton);
        Button ratingButton = findViewById(R.id.VehicleProfileRateVehicleButton);

        // Bundle the data from RecyclerView
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            vehicleModel = extras.getString("model");
            vehicleCapacity = extras.getString("capacity");
            vehicleOwner = extras.getString("owner");
            vehicleType = extras.getString("type");
            vehiclePrice = extras.getString("price");
            vehicleRating = extras.getString("rating");
            vehicleOpenStatus = extras.getString("openStatus");
            vehicleID = extras.getInt("vID");
            System.out.println("***** Open status#2: "+vehicleOpenStatus);
        }

        // Hide Booking button if owner is current user, else hide Open button
        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser.getEmail().equals(vehicleOwner))
        {
            bookingButton.setVisibility(View.GONE);
            ratingButton.setVisibility(View.GONE);
        }
        else{
            openButton.setVisibility(View.GONE);
        }

        // Show data on screen
        nameText.setText(vehicleModel);
        capacityText.setText(vehicleCapacity);
        ownerText.setText(vehicleOwner);
        typeText.setText(vehicleType);
        priceText.setText(vehiclePrice);
        ratingText.setText(vehicleRating);
        OpenStatusText.setText(vehicleOpenStatus);
    }

    /**
     * when user click on the button, to update firebase to open or close the vehicle
     * @param v
     */
    public void openCloseVehicle(View v)
    {
        // connect to firebase
        System.out.println("***** At openCloseVehicle method");
        firestore = FirebaseFirestore.getInstance();

        // locate the vehicle object to update on firebase
        firestore.collection("Vehicles")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // retrieve data from firebase and loop to identify current vehicle
                            myVehObj = new Vehicle();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                myVehObj = document.toObject(Vehicle.class);

                                // Found the Vehicle object
                                if(vehicleID.equals(myVehObj.getVehicleID()))
                                {
                                    System.out.println("inside If loop found VID : "+myVehObj.getVehicleID());

                                    // Switch Open/Close status
                                    myVehObj.setOpenStatus();

                                    // call method to update this object on Firebase
                                    System.out.println("***** vehicle doc now : "+document.getId());
                                    updateVehicleStatus(document.getId(), myVehObj);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // once done, navigate to Vehicle Info screen
            Intent intent = new Intent(this, VehicleInfoActivity.class);
            startActivity(intent);
    }

    /**
     * This method is to update firebase and reduce capacity by one when user book the vehicle
     * also update User object's RiderVehicle arraylist to include the vehicleRode
     * @param v
     */
    public void bookVehicle(View v)
    {
        System.out.println("***** At bookVehicle method");

        // Error handling to toast message if capacity is 0 then cannot book
        if(vehicleCapacity.equals("0")) {
            System.out.println("***** capacity is full");
            Toast.makeText(getApplicationContext(), "The capacity is full", Toast.LENGTH_SHORT).show();
        }
        else {
            // Error handling to toast message if vehicle is closed then cannot book
            if(vehicleOpenStatus.equals("Closed"))
            {
                Toast.makeText(getApplicationContext(), "The vehicle is closed for booking",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                // update Capacity field of the vehicle booked
                System.out.println("***** updating vehicle capacity");
                firestore = FirebaseFirestore.getInstance();
                firestore.collection("Vehicles")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // retrieve data from firebase and loop to identify current vehicle
                                    myVehObj = new Vehicle();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        myVehObj = document.toObject(Vehicle.class);

                                        // Found the Vehicle object
                                        if (vehicleID.equals(myVehObj.getVehicleID())) {
                                            System.out.println("*** this vehicle is : " + myVehObj);

                                            // Call method to reduce capacity
                                            myVehObj.setVehicleCapacityReduceOne();

                                            // call method to update this object on Firebase
                                            System.out.println("***** vehicle doc now : " + document.getId());
                                            updateVehicleStatus(document.getId(), myVehObj);
                                            Toast.makeText(getApplicationContext(), "Vehicle booked!",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });

                // Retrieve the User object from Firestore
                firestore.collection("User")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // retrieve data from firebase and loop to identify current user
                                    myUserObj = new User();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        myUserObj = document.toObject(User.class);

                                        // Found the User object
                                        if (mUser.getEmail().equals(myUserObj.getEmail())) {
                                            System.out.println("inside If loop for: " + myUserObj.getEmail());
                                            System.out.println("the vehicle obj is : " + myUserObj);
                                            myUserObj.addVehicleRode(myVehObj);

                                            System.out.println("***** docID is : " + document.getId());
                                            updateUserAddVehicleRode(document.getId(), myUserObj);
                                        }
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            } // close second IF for Open status checking
        } // close first IF for 0 capacity checking

        // go back to VehicleInfo
        Intent intent = new Intent(this, VehicleInfoActivity.class);
        startActivity(intent);
    }

    /**
     * Update Vehicle object document with updated capacity or openStatus to Firebase
     * @param docID
     * @param v
     */
    public void updateVehicleStatus(String docID, Vehicle v)
    {
        System.out.println("***** docID is : "+docID);
        // Update Vehicle object with parameter object
        firestore.collection("Vehicles")
                .document(docID)
                .set(v)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        Log.d(TAG, "DocumentSnapshot updated with ID: " + docID);
    }

    /**
     * Update User object with Vehicle booked to Firebase
     * @param docID
     * @param u
     */
    public void updateUserAddVehicleRode(String docID, User u)
    {
        System.out.println("***** docID is : "+docID);
        // Update User object to add a vehicle to the user
        firestore.collection("User")
                .document(docID)
                .set(u)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        Log.d(TAG, "DocumentSnapshot updated with ID: " + docID);
    }

    /**
     * navigate to Rate Vehicle screen and pass the vehicle information
     * @param v
     */
    public void rateVehicle(View v)
    {
        // pass the vehicle information to next intent
        Intent intent = new Intent(getApplicationContext(), RateVehicleActivity.class);
        intent.putExtra("model", vehicleModel);
        intent.putExtra("owner", vehicleOwner);
        intent.putExtra("type", vehicleType);
        startActivity(intent);
    }
}