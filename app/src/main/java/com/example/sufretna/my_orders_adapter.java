package com.example.sufretna;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class my_orders_adapter extends RecyclerView.Adapter<my_orders_adapter.imageviewholder> {
    private Context mcontext;
    private List<retrive_favouret> mlist;
    private double lat, lon;

    private DatabaseReference firebaseDatabase, databaseReference2;
    private FirebaseUser firebaseUser;

    public my_orders_adapter(Context mcontext, List<retrive_favouret> mlist) {
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
        holder.item_name.setText(ret_bind.getMname());
        Picasso.get().load(ret_bind.getMimagetrl()).placeholder(R.drawable.food_cart_order).fit().into(holder.image_item);
        holder.image_item.setVisibility(View.INVISIBLE);
        holder.place_item.setText(ret_bind.getName_of_order());
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
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
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
        holder.take_order.setText("cancel order");
        holder.take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String uid_name = ret_bind.getUid_order();
                firebaseDatabase.child(firebaseUser.getUid()).child("orders").child(uid_name).setValue(null);
                firebaseDatabase.child("orders").child(uid_name).setValue(null);


            }
        });////////////////////////////////////////////
        holder.show_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog show_image=new Dialog(mcontext);
                show_image.setContentView(R.layout.show_photo);
                ImageView imageView2=show_image.findViewById(R.id.imageView2);
                //imageView2.setImageAlpha(ret_bind.getMimagetrl());
                TextView place_name=show_image.findViewById(R.id.place_name);
                place_name.setText(ret_bind.getGeo());
                show_image.show();
            }
        });
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
        public Button take_order,show_order;


        public imageviewholder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            place_item = itemView.findViewById(R.id.place_item);
            image_item = itemView.findViewById(R.id.image_item);
            take_order = itemView.findViewById(R.id.take_order);
            add_to_fav = itemView.findViewById(R.id.add_to_fav);
            text_order_time = itemView.findViewById(R.id.text_order_time);
            show_order = itemView.findViewById(R.id.show_order);
            price = itemView.findViewById(R.id.price);

            maps = itemView.findViewById(R.id.maps);
        }
    }
}
