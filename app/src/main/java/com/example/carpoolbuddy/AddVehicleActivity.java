package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class allows the user to enter vehicle information that will appear when other users book/browse
 * It uses a spinner object to list types of vehicles that the user can select
 * It then adds the new Vehicle object onto the firebase Vehicle collection
 * It also update the firebase User collection to add this vehicle to the user's list of vehicles
 *
 * @author addison lee
 * @version 0.0
 */
public class AddVehicleActivity extends AppCompatActivity {
    private Spinner spinnerVecType;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText vecModel;
    private EditText maxCap;
    private EditText bestPrice;

    private String TAG= "myTag";
    private User myUserObj;

    /**
     * onCreate method connects to Firebase when code is run
     * It also sets some of the layout items such as spinner values and links the input to parameters for later use
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        // links to firebase
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // setting spinner
        spinnerVecType = findViewById(R.id.AVA_Spinner);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.VehicleType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVecType.setAdapter(adapter);

        // linking input to properties
        maxCap = findViewById(R.id.AVA_capacity);
        vecModel = findViewById(R.id.AVA_model);
        bestPrice = findViewById(R.id.AVA_best_price);

    }

    /**
     * This method takes the input and instantiates a new Vehicle object
     * The new vehicle is added to Firebase
     * Then, updates current user's Firebase record to add the vehicle
     *
     * @param v
     */
    public void addVehicle(View v) {


        //System.out.println("addVehicle method"); for debugging

        // linking input with params
        String maxCapacityString = maxCap.getText().toString();
        Integer maxCapacityInt = Integer.parseInt(maxCapacityString);
        String vehicleModelString = vecModel.getText().toString();
        String bestPriceString = bestPrice.getText().toString();
        String myVehicleTypeString = spinnerVecType.getSelectedItem().toString();


        // get current User
        FirebaseUser mUser = mAuth.getCurrentUser();

        // create new Vehicle object
        Vehicle myVehicle = new Vehicle(myVehicleTypeString, maxCapacityInt, vehicleModelString, bestPriceString, mUser.getEmail());

        /* debugging
        System.out.println("vehicle type: " + myVehicle.getVehicleType());
        System.out.println("vehicle model: " + myVehicle.getModel());
        System.out.println("vehicle capacity is: " + myVehicle.getCapacity());
        System.out.println("vehicle best price: " + myVehicle.getBestPrice());
        System.out.println("vehicle email: " + myVehicle.getOwnerEmail());
        System.out.println("vehicle ID: " + myVehicle.getVehicleID());
        System.out.println("vehicle openStatus: " + myVehicle.getOpenStatus());
        */

        // add a new Vehicle with a generated ID
        firestore.collection("Vehicles")
                .add(myVehicle)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added Vehicle with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        // Retrieve the User object from Firestore to add the vehicle to the User's owned vehicles
        firestore.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            myUserObj = new User();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                myUserObj = document.toObject(User.class);
                                if(mUser.getEmail().equals(myUserObj.getEmail()))
                                {
                                    myUserObj.addVehicle(myVehicle);
                                    System.out.println("***** docID is : "+document.getId());
                                    updateUserAddVehicle(document.getId(), myUserObj);
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        // when finished with adding vehicle, go back to 'see all vehicles'
        Intent intent = new Intent(this, VehicleInfoActivity.class);
        startActivity(intent);

    }

    /**
     * This method is called by AddVehicle method to update the User document on Firebase
     * with the parameter User object
     * @param docID
     * @param u
     */
        public void updateUserAddVehicle(String docID, User u)
        {
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

}