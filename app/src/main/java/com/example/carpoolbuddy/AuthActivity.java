package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

/**
 * This class allows the user to sign up or log in to the Carpool Buddy system
 * It uses a spinner to show list of user types (student, teacher, alumni etc.)
 * For existing user login, it authenticates the user with Firebase
 * For signup, it checks if user's email domain is cis.edu as this is only for CIS
 * Then it adds the user to Firebase
 *
 * @author addison lee
 * @version 0.0
 */
public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private EditText emailField;
    private EditText passwordField;
    private Spinner spinnerUserTypeField;
    private String TAG= "myTag";

    /**
     * The onCreate method connects to Firebase
     * It prepares some layout items such as Spinner with types of user to select
     * It also links input fields to parameters for later use
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Firebase connection
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // if the user has already signed in, then go to user profile
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // spinner for selecting user type
        spinnerUserTypeField = findViewById(R.id.AA_spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.UserType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserTypeField.setAdapter(adapter);

        // link input to properties
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
    }

    /**
     * This method checks the input email and password and validates it with Firebase
     * if login is successful, then call the updateUI method to go to the User Profile
     * @param v
     */
    public void signIn(View v)
    {
        // fetch login info
        String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();
        System.out.println(String.format("email: %s and password: %s", emailString, passwordString));

        // connect to firebase to verity login info
        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * This method first makes sure the input email is part of cis
     * If email is valid, then it adds the user to Firebase authentication database, and call the uploadData() method to add the user to firebase User document
     * It then calls updateUI() to go to the User Profile
     *
     * @param v
     */
    public void signUp(View v)
    {
        // param to property
        String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();
        System.out.println("Email: "+emailString);
        System.out.println("pass: "+passwordString);

        // check if CIS email address: if not, toast error
        if(!emailString.contains("cis.edu"))
            Toast.makeText(getApplicationContext(), "The app is only for CIS community, Please register with CIS email!", Toast.LENGTH_SHORT).show();
        else {
            // if it's a CIS user, create and add new user account to firebase
            mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("SIGN UP", "Successfully signed up the user");
                        FirebaseUser user = mAuth.getCurrentUser();
                        uploadData(user);
                        updateUI(user);
                    } else {
                        Log.w("SIGN UP", "createUserWithEmail:failure", task.getException());
                        updateUI(null);
                    }
                }
            });
        }
    }

    /**
     * This method navigates to the user profile screen when called
     *
     * @param currentUser
     */
    public void updateUI(FirebaseUser currentUser)
    {
        if(currentUser != null)
        {
            Intent intent = new Intent(this, UserProfileActivity.class);
            startActivity(intent);
        }
    }

    /**
     * This method is used by the signUp method to add the user object to firebase
     * @param currentUser
     */
    public void uploadData(FirebaseUser currentUser)
    {
        String emailString = currentUser.getEmail();
        String userTypeString = spinnerUserTypeField.getSelectedItem().toString();

        // user id generator
        Random rand = new Random();
        int random = rand.nextInt(99999);

        // create the new User object
        User myUser = new User(random, emailString, userTypeString);
        System.out.println("object uid: "+myUser.getUid());
        System.out.println("object email: "+myUser.getEmail());
        System.out.println("object type: "+myUser.getType());

        // Add a new document with a generated ID
        firestore.collection("User")
                .add(myUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}