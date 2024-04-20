package com.example.mpi_project1;

import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DelivreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DelivreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private delivre_adapter Delivre_adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public DelivreFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DelivreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DelivreFragment newInstance(String param1, String param2) {
        DelivreFragment fragment = new DelivreFragment();
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
        //recyclerView=(R.id.rv_delivre);

    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_delivre2, container, false);
        recyclerView=view.findViewById(R.id.rv_delivre);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<delivre_Model> options =
                new FirebaseRecyclerOptions.Builder<delivre_Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("delivery"),delivre_Model.class)
                        .build();
        Delivre_adapter=new delivre_adapter(options);
        recyclerView.setAdapter(Delivre_adapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        Delivre_adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Delivre_adapter.stopListening();
    }
}