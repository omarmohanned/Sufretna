package com.example.sufretna;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class my_orders extends AppCompatActivity {
    private RecyclerView all_my_orders;
    private DatabaseReference databaseReference;
    private List<retrive_favouret> retrive_favouret;
    private my_orders_adapter my_orders_adapter;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        all_my_orders = findViewById(R.id.all_my_orders);
        all_my_orders.setHasFixedSize(true);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        all_my_orders.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        retrive_favouret = new ArrayList<>();
        retrieve_class();
    }
    public void retrieve_class() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(firebaseUser.getUid()).child("orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                retrive_favouret.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    retrive_favouret retrive_image = ds.getValue(com.example.sufretna.retrive_favouret.class);
                    retrive_favouret.add(retrive_image);
                }
                my_orders_adapter = new my_orders_adapter(my_orders.this, retrive_favouret);
                all_my_orders.setAdapter(my_orders_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}