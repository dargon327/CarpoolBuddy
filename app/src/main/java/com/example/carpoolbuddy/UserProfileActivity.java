package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This class displays user's basic information on screen after user logged in
 * It also have several buttons for user to use
 * One button is to see all vehicles, another is to directly add a vehicle
 * It also allows the user to rate a rider, and see a list of recommended vehicles
 * There is also a Sign Out button on this screen
 *
 * @author addison lee
 * @version 0.0
 */
public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore firestore;
    private User myUserObj;
    private String TAG= "myTag";
    private User currUserObj;
    TextView user_email;

    /**
     * when code launches, connect to firebase, link textViews with parameters for display
     * then it connects to Firebase to retrieve user's information for display
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // firebase connection
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        // display user email
        user_email = (TextView) this.findViewById(R.id.UP_email);
        user_email.setText(mUser.getEmail());

        // link textview to properties
        TextView user_name = findViewById(R.id.UserProfileName);
        TextView user_type = findViewById(R.id.UserProfileType);
        TextView user_rating = findViewById(R.id.UserProfileRating);

        // retrieve user information from firebase
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
                                    user_type.setText(myUserObj.getType());
                                    user_name.setText(myUserObj.getName());
                                    user_rating.setText(myUserObj.getRider_rating().toString());
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    /**
     * This method allows user to signout
     * @param v
     */
   public void signOut(View v)
   {
       FirebaseAuth.getInstance().signOut();
       Intent intent = new Intent(this, AuthActivity.class);
       startActivity(intent);
   }

    /**
     * This method allows user to navigate to the Vehicle Info screen
     * @param v
     */
   public void seeVehicles(View v)
   {
       Intent intent = new Intent(this, VehicleInfoActivity.class);
       startActivity(intent);
   }

    /**
     * This method allows user to navigate to the Rate User screen
     * @param v
     */
   public void rateUserButton(View v)
   {
       Intent intent = new Intent(this, RateUserActivity.class);
       startActivity(intent);
   }

    /**
     * This method allows user to navigate to the Add Vehicle screen
     * @param v
     */
   public void addVehicleButton(View v)
   {
       Intent intent = new Intent(this, AddVehicleActivity.class);
       startActivity(intent);
   }

    /**
     * This method allows user to navigate to the Recommend Vehicle screen
     * @param v
     */
    public void recommendVehicleButton(View v)
    {
        Intent intent = new Intent(this, RecommendedVehicleActivity.class);
        startActivity(intent);
    }
}