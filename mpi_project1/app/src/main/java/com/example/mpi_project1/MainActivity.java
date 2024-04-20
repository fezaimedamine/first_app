package com.example.mpi_project1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.AuthResult;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    Button btnsignup, btnLogin;
    Drawable icon;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private ProgressBar progressBar;
    public String Email_add_item;
    public Mainadd mainadd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        email = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        icon = getResources().getDrawable(R.drawable.ic_email);
        email.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        icon = getResources().getDrawable(R.drawable.ic_password);
        password.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        btnsignup = (Button) findViewById(R.id.btnsignup);
        firebaseAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnlogin);
        progressBar=findViewById(R.id.progressbar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = email.getText().toString();
                String ppassword = password.getText().toString();
                showProgressBar();
                loginWithFirebase(username, ppassword);
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Mainsignup.class));
            }
        });

    }
        /*
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        usersRef = firebaseDatabase.getReference("users");
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = email.getText().toString().trim();
                String userpassword = password.getText().toString().trim();
                //checkUserExists(username);
                //loginWithEmailPassword(username, password_user);
                retrieveUserData(username,userpassword);
                startActivity(new Intent(MainActivity.this,MainAccueil.class));

            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Mainsignup.class));
            }
        });
    }
    */
    public void determine_email(String email){
        this.Email_add_item=email;
    }

    public String getEmail_add_item() {
        return Email_add_item;
    }

    private void showProgressBar() {
            progressBar.setVisibility(View.VISIBLE);
        }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
        private void loginWithFirebase(String email, String password){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            hideProgressBar();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //determine_email(email);
                                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                                //pass user's name
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("profiles");
                                usersRef.child(firebaseUser.getUid()).child("state").setValue(true);
                                usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String userName = snapshot.child("name").getValue(String.class);
                                        String userfname = snapshot.child("fname").getValue(String.class);
                                        determine_email(snapshot.child("email").getValue(String.class));
                                        //Toast.makeText(MainActivity.this, ""+Email_add_item, Toast.LENGTH_SHORT).show();
                                        if (userName != null) {
//hello worl
                                            Intent intent=new Intent(MainActivity.this, MainAccueil.class);
                                            String user_name=userName+" "+userfname;
                                            //Toast.makeText(MainActivity.this, ""+user_name, Toast.LENGTH_SHORT).show();
                                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            intent.putExtra("username",user_name);
                                            Toast.makeText(MainActivity.this, "hhh"+user_name, Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                        } else {
                                            // User's name not found in Firebase Realtime Database.
                                            Toast.makeText(MainActivity.this, "name inexist", Toast.LENGTH_SHORT).show();                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Failed to retrieve user's name from Firebase Realtime Database.
                                        //textViewUserName.setText("Welcome!");
                                        Toast.makeText(MainActivity.this, "name inexist lllllll", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                EditText name = findViewById(R.id.s_email);
                                String errorMessage = "Login failed!";
                                Exception exception = task.getException();
                                if (exception != null) {
                                    // Check specific error cases and show relevant error messages
                                    if (exception instanceof FirebaseAuthInvalidUserException) {
                                        errorMessage = "Login failed: " + exception.getMessage();
                                    } else if (exception instanceof FirebaseAuthWeakPasswordException) {
                                        errorMessage = "Invalid password!";
                                    } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                        errorMessage = "Invalid email or password!";
                                    }
                                }
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                            }
                    });
        }
    @Override
    public void onBackPressed() {
        // Start the LoginActivity and clear the back stack to prevent going back to MainAccueil
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // Optional: Finish the MainAccueil activity if you don't want it to remain in the back stack
    }
    /*private void retrieveUserData(String username,String userpassword) {
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean test=true;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String d_email = userSnapshot.child("email").getValue(String.class);
                    String d_password = userSnapshot.child("password").getValue(String.class);

                    if (username.equals(d_email) && userpassword.equals(d_password)) {
                        Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(),mainLogin.class));
                        startActivity(new Intent(MainActivity.this, MainAccueil.class));
                        test=false;
                        break;
                    }

                    //Log.d("User Data", "Email: " + email + ", Password: " + password);
                }
                if(test){
                    Toast.makeText(MainActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Firebase Error", "Failed to retrieve user data: " + databaseError.getMessage());
            }
        });
    }
    private void startMainActivity() {
        Intent intent = new Intent(MainActivity.this, Mainsignup.class);
        startActivity(intent);
        //finish(); // Optional: Close the current activity if needed
    }*/
}