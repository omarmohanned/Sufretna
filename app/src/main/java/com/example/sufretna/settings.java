package com.example.sufretna;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaRouter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class settings extends AppCompatActivity {
    public String email;
    private TextView  get_name, get_email, get_phone;
    private Button change_pass;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private EditText email_pass_change;
    private Button button2, checkBox;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        get_name = findViewById(R.id.get_name);
        get_email = findViewById(R.id.get_email);
        change_pass = findViewById(R.id.change_pass);
        get_phone = findViewById(R.id.get_phone);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;


        databaseReference.child(firebaseUser.getUid()).child("username").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                get_name.setText( dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child(firebaseUser.getUid()).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                get_email.setText( dataSnapshot.getValue(String.class));
                email = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child(firebaseUser.getUid()).child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                get_phone.setText( dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        change_pass.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(settings.this);
                dialog.setTitle("Forget password");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.forget_pass);
                email_pass_change = dialog.findViewById(R.id.email_pass_change);
                button2 = dialog.findViewById(R.id.button2);
                checkBox = dialog.findViewById(R.id.checkBox);
                email_pass_change.setText(email);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (!email_pass_change.getText().toString().isEmpty()) {
                            firebaseAuth.sendPasswordResetEmail(email_pass_change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(view, "please check your email", Snackbar.LENGTH_LONG).show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        }, 500);
                                    } else {
                                        Snackbar.make(view, "please provide a valid email ", Snackbar.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                }
                            });
                        } else {
                            Snackbar.make(view, "Enter Email ", Snackbar.LENGTH_LONG).show();
                            email_pass_change.setHintTextColor(getResources().getColor(R.color.colorAccent));
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();
            }
        });
        get_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (get_phone.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No Phone Number", Toast.LENGTH_LONG).show();
                } else {
                    Uri uri=Uri.parse("tel:"+get_phone.getText().toString());
                    Intent intent=new Intent(Intent.ACTION_DIAL);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
