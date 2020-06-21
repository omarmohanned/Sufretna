package com.example.sufretna;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private RecyclerView rec_items;
    private DatabaseReference databaseReference, databaseReference1, databaseReference2;
    private List<retrive_image> retrive_images;
    private retrieve_order_data_base retrieve_order_data_base;
    private FirebaseUser firebaseUser;
    private String loc, stat, place_name;
    private AdView adView_main;


    private Handler mhandler = new Handler();
    private Runnable code_exec = new Runnable() {
        @Override
        public void run() {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm:ss");
            date.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));
            String localtime = date.format(currentLocalTime);
            databaseReference2 = FirebaseDatabase.getInstance().getReference();



            mhandler.postDelayed(this, 5000);
        }
    };

    public void startrepeting() {
        code_exec.run();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        startrepeting();

        rec_items = view.findViewById(R.id.rec_items);
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("choplace");

        adView_main = view.findViewById(R.id.adView_main);
        MobileAds.initialize(getContext(), "ca-app-pub-7812857004737916~7353460118");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView_main.loadAd(adRequest);


        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loc = dataSnapshot.getValue(String.class);
                Toast.makeText(getContext(), "In " + loc, Toast.LENGTH_LONG).show();
                retrieve_class();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "getting the information needed", Snackbar.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(), take_location.class);
                        if (loc.equals("AMMAN")) {
                            intent.putExtra("lat", "31.945368");
                            intent.putExtra("lon", "35.928371");
                            startActivity(intent);
                        } else if (loc.equals("IRBID")) {
                            intent.putExtra("lat", "32.556957");
                            intent.putExtra("lon", "35.847908");
                            startActivity(intent);
                        } else if (loc.equals("AQABA")) {
                            intent.putExtra("lat", "29.533991");
                            intent.putExtra("lon", "35.005020");
                            startActivity(intent);
                        }


                    }
                }, 2000);


            }


        });


        rec_items.setHasFixedSize(true);
        rec_items.setLayoutManager(new LinearLayoutManager(getContext()));
        retrive_images = new ArrayList<>();

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    public void retrieve_class() {
        databaseReference = FirebaseDatabase.getInstance().getReference("orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retrive_images.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    place_name = String.valueOf(ds.child("place_name").getValue());
                    if (place_name.equals(loc)) {
                        retrive_image retrive_image = ds.getValue(com.example.sufretna.retrive_image.class);
                        retrive_images.add(retrive_image);
                    }
                }
                retrieve_order_data_base = new retrieve_order_data_base(getContext(), retrive_images);
                rec_items.setAdapter(retrieve_order_data_base);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
