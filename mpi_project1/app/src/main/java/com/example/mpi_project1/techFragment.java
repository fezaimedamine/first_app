package com.example.mpi_project1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link techFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class techFragment extends Fragment implements MainAccueil.searchInterface,product_adapter.OnItemClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2,double_check_email;
    private RecyclerView recyclerView;
    private product_adapter Product_adapter;
    public MainAccueil mainAccueil;
    private List<Uri> list_test=new ArrayList<>();
    private DialogPlus dialogPlus;
    public techFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment techFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static techFragment newInstance(String param1, String param2) {
        techFragment fragment = new techFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tech, container, false);
        recyclerView=view.findViewById(R.id.rv_tech);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        /*FirebaseRecyclerOptions<product_model> options =
                new FirebaseRecyclerOptions.Builder<product_model>()
                        .setQuery(FirebaseFirestore.getInstance().collection("technology"),product_model.class)
                        .build();
                        */

        FirestoreRecyclerOptions<product_model> options =
                new FirestoreRecyclerOptions.Builder<product_model>()
                .setQuery(FirebaseFirestore.getInstance().collection("technology"), product_model.class)
                .build();

        Product_adapter=new product_adapter(options,getContext());
        Product_adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(Product_adapter);
        mainAccueil=new MainAccueil();
        if (mainAccueil != null) {
            mainAccueil.setSearchFragmentListener(this);
            //Toast.makeText(getContext(), "bism ALLAH"+this , Toast.LENGTH_SHORT).show();
        }
        FloatingActionButton floatingActionButton=(FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Mainadd.class);
                intent.putExtra("domain","technology");
                startActivity(intent);
            }
        });
        return view;
    }
    public void onItemClick(int position,String email,String des,String Name){
        //startActivity(new Intent(getContext(),Maindetails.class));
        dialogPlus = DialogPlus.newDialog(getContext())
                .setContentHolder(new ViewHolder(R.layout.activity_details))
                .setExpanded(true, 1500)
                .create();
        dialogPlus.show();
        double_check_email=email;
        TextView textView=(TextView)dialogPlus.getHolderView().findViewById(R.id.text_email_product);
        textView.setText("Contact : "+email);
        TextView desView=(TextView)dialogPlus.getHolderView().findViewById(R.id.text_des_product);
        desView.setText(des);
        FirebaseFirestore.getInstance()
                .collection("technology")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            List<String> imageURLList = (List<String>) documentSnapshot.get("imageURL");
                            String documentId = documentSnapshot.getId();
                            if (imageURLList != null && !imageURLList.isEmpty() && documentSnapshot.get("email").equals(double_check_email) && documentSnapshot.get("name").equals(Name)) {
                                //String imageUrl = imageURLList.get(0);
                                // Load image from Firebase Storage using Glide
                                RecyclerView recyclerView = dialogPlus.getHolderView().findViewById(R.id.rv_product);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
                                recyclerView.setLayoutManager(layoutManager);
                                MyAdapter_image adapter = new MyAdapter_image(imageURLList);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        Product_adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        Product_adapter.stopListening();
    }
    @Override
    public void tech_txtsearch(String str){
        FirestoreRecyclerOptions<product_model> options =
                new FirestoreRecyclerOptions.Builder<product_model>()
                        .setQuery(FirebaseFirestore.getInstance().collection("technology").startAt(str).endAt(str+"~").orderBy("price"),product_model.class)
                        .build();
        Product_adapter=new product_adapter(options,getContext());
        Product_adapter.startListening();
        recyclerView.setAdapter(Product_adapter);
    }
}