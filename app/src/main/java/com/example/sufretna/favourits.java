package com.example.sufretna;

import android.os.Bundle;

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

public class favourits extends AppCompatActivity {
    private fav_adapter fav_adapter;
    private RecyclerView recyclerView_fav;
    private DatabaseReference databaseReference1,databaseReference;
    private FirebaseUser firebaseUser;
    private List<retrive_favouret> retrive_images;
    private List<String> fav_uid;
    InterstitialAd interstitialAd;
    private AdView adView_fav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourits);
        adView_fav=findViewById(R.id.adView_fav);
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-7812857004737916~7353460118");
        AdRequest adRequest=new AdRequest.Builder().build();
        adView_fav.loadAd(adRequest);

        fav_uid = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView_fav = findViewById(R.id.recyclerView_fav);
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("fav");
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("fav_orders_uid");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    fav_uid.add(ds.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView_fav.setHasFixedSize(true);
        recyclerView_fav.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        retrive_images = new ArrayList<>();

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    retrive_favouret ret = ds.getValue(retrive_favouret.class);
                    retrive_images.add(ret);

                }
                fav_adapter = new fav_adapter(favourits.this, retrive_images);
                recyclerView_fav.setAdapter(fav_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
