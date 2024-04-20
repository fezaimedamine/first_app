package com.example.mpi_project1;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Mainadd extends AppCompatActivity {
    private static final int IMAGE_PICKER_REQUEST_CODE0 = 3;
    private StorageReference mStorageRef;
    private DatabaseReference mDataRef;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    FirebaseDatabase firebaseDatabase;
    EditText name,email,price,des,imgurl_profile;
    StorageReference imageRef;
    int itemCount=0;
    ImageView imageView;
    private Bitmap selectedImageBitmap;
    Button btnuplouad;
    //int i=0;
    int successfulUploads = 0;
    Map<String, Object> map = new HashMap<>();
    Map<String, Object> map1 = new HashMap<>();
    private List<String> imageUriList = new ArrayList<>();;
    ClipData clipData;
    ProgressBar progressBar;
    String domain;
    public Mainadd(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        name=(EditText) findViewById(R.id.add_textname);
        email=(EditText) findViewById(R.id.add_textemail);
        price=(EditText) findViewById(R.id.add_textprix);
        des=(EditText) findViewById(R.id.add_textdes);
        imgurl_profile=(EditText) findViewById(R.id.add_textimgurl);
        mDataRef= FirebaseDatabase.getInstance().getReference();
        btnuplouad=findViewById(R.id.uploadButton);
        imageView = findViewById(R.id.img_selected);
        progressBar=findViewById(R.id.progressbar);
        Button btnimg=findViewById(R.id.selectimagesButton0);
        String path_complete=getIntent().getStringExtra("domain");
        btnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE0);
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST_CODE0 && resultCode == Activity.RESULT_OK) {
            if (data != null ) {
                if (data.getClipData()!=null) {
                    //ArrayList<Uri> imageUriList = new ArrayList<>();
                    clipData = data.getClipData();
                    storageRef = FirebaseStorage.getInstance().getReference();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    //Uri imageUri = clipData.getItemAt().getUri();
                    upLouadImageAndFirestore(storageRef);
                } else if(data.getData()!=null){
                    ArrayList<Uri> imageUriList = new ArrayList<>();
                    storageRef = FirebaseStorage.getInstance().getReference();
                    Uri imageUri = data.getData();
                    imageRef = storageRef.child("images/" +"img"+System.currentTimeMillis() + "." + "jpg");
                    UploadTask uploadTask = imageRef.putFile(imageUri);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle successful upload, like getting the download URL
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                    // Store this URL in Firestore along with other data
                                    // Assuming you have a reference to the Firestore instance and a "technology" collection
                                    imageUriList.add(downloadUrl); // Replace with the actual download URL
                                    btnuplouad.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                                map.put("imageURL",imageUriList);
                                                map.put("name", name.getText().toString());
                                                map.put("email", email.getText().toString());
                                                map.put("price", price.getText().toString());
                                                map.put("discription", des.getText().toString());
                                                String path=getIntent().getStringExtra("domain");
                                                Toast.makeText(Mainadd.this, "Data inserted Successfuly.", Toast.LENGTH_SHORT).show();
                                                FirebaseFirestore.getInstance().collection(path)
                                                        .add(map)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                // Document added successfully
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Error adding document
                                                            }
                                                        });
                                            }


                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle unsuccessful upload
                        }
                    });
                }

            }

        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR =getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void upLouadImageAndFirestore(StorageReference storageRef){
        imageRef = storageRef.child("images/" + "img"  + System.currentTimeMillis() + "." + "jpg");
        UploadTask uploadTask = imageRef.putFile(clipData.getItemAt(0).getUri());
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                showProgressBar();
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        imageUriList.add(downloadUrl.toString());
                        // Check first image are uploaded
                        StorageReference imageRef = storageRef.child("images/" + "img" + System.currentTimeMillis() + "." + "jpg");
                        UploadTask uploadTask = imageRef.putFile(clipData.getItemAt(1).getUri());
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUrl) {
                                        imageUriList.add(downloadUrl.toString());
                                        StorageReference imageRef = storageRef.child("images/" + "img" + System.currentTimeMillis() + "." + "jpg");
                                        UploadTask uploadTask = imageRef.putFile(clipData.getItemAt(2).getUri());
                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                hideProgressBar();
                                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri downloadUrl) {
                                                        imageUriList.add(downloadUrl.toString());
                                                        btnuplouad.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                String path=getIntent().getStringExtra("domain");
                                                                String time=System.currentTimeMillis()+"";
                                                                String id=path+time;
                                                                map.put("id",id);
                                                                map.put("domain",path);
                                                                map.put("name", name.getText().toString());
                                                                map.put("email", email.getText().toString());
                                                                map.put("prix", price.getText().toString());
                                                                map.put("discription", des.getText().toString());
                                                                map.put("imageURL", imageUriList);
                                                                map.put("imageurl_profile",imgurl_profile.getText().toString());
                                                                FirebaseFirestore.getInstance().collection(path).document(id)
                                                                        .set(map)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(Mainadd.this, "Data inserted Successfully.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                // Error adding document
                                                                            }
                                                                        });
                                                                map1.put("id",id);
                                                                map1.put("domain",path);
                                                                map1.put("name", name.getText().toString());
                                                                map1.put("email", email.getText().toString());
                                                                map1.put("prix", price.getText().toString());
                                                                map1.put("discription", des.getText().toString());
                                                                map1.put("imageurl_profile",imgurl_profile.getText().toString());
                                                                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                                                                FirebaseUser user=firebaseAuth.getCurrentUser();
                                                                FirebaseDatabase.getInstance().getReference()
                                                                        .child("mystore")
                                                                        .child("mystore"+user.getUid())
                                                                        .push()
                                                                        .setValue(map1)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void unused) {
                                                                                Toast.makeText(Mainadd.this, "you are adding product to your store", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(Exception e) {

                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                                Toast.makeText(Mainadd.this, "Error for assyncronise", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                Toast.makeText(Mainadd.this, "Error for assyncronise", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
                Toast.makeText(Mainadd.this, "Error for assyncronise", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}