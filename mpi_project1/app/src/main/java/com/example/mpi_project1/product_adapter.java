package com.example.mpi_project1;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.jetbrains.annotations.NotNull;
import android.view.LayoutInflater;

import de.hdodenhof.circleimageview.CircleImageView;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class product_adapter extends FirestoreRecyclerAdapter<product_model,product_adapter.myViewHolder> {
    List<String> list_of_images=new ArrayList<>();
    Context context;
    String email;
    DialogPlus dialogPlus;
    private OnItemClickListener itemClickListener;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public product_adapter(FirestoreRecyclerOptions<product_model> options, Context context) {
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
       list_of_images= model.getList_of_images();
       holder.bindProductModel(model);
       holder.btnadd_bascket.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //startActivity(new Intent(context,MainActivity.class));
               Map<String,Object> map=new HashMap<>();
               map.put("name", model.getName());
               map.put("email", model.getEmail());
               map.put("price", model.getPrix());
               map.put("imgurl",model.getPurl());
               FirebaseAuth auth = FirebaseAuth.getInstance();
               FirebaseUser user = auth.getCurrentUser();
               if (user != null) {
                   String userId = user.getUid();
                   FirebaseDatabase.getInstance().getReference()
                           .child("panier")
                           .child(userId)
                           .push()
                           .setValue(map)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Toast.makeText(context, "Product added successfully to Bascket", Toast.LENGTH_SHORT).show();


                               }
                           }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(Exception e) {

                       }
                   });
                   // Use userId as needed
               }
           }
       });
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view=layoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemproduct, parent, false);
        return new myViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(int position,String email,String des,String Name);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    class myViewHolder  extends RecyclerView.ViewHolder {
        TextView name, price;
        CircleImageView img;
        Button btnadd_bascket;
        String email_item,des_item,Name;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nametext);
            price=(TextView)itemView.findViewById(R.id.pricetext);
            img=(CircleImageView)itemView.findViewById(R.id.product_img1);
            btnadd_bascket=(Button)itemView.findViewById(R.id.btnadd_basket);
            //email_item=product_model.getName();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(getAdapterPosition(),email_item,des_item,Name);
                    }
                }
            });
        }
        public void bindProductModel(product_model model) {
            email_item = model.getEmail();
            des_item=model.getDiscription();
            Name=model.getName();
        }
    }}