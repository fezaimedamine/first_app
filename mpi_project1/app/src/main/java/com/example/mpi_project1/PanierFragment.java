package com.example.mpi_project1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PanierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PanierFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private panier_adapter panier_adapter;

    public PanierFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PanierFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PanierFragment newInstance(String param1, String param2) {
        PanierFragment fragment = new PanierFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_panier, container, false);
        recyclerView=view.findViewById(R.id.rv_panier);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        FirebaseRecyclerOptions<panier_model> options =
                new FirebaseRecyclerOptions.Builder<panier_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("panier").child(userId),panier_model.class)
                        .build();
        panier_adapter=new panier_adapter(options,getContext());
        recyclerView.setAdapter(panier_adapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        panier_adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        panier_adapter.stopListening();
    }
}