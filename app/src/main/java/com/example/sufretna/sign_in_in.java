package com.example.sufretna;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class sign_in_in extends Fragment {
    private FirebaseAuth firebaseAuth, firebaseAuth1;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private EditText email1, email_pass_change;
    private EditText pass1;
    private Button connect1, button2, checkBox;
    private TextView terms;


    public sign_in_in() {

    }

    public static sign_in_in newInstance(String param1, String param2) {
        sign_in_in fragment = new sign_in_in();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null ) {
            updateUI();
        }

    }

    private void updateUI() {
        Toast.makeText(getContext(), "you are logged in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        email1 = view.findViewById(R.id.email1);
        pass1 = view.findViewById(R.id.pass1);
        connect1 = view.findViewById(R.id.connect1);
        terms = view.findViewById(R.id.terms);
        firebaseAuth1 = FirebaseAuth.getInstance();
        terms.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(final View view1) {
                final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
                dialog.setTitle("Forget password");
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.forget_pass);
                email_pass_change = dialog.findViewById(R.id.email_pass_change);
                button2 = dialog.findViewById(R.id.button2);
                checkBox = dialog.findViewById(R.id.checkBox);
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
                            firebaseAuth1.sendPasswordResetEmail(email_pass_change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(view1, "please check your email", Snackbar.LENGTH_LONG).show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                            }
                                        }, 500);
                                    } else {
                                        Snackbar.make(view1, "please provide a valid email ", Snackbar.LENGTH_LONG).show();
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
        connect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (email1.getText().toString().isEmpty() || pass1.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "fail to log in", Toast.LENGTH_LONG).show();
                } else {

                    firebaseAuth.signInWithEmailAndPassword(email1.getText().toString(), pass1.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                firebaseUser.getUid();
                                if (firebaseUser.isEmailVerified()) {
                                    startActivity(new Intent(getContext(), MainActivity.class));
                                    getActivity().finish();
                                }else{

                                    Snackbar.make(view,"please verify your account",Snackbar.LENGTH_LONG).show();
                                }


                            } else {
                                String problem = task.getException().getMessage().substring(30);
                                Toast.makeText(getContext(), "                  failed to sign up\n" + problem, Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                }


            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sign_in_in, container, false);
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
