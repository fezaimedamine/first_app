package com.example.mpi_project1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link storeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class storeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private mystore_adapter store_adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public storeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment storeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static storeFragment newInstance(String param1, String param2) {
        storeFragment fragment = new storeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_store, container, false);
        recyclerView=view.findViewById(R.id.rv_mystore);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        /*FirebaseRecyclerOptions<product_model> options =
                new FirebaseRecyclerOptions.Builder<product_model>()
                        .setQuery(FirebaseFirestore.getInstance().collection("technology"),product_model.class)
                        .build();
                        */
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        FirebaseRecyclerOptions<product_model> options =
                new FirebaseRecyclerOptions.Builder<product_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("mystore").child("mystore"+userId),product_model.class)
                        .build();
        store_adapter=new mystore_adapter(options,getContext());
        recyclerView.setAdapter(store_adapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        store_adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        store_adapter.stopListening();
    }

}