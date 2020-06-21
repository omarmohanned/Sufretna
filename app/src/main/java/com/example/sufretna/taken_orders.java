package com.example.sufretna;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class taken_orders extends AppCompatActivity {
    private RecyclerView taken_order_rec;
    private DatabaseReference databaseReference;
    private List<retrive_taken_orders> retrive_images;
    private taken_adapter taken_adapter;
    private FirebaseUser firebaseUser;
    InterstitialAd interstitialAd;
    private AdView adView_taken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken_orders);
        adView_taken=findViewById(R.id.adView_taken);
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-7812857004737916~7353460118");
        AdRequest adRequest=new AdRequest.Builder().build();
        adView_taken.loadAd(adRequest);
        taken_order_rec = findViewById(R.id.taken_order_rec);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        taken_order_rec.setHasFixedSize(true);
        taken_order_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        retrive_images = new ArrayList<>();
        retrieve_class();


    }

    public void retrieve_class() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("taken");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retrive_images.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    retrive_taken_orders retrive_image = ds.getValue(com.example.sufretna.retrive_taken_orders.class);
                    retrive_images.add(retrive_image);
                }
                taken_adapter = new taken_adapter(taken_orders.this, retrive_images);
                taken_order_rec.setAdapter(taken_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
