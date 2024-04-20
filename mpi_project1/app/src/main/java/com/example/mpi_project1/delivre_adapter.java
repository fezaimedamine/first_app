package com.example.mpi_project1;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;
import android.view.LayoutInflater;

import de.hdodenhof.circleimageview.CircleImageView;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class delivre_adapter extends FirebaseRecyclerAdapter<delivre_Model,delivre_adapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public delivre_adapter(FirebaseRecyclerOptions<delivre_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(myViewHolder holder, final int position, delivre_Model model) {
        holder.name.setText(model.getName());
        holder.email.setText(model.getEmail());
        holder.region.setText(model.getRegion());
        Glide.with(holder.img.getContext())
                .load(model.getDurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);

    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view=layoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivre, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder  extends RecyclerView.ViewHolder {
        TextView name, email,region;
        CircleImageView img;
        Button btninfo;

        public myViewHolder(@NonNull View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.jnametext);
            email=(TextView)itemView.findViewById(R.id.jemailtext);
            region=(TextView)itemView.findViewById(R.id.jregiontext);
            img=(CircleImageView)itemView.findViewById(R.id.jimg1);
            btninfo =(Button)itemView.findViewById(R.id.jbtninfo);

        }
    }}
