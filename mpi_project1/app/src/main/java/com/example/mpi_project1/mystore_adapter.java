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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;
import android.view.LayoutInflater;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.internal.cache.DiskLruCache;

import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class mystore_adapter extends FirebaseRecyclerAdapter<product_model,mystore_adapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public mystore_adapter(FirebaseRecyclerOptions<product_model> options, Context context) {
        super(options);
        this.context=context;
    }
    @Override
    protected void onBindViewHolder(myViewHolder holder, final int position, product_model model) {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrix());
        Glide.with(holder.img.getContext())
                .load(model.getPurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        holder.btnsponsorship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Free Sponsorship for a week", Toast.LENGTH_SHORT).show();
                DialogPlus dialogPlus;
                dialogPlus= DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.edit_product))
                .setExpanded(true,1500)
                .create();
                dialogPlus.show();
                EditText name=dialogPlus.getHolderView().findViewById(R.id.edit_name);
                EditText email=dialogPlus.getHolderView().findViewById(R.id.edit_email);
                EditText price=dialogPlus.getHolderView().findViewById(R.id.edit_price);
                EditText imageurl=dialogPlus.getHolderView().findViewById(R.id.edit_imageurl);
                EditText description=dialogPlus.getHolderView().findViewById(R.id.edit_description);
                Button btnUpdate=dialogPlus.getHolderView().findViewById(R.id.btnupdate);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, ""+name.getText().toString(), Toast.LENGTH_SHORT).show();
                        Map<String,Object> newMap=new HashMap<>();
                        newMap.put("id",model.getId());
                        newMap.put("domain",model.getDomain());
                        newMap.put("name", name.getText().toString());
                        newMap.put("email", email.getText().toString());
                        newMap.put("prix", price.getText().toString());
                        newMap.put("discription", description.getText().toString());
                        newMap.put("imageurl_profile",imageurl.getText().toString());
                        FirebaseFirestore.getInstance().collection(model.getDomain()).document(model.getId())
                                .update(newMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        //Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                        FirebaseUser user=firebaseAuth.getCurrentUser();
                        FirebaseDatabase.getInstance().getReference()
                                .child("mystore")
                                .child("mystore"+user.getUid())
                                .child(getRef(position).getKey())
                                .updateChildren(newMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mystore, parent, false);
        return new myViewHolder(view);
    }
    class myViewHolder  extends RecyclerView.ViewHolder {
        private final Button btndelete_product;
        TextView name, email,price;
        CircleImageView img;
        Button btninfo,btnsponsorship;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.snametext);
            price=(TextView)itemView.findViewById(R.id.spricetext);
            img=(CircleImageView)itemView.findViewById(R.id.store_img1);
            btndelete_product =(Button)itemView.findViewById(R.id.sbtndelete_basket);
            btnsponsorship =(Button)itemView.findViewById(R.id.sbtn_edit);
        }
    }}

