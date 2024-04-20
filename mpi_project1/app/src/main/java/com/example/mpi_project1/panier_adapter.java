package com.example.mpi_project1;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

public class panier_adapter extends FirebaseRecyclerAdapter<panier_model,panier_adapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public panier_adapter(FirebaseRecyclerOptions<panier_model> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(myViewHolder holder, final int position, panier_model model) {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        holder.btndelete_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                FirebaseDatabase.getInstance().getReference().child("panier").child(user.getUid())
                        .child(getRef(position).getKey())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Product removed successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view=layoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_panier, parent, false);
        return new myViewHolder(view);
    }


    class myViewHolder  extends RecyclerView.ViewHolder {
        private final Button btndelete_product;
        TextView name, email,price;
        CircleImageView img;
        Button btninfo;

        public myViewHolder(@NonNull View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.pnametext);
            price=(TextView)itemView.findViewById(R.id.ppricetext);
            img=(CircleImageView)itemView.findViewById(R.id.panier_img1);
            btndelete_product =(Button)itemView.findViewById(R.id.btndelete_basket);

        }
    }}
