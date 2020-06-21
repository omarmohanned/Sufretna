package com.example.sufretna;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class sign_in extends AppCompatActivity implements sign_up.OnFragmentInteractionListener, sign_in_in.OnFragmentInteractionListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View sign_in;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        startService(new Intent(getApplicationContext(), sign_in.class));
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager2);



        tabLayout.addTab(tabLayout.newTab().setText("sign in"));
        tabLayout.addTab(tabLayout.newTab().setText("sing up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter_for_tabs adapter = new adapter_for_tabs(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
