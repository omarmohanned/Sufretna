package com.example.sufretna;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setCheckable(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        break;
                    case R.id.favourts:
                        startActivity(new Intent(getApplicationContext(), favourits.class));
                        break;
                    case R.id.nav_gallery:
                        startActivity(new Intent(getApplicationContext(), taken_orders.class));
                        break;
                    case R.id.my_orders:
                        startActivity(new Intent(getApplicationContext(), my_orders.class));
                        break;

                    case R.id.tutorial:
                        startActivity(new Intent(getApplicationContext(), tutorial.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), settings.class));
                        break;
                    case R.id.AMMAN:
                        databaseReference.child(firebaseUser.getUid()).child("choplace").setValue("AMMAN");
                        break;
                    case R.id.IRBID:
                        databaseReference.child(firebaseUser.getUid()).child("choplace").setValue("IRBID");
                        break;
                    case R.id.AQABA:
                        databaseReference.child(firebaseUser.getUid()).child("choplace").setValue("AQABA");
                        break;
                    case R.id.sign_out:
                        firebaseAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), sign_in.class));
                        finish();
                        break;
                }
                drawer.closeDrawers();
                return true;
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
