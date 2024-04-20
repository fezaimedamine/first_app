package com.example.mpi_project1;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;
import android.view.LayoutInflater;

import de.hdodenhof.circleimageview.CircleImageView;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class profileAdapter extends FirebaseRecyclerAdapter<profileModel,profileAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    int pposition;
    public profileAdapter(FirebaseRecyclerOptions<profileModel> options, Context context) {
        super(options);
        this.context=context;
    }
    @Override
    protected void onBindViewHolder(myViewHolder holder, final int position, profileModel model) {
        FirebaseAuth firebaseAut=FirebaseAuth.getInstance();
        //pposition=position;
        FirebaseUser firebaseUser=firebaseAut.getCurrentUser();
        holder.name.setText(model.getName());
        holder.position_holder=position;
        holder.fname.setText(model.getFname());
        Glide.with(holder.img.getContext())
                .load(model.getProfileimage())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        if(model.isState()){
            holder.imageViewOfState.setBackgroundColor(Color.parseColor("#08F80F"));
        }else holder.imageViewOfState.setBackgroundColor(Color.parseColor("#F82407"));
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
            return new myViewHolder(view);
    }
    class myViewHolder  extends RecyclerView.ViewHolder {
        CircleImageView img;
        TextView name,fname;
        ImageView imageViewOfState;
        int position_holder;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            position_holder=pposition;
            name=(TextView)itemView.findViewById(R.id.profilenametext);
            fname=(TextView)itemView.findViewById(R.id.profilefnametext);
            img=itemView.findViewById(R.id.profilechat_img1);
            imageViewOfState=itemView.findViewById(R.id.profilestateimg);
        }
    }}