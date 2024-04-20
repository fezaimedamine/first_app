package com.example.mpi_project1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.Timer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MainAccueil extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.HomeFragmentListener {
    private ViewPager viewPager;
    private ImageView bar_menu;
    private int[] images = {R.drawable.img1, R.drawable.img3};
    private int currentPage = 0;
    private Timer timer;
    private final long DELAY_MS = 1000; // Delay in milliseconds before first slide change
    private final long PERIOD_MS = 3000; // Interval between slide changes
    Drawable icon;
    TextView home, panier, delivre, help;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private HomeFragment homeFragment = new HomeFragment();
    private TextView username,tech;
    MainAccueil.searchInterface listener;
    private int IMAGE_PICKER_REQUEST_CODE0=1;
    ImageView imgprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        //bar_menu=findViewById(R.id.bar_menu);
        //home=findViewById(R.id.welcome);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomsheet);
        navigationView.bringToFront();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        // Set up the navigation drawer toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        homeFragment.setHomeFragmentListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        bottomNavigationView.setOnItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.home_bs:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                            //Bundle args=new Bundle();
                            //args.putString("key",this);
                            break;
                        case R.id.delivre_bs:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DelivreFragment()).commit();
                            break;
                        case R.id.panier_bs:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PanierFragment()).commit();
                            break;
                    }
                    return true;
        });
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        username=headerView.findViewById(R.id.header_user_name);
        imgprofile=headerView.findViewById(R.id.imgprofile);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("profiles").child(firebaseUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String img=snapshot.child("profileimage").getValue(String.class);
                        if(img!=null){
                            Picasso.get().load(img).into(imgprofile);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, IMAGE_PICKER_REQUEST_CODE0);
            }
        });
        String fullname=getIntent().getStringExtra("username");
        username.setText(fullname);
        if(listener!=null){
            Toast.makeText(this, "hello world" , Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_REQUEST_CODE0 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
                StorageReference storageRef ;
                storageRef= FirebaseStorage.getInstance().getReference();
                Uri imageUri = data.getData();
                StorageReference imageRef;
                imageRef= storageRef.child("images/" + "img" + System.currentTimeMillis() + "." + "jpg");
                UploadTask uploadTask = imageRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase.getInstance().getReference()
                                        .child("profiles").child(firebaseUser.getUid()).child("profileimage")
                                        .setValue(uri.toString());
                                Toast.makeText(MainAccueil.this, "profile image pushed successfully " , Toast.LENGTH_SHORT).show();
                                //imgprofile.setImageURI(uri);
                                Picasso.get().load(uri).into(imgprofile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(MainAccueil.this, "Error of upload URL of image" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainAccueil.this, "Error of upload image in storage" , Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }
    }

    //@Override
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                //startActivity(new Intent(MainAccueil.this,HomeFragment.class));
                break;
            case R.id.nav_description: getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new profileFragment()).commit();
            break;
            case R.id.nav_store: getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new storeFragment()).commit();
            break;
            case R.id.nav_faq: break;
            case R.id.nav_logout:
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                FirebaseUser user=firebaseAuth.getCurrentUser();
                FirebaseDatabase.getInstance().getReference().child("profiles").child(user.getUid()).child("state")
                        .setValue(false);
                firebaseAuth.signOut();
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainAccueil.this,MainActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onTechTextViewClicked(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new techFragment()).commit();
    }
    @Override
    public void onImmobTextViewClicked(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new immobFragment()).commit();
    }
    @Override
    public void onGamingTextViewClicked(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new gamingFragment()).commit();
    }
    @Override
    public void onClubsTextViewClicked(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new clubsFragment()).commit();
    }
    @Override
    public void onSocialTextViewClicked(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new socialFragment()).commit();
    }
    @Override
    public void onServicesTextViewClicked(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new servicesFragment()).commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spartner_search,menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(listener!=null) {
                    listener.tech_txtsearch(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(listener!=null) {
                    listener.tech_txtsearch(newText);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void setSearchFragmentListener(searchInterface listener) {
        this.listener = listener;
    }
    public interface searchInterface{
        void tech_txtsearch(String newText);
        //void immob_txtsearch(String newText);
    }
    @Override
    public void onBackPressed() {
        // Start the LoginActivity and clear the back stack to prevent going back to MainAccueil
        Intent intent = new Intent(this, MainAccueil.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // Optional: Finish the MainAccueil activity if you don't want it to remain in the back stack
    }
}