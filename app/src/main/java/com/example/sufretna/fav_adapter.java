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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class fav_adapter extends RecyclerView.Adapter<fav_adapter.imageviewholder> {
    private Context mcontext;
    private List<retrive_favouret> mlist;
    private double lat, lon;

    private DatabaseReference firebaseDatabase, databaseReference2;
    private FirebaseUser firebaseUser;

    public fav_adapter(Context mcontext, List<retrive_favouret> mlist) {
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
        final retrive_favouret ret_bind = mlist.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("orders");
        holder.item_name.setText(ret_bind.getItem_name());
        Picasso.get().load(ret_bind.getMimagetrl()).placeholder(R.drawable.food_cart_order).fit().into(holder.image_item);
        holder.image_item.setVisibility(View.INVISIBLE);
        holder.place_item.setText(ret_bind.getGeo());
        holder.price.setText(ret_bind.getPrice()+"JD");
        holder.text_order_time.setText(ret_bind.getDate());
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
                final String uid_name = ret_bind.getUid_order();
                final AlertDialog.Builder alert = new AlertDialog.Builder(mcontext).setMessage("Do you wish to take that order").setTitle("confirmation")
                        .setCancelable(false).setPositiveButton("take order", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
                                alert.setTitle("Take order").setMessage("Are you sure you will add this order:" + " (" + ret_bind.getItem_name() + " )" + "that is located at:\n"
                                        + ret_bind.getGeo())
                                        .setPositiveButton("Add order", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                final ProgressDialog progressDialog = new ProgressDialog(mcontext);
                                                progressDialog.setTitle("adding order");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String upload_id = firebaseDatabase.push().getKey();
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("item_name").setValue(holder.item_name.getText().toString());
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("geo").setValue(holder.place_item.getText().toString());
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("lat").setValue(ret_bind.getLat());
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("lon").setValue(ret_bind.getLon());
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("mimagetrl").setValue(ret_bind.getMimagetrl());
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("place_name").setValue(ret_bind.getPlace_name());
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("stat").setValue(ret_bind.getStat());
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("uid_order").setValue(upload_id);
                                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("date").setValue(ret_bind.getDate());

                                                        String uid_taken_order = ret_bind.getUid_order();
                                                        databaseReference2.child(uid_taken_order).setValue(null);
                                                        progressDialog.cancel();
                                                    }
                                                }, 2000);


                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).setCancelable(false).setIcon(R.drawable.ic_check_black_24dp)
                                        .create().show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alert.create().show();


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
        public TextView item_name, place_item, price,text_order_time;
        public ImageView image_item, add_to_fav, maps;
        public Button take_order;


        public imageviewholder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            place_item = itemView.findViewById(R.id.place_item);
            image_item = itemView.findViewById(R.id.image_item);
            take_order = itemView.findViewById(R.id.take_order);
            add_to_fav = itemView.findViewById(R.id.add_to_fav);
            text_order_time = itemView.findViewById(R.id.text_order_time);

            price = itemView.findViewById(R.id.price);

            maps = itemView.findViewById(R.id.maps);
        }
    }
}
