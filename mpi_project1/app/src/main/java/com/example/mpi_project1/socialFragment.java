package com.example.mpi_project1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link socialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class socialFragment extends Fragment implements MainAccueil.searchInterface,product_adapter.OnItemClickListener{

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
    public socialFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment immobFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static socialFragment newInstance(String param1, String param2) {
        socialFragment fragment = new socialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_social, container, false);
        recyclerView=view.findViewById(R.id.rv_social);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirestoreRecyclerOptions<product_model> options =
                new FirestoreRecyclerOptions.Builder<product_model>()
                        .setQuery(FirebaseFirestore.getInstance().collection("social_assistance"), product_model.class)
                        .build();
        Product_adapter=new product_adapter(options,getContext());
        Product_adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(Product_adapter);
        mainAccueil=new MainAccueil();
        if (mainAccueil != null) {
            mainAccueil.setSearchFragmentListener(this);
        }
        FloatingActionButton floatingActionButton=(FloatingActionButton)view.findViewById(R.id.floatingActionButton5);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Mainadd.class);
                intent.putExtra("domain","social_assistance");
                startActivity(intent);
            }
        });
        return view;
    }
    public void onItemClick(int position,String email,String des,String Name){
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
                .collection("social_assistance")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            List<String> imageURLList = (List<String>) documentSnapshot.get("imageURL");
                            String documentId = documentSnapshot.getId();
                            if (imageURLList != null && !imageURLList.isEmpty() && documentSnapshot.get("email").equals(double_check_email) && documentSnapshot.get("name").equals(Name)) {
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