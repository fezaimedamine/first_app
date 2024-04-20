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
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private profileAdapter profileAdapter;
    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        recyclerView=view.findViewById(R.id.rv_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        /*FirebaseRecyclerOptions<product_model> options =
                new FirebaseRecyclerOptions.Builder<product_model>()
                        .setQuery(FirebaseFirestore.getInstance().collection("technology"),product_model.class)
                        .build();
                        */
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        FirebaseRecyclerOptions<profileModel> options =
                new FirebaseRecyclerOptions.Builder<profileModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("profiles"),profileModel.class)
                        .build();
        profileAdapter=new profileAdapter(options,getContext());
        recyclerView.setAdapter(profileAdapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        profileAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        profileAdapter.stopListening();
    }

}