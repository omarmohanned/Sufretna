package com.example.sufretna;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class sign_up extends Fragment {


    public boolean valdite_num = true;
    CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private EditText user_name, email, pass, phone_number, code;
    private Button connect, confirm;
    private LoginButton face_book_sign_up;
    private TextView terms;
    private LinearLayout signup_lay;
    private PhoneAuthProvider phoneAuthProvider;
    private String phoneverf,phone;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    public sign_up() {
        // Required empty public constructor
    }

    public static sign_up newInstance(String param1, String param2) {
        sign_up fragment = new sign_up();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        }

    }

    private void updateUI() {
        Toast.makeText(getContext(), "you are logged in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        callbackManager = CallbackManager.Factory.create();

        user_name = view.findViewById(R.id.user_name);
        face_book_sign_up = view.findViewById(R.id.face_book_sign_up);
        face_book_sign_up.setReadPermissions("email", "public_profile");
        email = view.findViewById(R.id.email);

        pass = view.findViewById(R.id.pass);
        connect = view.findViewById(R.id.connect);
        phone_number = view.findViewById(R.id.phone_number);
        terms = view.findViewById(R.id.terms);
        signup_lay = view.findViewById(R.id.signup_lay);
        phone_number.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Snackbar.make(view, "Enter 10 digits", Snackbar.LENGTH_LONG).show();
                if (phone_number.getText().toString().length() == 10 && valdite_num) {
                    Snackbar.make(view, "wait for your code", Snackbar.LENGTH_LONG).show();

                    sendverficationcodephone(phone_number.getText().toString().trim());
                    final ProgressDialog verfyy = new ProgressDialog(getContext());
                    verfyy.setMessage("verifying");
                    verfyy.setCancelable(false);
                    verfyy.show();
                    valdite_num = false;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            verfyy.dismiss();
                        }
                    }, 3000);

                    try {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneverf, "123456");
                        signinwithcredentaial(credential);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Verification Code is wrong", Toast.LENGTH_SHORT);
                    }


                }

                return false;
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Snackbar.make(view, "please fill all the information", Snackbar.LENGTH_LONG).show();
                    if (user_name.getText().toString().isEmpty()) {
                        user_name.setHint("Enter your name ");
                    } else if (email.getText().toString().isEmpty()) {
                        email.setHint("Enter your Email ");

                    } else if (pass.getText().toString().isEmpty()) {
                        pass.setHint("Enter your Password ");

                    } else if (pass.getText().toString().length() < 9) {
                        Snackbar.make(view, "password is too short should be 9 characters", Snackbar.LENGTH_LONG).show();
                    } else if (phone_number.getText().toString().isEmpty()) {
                        phone_number.setHint("Enter your Phone number ");
                    }

                } else if (pass.getText().toString().contains("@") || pass.getText().toString().contains("#") || pass.getText().toString().contains("$")) {
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        phone=phone_number.getText().toString();
                                        phone=phone.substring(1);
                                        phone="+962"+phone;

                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(getContext(), "sign up successfully", Toast.LENGTH_LONG).show();
                                        reference.child(firebaseUser.getUid()).child("username").setValue(user_name.getText().toString());
                                        reference.child(firebaseUser.getUid()).child("email").setValue(email.getText().toString());
                                        reference.child(firebaseUser.getUid()).child("phone").setValue(phone);
                                        reference.child(firebaseUser.getUid()).child("choplace").setValue("AQABA");
                                        reference.child(firebaseUser.getUid()).child("fav_orders_uid").setValue(0);
                                        reference.child(firebaseUser.getUid()).child("boolan_fav").setValue(true);


                                    } else {
                                        String problem = task.getException().getMessage();
                                        Toast.makeText(getContext(), "                  failed to sign up\n" + problem, Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                } else if (!pass.getText().toString().contains("@") || !pass.getText().toString().contains("#") || !pass.getText().toString().contains("$")) {
                    Snackbar.make(view, "password should contain  @ or # or $", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        face_book_sign_up.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                updateUI();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    private void signinwithcredentaial(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "phone verified", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendverficationcodephone(String phone_numer_taken) {
        String phone_number = phone_numer_taken;
        Toast.makeText(getContext(), phone_number, Toast.LENGTH_LONG).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone_number, 100, TimeUnit.SECONDS,
                getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        phoneverf = s;
                        resendingToken = forceResendingToken;

                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                    }
                });

    }


    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI();

                } else {


                    Toast.makeText(getContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();

                    updateUI();
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
