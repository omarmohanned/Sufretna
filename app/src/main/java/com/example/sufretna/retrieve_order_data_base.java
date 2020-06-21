package com.example.sufretna;

import android.annotation.SuppressLint;
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

import com.google.android.gms.ads.internal.overlay.zzo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Locale;


public class retrieve_order_data_base extends RecyclerView.Adapter<retrieve_order_data_base.imageviewholder> {
    private Context mcontext;
    private List<retrive_image> mlist;
    private Double lat, lon;
    private DatabaseReference firebaseDatabase, databaseReference, databaseReference1, databaseReference2,databaseReference3;
    private FirebaseUser firebaseUser;
    private List<String> fav_uid;
    private boolean first_order_check;
    private  Date date_post;

    public retrieve_order_data_base(Context mcontext, List<retrive_image> mlist) {
        this.mcontext = mcontext;
        this.mlist = mlist;
    }

    public retrieve_order_data_base(Context mcontext, List<retrive_image> mlist, String lat, String lon) {
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
        fav_uid = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("fav_orders_uid");
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("boolan_fav");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("orders");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                first_order_check = dataSnapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final retrive_image ret_bind = mlist.get(position);
        holder.item_name.setText(ret_bind.getMname());
        holder.text_order_time.setText(ret_bind.getDate());





        holder.add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fav_uid.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            fav_uid.add(ds.getValue(String.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
                alert.setCancelable(false);
                alert.setTitle("ADD TO FAVORITE").setMessage("Add to Favorite?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (first_order_check) {
                            databaseReference1.setValue(false);
                            add_to_database();


                        } else {


                            boolean a = false;
                            for (int y = 0; y < fav_uid.size(); y++) {
                                a = ret_bind.getUid_order().equals(fav_uid.get(y));
                                if (a) {
                                    break;
                                }
                            }

                            if (a) {
                                Toast.makeText(mcontext, "this order has been added before", Toast.LENGTH_LONG).show();
                            } else {
                                add_to_database();
                            }
                        }


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }

            @SuppressLint("ResourceAsColor")
            private void add_to_database() {
                String upload_id = databaseReference3.push().getKey();

                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("item_name").setValue(holder.item_name.getText().toString());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("geo").setValue(holder.place_item.getText().toString());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("lat").setValue(ret_bind.getLat());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("lon").setValue(ret_bind.getLon());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("mimagetrl").setValue(ret_bind.getMimagetrl());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("place_name").setValue(ret_bind.getPlace_name());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("stat").setValue(ret_bind.getStat());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("uid_order").setValue(ret_bind.getUid_order());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav_orders_uid").child(ret_bind.getUid_order()).setValue(ret_bind.getUid_order());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("date").setValue(ret_bind.getDate());
                firebaseDatabase.child(firebaseUser.getUid()).child("fav").child(ret_bind.getUid_order()).child("price").setValue(ret_bind.getPrice());


            }
        });

        holder.price.setText(ret_bind.getPrice()+" JD");

        Picasso.get().load(ret_bind.getMimagetrl()).placeholder(R.drawable.food_cart_order).fit().into(holder.image_item);
        holder.image_item.setVisibility(View.INVISIBLE);
        holder.place_item.setText(ret_bind.getName_of_order());
        holder.maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
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

        holder.take_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
                alert.setTitle("Take order").setMessage("Are you sure you will add this order:" + " (" + ret_bind.getMname() + " )" + "that is located at:\n"
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
                                        String upload_id = databaseReference3.push().getKey();
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("item_name").setValue(holder.item_name.getText().toString());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("geo").setValue(holder.place_item.getText().toString());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("lat").setValue(ret_bind.getLat());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("lon").setValue(ret_bind.getLon());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("mimagetrl").setValue(ret_bind.getMimagetrl());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("place_name").setValue(ret_bind.getPlace_name());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("stat").setValue(ret_bind.getStat());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("uid_order").setValue(ret_bind.getUid_order());
                                        firebaseDatabase.child(firebaseUser.getUid()).child("taken").child(upload_id).child("uid_order").setValue(ret_bind.getUid_order());
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
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class imageviewholder extends RecyclerView.ViewHolder {
        public TextView item_name, place_item,price,text_order_time;
        public ImageView image_item, add_to_fav;
        public Button take_order,show_order;
        private ImageView maps;

        public imageviewholder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            place_item = itemView.findViewById(R.id.place_item);
            image_item = itemView.findViewById(R.id.image_item);
            take_order = itemView.findViewById(R.id.take_order);
            show_order = itemView.findViewById(R.id.show_order);
            add_to_fav = itemView.findViewById(R.id.add_to_fav);
            price = itemView.findViewById(R.id.price);
            maps = itemView.findViewById(R.id.maps);
            text_order_time=itemView.findViewById(R.id.text_order_time);


        }
    }
}
