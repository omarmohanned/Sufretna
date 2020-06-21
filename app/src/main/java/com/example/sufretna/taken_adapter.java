package com.example.sufretna;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class taken_adapter extends RecyclerView.Adapter<taken_adapter.imageviewholder> {
    private Context mcontext;
    private List<retrive_taken_orders> mlist;
    private Double lat, lon;
    private DatabaseReference firebaseDatabase, databaseReference2,databaseReference3;
    private FirebaseUser firebaseUser;

    public taken_adapter(Context mcontext, List<retrive_taken_orders> mlist) {
        this.mcontext = mcontext;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public imageviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.items_show_over_view_in_frag_home, parent, false);
        return new imageviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final imageviewholder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final retrive_taken_orders ret_bind = mlist.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        holder.take_order.setText("cancel order");
        holder.price.setText(ret_bind.getPrice()+"JD");
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3=FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("taken");
        holder.item_name.setText(ret_bind.getItem_name());////////////////HERE
        Picasso.get().load(ret_bind.getMimagetrl()).placeholder(R.drawable.food_cart_order).fit().into(holder.image_item);
        holder.place_item.setText(ret_bind.getGeo());
        holder.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("confirmation");
                alert.setMessage("check location");
                alert.setCancelable(false);
                alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            lat = Double.valueOf(ret_bind.getLat());
                            lon = Double.valueOf(ret_bind.getLon());

                            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat,lon);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                            mcontext.startActivity(intent);


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.create().show();


            }
        });
        holder.take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,"cancel order",Snackbar.LENGTH_LONG).show();
                AlertDialog.Builder cancel=new AlertDialog.Builder(mcontext);
                cancel.setIcon(R.drawable.ic_clear_black_24dp);
                cancel.setCancelable(false);
                cancel.setTitle("Delete Order");
                cancel.setMessage("Are you sure you want to delete the order??");
                cancel.setPositiveButton("Delete order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      //////some work
                        String upload_id = databaseReference2.push().getKey();
                        databaseReference2.child("orders").child(upload_id).child("geo").setValue(ret_bind.getGeo());

                        databaseReference2.child("orders").child(upload_id).child("price").setValue(ret_bind.getPrice());

                        databaseReference2.child("orders").child(upload_id).child("lat").setValue(ret_bind.getLat());
                        databaseReference2.child("orders").child(upload_id).child("lon").setValue(ret_bind.getLon());
                        databaseReference2.child("orders").child(upload_id).child("place_name").setValue(ret_bind.getPlace_name());
                        databaseReference2.child("orders").child(upload_id).child("mimagetrl").setValue(ret_bind.getMimagetrl());
                        databaseReference2.child("orders").child(upload_id).child("mname").setValue(ret_bind.getItem_name());
                        databaseReference2.child("orders").child(upload_id).child("price").setValue(ret_bind.getPrice());
//check the price and remove from the fire base from taken orders

                        databaseReference2.child("orders").child(upload_id).child("uid_order").setValue(upload_id);


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();


            }
        });////////////////////////////////////////////
        holder.add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).setValue(null);


                firebaseDatabase.child(firebaseUser.getUid()).child("fav_orders_uid").child(ret_bind.getUid_order()).setValue(null);

                final ProgressDialog progressDialog = new ProgressDialog(mcontext);
                progressDialog.setCancelable(false);
                progressDialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class imageviewholder extends RecyclerView.ViewHolder {
        public TextView item_name, place_item,price;
        public ImageView image_item, add_to_fav, maps;
        public Button take_order;


        public imageviewholder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            place_item = itemView.findViewById(R.id.place_item);
            image_item = itemView.findViewById(R.id.image_item);
            take_order = itemView.findViewById(R.id.take_order);
            add_to_fav = itemView.findViewById(R.id.add_to_fav);
            price = itemView.findViewById(R.id.price);


            maps = itemView.findViewById(R.id.maps);
        }
    }
}
