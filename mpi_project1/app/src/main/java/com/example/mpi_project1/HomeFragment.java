package com.example.mpi_project1;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView tech;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HomeFragmentListener listener;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        //view.setOnClickListener(this);
        TextView tech=view.findViewById(R.id.tech);
        tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onTechTextViewClicked();
                }            }

        });
        TextView immob=view.findViewById(R.id.immoblier);
        immob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onImmobTextViewClicked();
                }            }

        });
        TextView gaming=view.findViewById(R.id.gaming);
        gaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onGamingTextViewClicked();
                }            }

        });
        TextView clubs=view.findViewById(R.id.club);
        clubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClubsTextViewClicked();
                }            }

        });
        TextView social=view.findViewById(R.id.social_assistance);
        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onSocialTextViewClicked();
                }            }

        });
        TextView services=view.findViewById(R.id.services);
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int viewId=view.getId();
                    if(R.id.services==viewId) {
                        Toast.makeText(getContext(), "" + viewId, Toast.LENGTH_SHORT).show();
                    }
                    listener.onServicesTextViewClicked();
                }            }

        });
        return view;
    }
    /*@Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.tech:
            case R.id.gaming:
            case R.id.club:
            case R.id.social_assistance:
            case R.id.services:
            case R.id.immoblier:
                // Handle the click event for the specific views
                listener.onSocialTextViewClicked();
                break;
            default:
                // Handle the default click event for the root view
                // This will be triggered if none of the specific views are clicked.
                break;
        }
    }*/
    public void setHomeFragmentListener(HomeFragmentListener listener) {
        this.listener = listener;
    }
    public interface HomeFragmentListener{
        void onTechTextViewClicked();
        void onImmobTextViewClicked();
        void onGamingTextViewClicked();
        void onClubsTextViewClicked();
        void onSocialTextViewClicked();
        void onServicesTextViewClicked();
    }
}