package com.example.mpi_project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//import org.checkerframework.checker.nullness.qual.NonNull;

//import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
//import io.reactivex.rxjava3.annotations.NonNull;

public class Mainsignup extends AppCompatActivity {
    EditText cin,name,fname,email,password,rpassword;
    Button btnsignup;
    String s_email;
    TextView log;
    StorageReference storageRef;
    FirebaseDatabase firebaseDatabase;
    StorageReference imageRef;
    Map<String,Object> map=new HashMap();
    boolean etest;
    private int IMAGE_PICKER_REQUEST_CODE0=3;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = (EditText) findViewById(R.id.s_name);
        fname = (EditText) findViewById(R.id.s_firstname);
        email = (EditText) findViewById(R.id.s_email);
        password = (EditText) findViewById(R.id.s_password);
        rpassword = (EditText) findViewById(R.id.s_conf_password);
        btnsignup = (Button) findViewById(R.id.s_btnsignup);
        log=findViewById(R.id.idTVLogin);
        progressBar=findViewById(R.id.progressBarSignUp);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Mainsignup.this,MainActivity.class));
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_name = name.getText().toString();
                String s_fname = fname.getText().toString();
                boolean passe = true;
                if (!check_name(s_name)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(name.getContext());
                    builder.setTitle("Error!!");
                    builder.setMessage("invalid Name");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    passe = false;
                } else {
                    if (!check_name(s_fname)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(name.getContext());
                        builder.setTitle("Error!!");
                        builder.setMessage("invalid First Name");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        passe = false;
                    } else {
                        s_email = email.getText().toString();
                        if ((s_email.indexOf('@') == -1)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(name.getContext());
                            builder.setTitle("Error!!");
                            builder.setMessage("invalid email");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            passe = false;
                        } else {
                            String s_password = password.getText().toString();
                            if (s_password.length() < 5) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(name.getContext());
                                builder.setTitle("Error!!");
                                builder.setMessage("invalid password");
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                String s_r_password = rpassword.getText().toString();
                                //String ss_password = password.getText().toString();
                                if (!s_r_password.equals(s_password)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(name.getContext());
                                    builder.setTitle("Error!!");
                                    builder.setMessage("Error for confirm password");
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                } else {
                                    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(s_email)
                                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                    if (task.isSuccessful()) {
                                                        SignInMethodQueryResult result = task.getResult();
                                                        if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                                                            // Email exists in Firebase Authentication
                                                            // You can handle the case where the email exists here
                                                            // For example, you can show a message or enable certain features for an existing user.
                                                            // Note: The task is successful even if the email exists but not verified (signed up with a different provider).
                                                            // You may need additional verification if required.
                                                            boolean emailExists = true;
                                                            Toast.makeText(Mainsignup.this, "Email not valid", Toast.LENGTH_SHORT).show();
                                                            // Proceed with further logic for an existing email.
                                                        } else {
                                                            //register the user
                                                            FirebaseAuth auth=FirebaseAuth.getInstance();
                                                            auth.createUserWithEmailAndPassword(s_email,s_password).addOnCompleteListener(Mainsignup.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(Task<AuthResult> task) {
                                                                    if (task.isSuccessful()){
                                                                        //Toast.makeText(Mainsignup.this, "Register successfuly ", Toast.LENGTH_SHORT).show();
                                                                        FirebaseUser firebaseUser=auth.getCurrentUser();
                                                                        map.put("name", name.getText().toString());
                                                                        map.put("fname", fname.getText().toString());
                                                                        map.put("email", email.getText().toString());
                                                                        map.put("password",password.getText().toString());
                                                                        map.put("state",true);
                                                                        String user_name=name.getText().toString()+" "+fname.getText().toString();
                                                                        //Toast.makeText(Mainsignup.this, " "+map.get("imgprofile"), Toast.LENGTH_SHORT).show();
                                                                                                    /*DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference();
                                                                                                    Toast.makeText(Mainsignup.this, " "+firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
                                                                                                    FirebaseDatabase.getInstance().getReference().child("profiles").child(firebaseUser.getUid()).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onComplete(Task<Void> task) {
                                                                                                            if (task.isSuccessful()){
                                                                                                                Toast.makeText(Mainsignup.this, "Register successfuly ", Toast.LENGTH_SHORT).show();
                                                                                                                //startActivity(new Intent(Mainsignup.this,MainAccueil.class));
                                                                                                            }else{
                                                                                                                Toast.makeText(Mainsignup.this, "Register failled", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        }
                                                                                                    });*/
                                                                            String userUid=firebaseUser.getUid();
                                                                            FirebaseDatabase.getInstance().getReference("profiles").child(userUid)
                                                                                    .setValue(map)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            Intent intent=new Intent(Mainsignup.this,MainAccueil.class);
                                                                                            intent.putExtra("username",user_name);
                                                                                            startActivity(intent);
                                                                                            Toast.makeText(Mainsignup.this, "Register successfuly ", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(Exception e) {
                                                                                    Toast.makeText(Mainsignup.this, "Register failed ", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });

                                                                    }else{
                                                                        Toast.makeText(Mainsignup.this, "sign up impossible", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            boolean emailExists = false;
                                                            // Proceed with further logic for a new user.
                                                        }
                                                    } else {
                                                        Exception exception = task.getException();
                                                        if (exception != null) {
                                                            Toast.makeText(Mainsignup.this, "Email not valid", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                            });
                                }


                            }
                        }
                    }}}
        });
        /*CircleImageView circleImageView=findViewById(R.id.imgprofile);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE0);
            }
        });*/

    }

    private boolean check_cin(){
        String s_cin=cin.getText().toString();
        boolean isNumeric = s_cin.matches("\\d+");
        if(s_cin.length()!=8||(!isNumeric)||((s_cin.charAt(0)!='1')&&(s_cin.charAt(0)!='0'))){
            return false;
        }else return true;
    }
    private boolean check_name(String s_name){
        return s_name.matches("[a-zA-Z-' ']+");
    }
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

}