package com.example.sufretna;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class take_location extends FragmentActivity implements OnMapReadyCallback {
    static final int requst_libary_picyure = 3;
    public Geocoder geocoder;
    public double lat_intent, lon_intent;
    public int number_of_times;
    List<Address> addresses;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String start_lat, fulladdress = null, start_lon, GEO_ADRESS, spinner_place_choosen, lat_intent_from, lon_intent_from, start_lat1, start_lon1, home_res_choosen;
    private String item_name_public, downuri,name_of_order;
    private GoogleMap mMap;
    private StorageReference storageReference;
    private Uri imageuri;
    private ImageView item_image;
    private TextView place_geo;
    private Button cancel_order;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser, firebaseUser1;
    private DatabaseReference databaseReference;
    private Long mili_second;
    private EditText item_price, item_quant;

    @Override
    protected void onStart() {
        location();
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        mMap = googleMap;
        lat_intent_from = getIntent().getStringExtra("lat");
        lon_intent_from = getIntent().getStringExtra("lon");

        lat_intent = Double.parseDouble(lat_intent_from);
        lon_intent = Double.parseDouble(lon_intent_from);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name_of_order=  dataSnapshot.child(firebaseUser.getUid()).child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final AlertDialog.Builder inform = new AlertDialog.Builder(take_location.this);
        inform.setIcon(R.drawable.location_order).setCancelable(false).setMessage("Touch and Hold to get location").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        LatLng amman = null;


        amman = new LatLng(lat_intent, lon_intent);
        mMap.setIndoorEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setTrafficEnabled(true);
        CountDownTimer loc = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long l) {
                Toast.makeText(getApplicationContext(), "please wait while we get your location", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFinish() {

            }
        }.start();
        mMap.addMarker(new MarkerOptions().position(amman).title("Food Place"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(amman));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(amman));
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                take_order();
            }
        });

    }

    private String getfileextinstion(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }


    public void onFragmentInteraction(Uri uri) {

    }

    private void location() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                start_lon = String.valueOf(location.getLongitude());
                start_lat = String.valueOf(location.getLatitude());


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
                return;
            } else {
                locationManager.requestLocationUpdates("gps", 1000, 5, locationListener);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "loading ", Toast.LENGTH_LONG).show();

        if (requestCode == 4) {
            Bitmap camera_image = (Bitmap) data.getExtras().get("data");
            item_image.setImageBitmap(camera_image);

        } else {
            imageuri = data.getData();
            item_image.setImageURI(imageuri);
        }
    }

    private void upload_file() {
        final ProgressDialog progressDialog = new ProgressDialog(take_location.this);
        if (item_image != null) {
            mili_second = System.currentTimeMillis();
            final StorageReference file_referance = storageReference.child(mili_second + "." + getfileextinstion(imageuri));/////problem
            file_referance.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            downuri = file_referance.getDownloadUrl().toString();
                            number_of_times = Integer.parseInt(item_quant.getText().toString());


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "upload successful", Toast.LENGTH_LONG).show();
                                }
                            }, 4000);

                            for (int i = 0; i < number_of_times; i++) {
                                upload_image upload_image = new upload_image(item_name_public.trim(), downuri);
                                String upload_id = databaseReference.push().getKey();
                                Toast.makeText(getApplicationContext(), imageuri.toString(), Toast.LENGTH_LONG).show();
                                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).setValue(upload_image);
                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("geo").setValue(GEO_ADRESS);
                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("lat").setValue(start_lat1);
                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("lon").setValue(start_lon1);

                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("price").setValue(item_price.getText().toString());

                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("place_name").setValue(spinner_place_choosen);
                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("stat").setValue("available");
                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("uid_order").setValue(upload_id);

                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("date").setValue(currentDateTimeString);
                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("source").setValue(home_res_choosen);
                                databaseReference.child(firebaseUser.getUid()).child("orders").child(upload_id).child("name_of_order").setValue(name_of_order);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////HERE ARE ALL ORDERS

                                databaseReference.child("orders").child(upload_id).setValue(upload_image);
                                databaseReference.child("orders").child(upload_id).child("geo").setValue(GEO_ADRESS);

                                databaseReference.child("orders").child(upload_id).child("price").setValue(item_price.getText().toString());

                                databaseReference.child("orders").child(upload_id).child("lat").setValue(start_lat1);
                                databaseReference.child("orders").child(upload_id).child("lon").setValue(start_lon1);
                                databaseReference.child("orders").child(upload_id).child("place_name").setValue(spinner_place_choosen);
                                databaseReference.child("orders").child(upload_id).child("stat").setValue("available");
                                databaseReference.child("orders").child(upload_id).child("uid_order").setValue(upload_id);

                                databaseReference.child("orders").child(upload_id).child("date").setValue(currentDateTimeString);

                                  databaseReference.child("orders").child(upload_id).child("name_of_order").setValue(name_of_order);


                            }

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setTitle("uploading your order");
                            progressDialog.setProgress((int) progress);
                            progressDialog.show();
                        }
                    });

        } else {
            Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_LONG).show();

        }

    }

    private void take_order() {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                double lat = latLng.latitude;
                double lan = latLng.longitude;

                start_lat1 = String.valueOf(lat);
                start_lon1 = String.valueOf(lan);

                final LatLng place2 = new LatLng(lat, lan);
                mMap.addMarker(new MarkerOptions().position(place2).title("Location Choosed"));


                try {

                    addresses = geocoder.getFromLocation(lat, lan, 10);
                    String address = addresses.get(0).getAddressLine(0);
                    String area = addresses.get(0).getLocality();
                    String city = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalcode = addresses.get(0).getPostalCode();

                    fulladdress = address + "," + city + "," + area + ",";
                    GEO_ADRESS = fulladdress;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                CountDownTimer countDownTimer = new CountDownTimer(100, 1000) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        final Dialog add_item = new Dialog(take_location.this);
                        add_item.setContentView(R.layout.adding_item_layout);
                        add_item.setCancelable(false);
                        cancel_order = add_item.findViewById(R.id.cancel_order);
                        item_image = add_item.findViewById(R.id.item_image);
                        place_geo = add_item.findViewById(R.id.place_geo);
                        item_price = add_item.findViewById(R.id.item_price);
                        item_quant = add_item.findViewById(R.id.item_quant);

                        place_geo.setText(fulladdress);

                        final EditText item_name = add_item.findViewById(R.id.item_name);
                        add_item.show();
                        cancel_order.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(take_location.this);
                                alert.setIcon(R.drawable.ic_delete_black_24dp);
                                alert.setTitle("Delete order");
                                alert.setMessage("Are you sure you want to delete the order?");
                                alert.setPositiveButton("Delete order", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).create().show();
                            }
                        });

                        item_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder choose_source = new AlertDialog.Builder(take_location.this);
                                choose_source.setMessage("Choose your source").setPositiveButton("camera", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent libary = new Intent();
                                        libary.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(libary, 4);
                                    }
                                }).setNegativeButton("gallary", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent libary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                        startActivityForResult(libary, requst_libary_picyure);
                                    }
                                }).show();


                            }
                        });

                        final Spinner spinner_place = add_item.findViewById(R.id.spinner_place);

                        String[] trips = {"Select City", "AMMAN", "IRBID", "AQABA"};
                        String[] loc_source = {"Select ", "Home", "Restaurant"};

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_dropdown_item_1line, trips);
                        ArrayAdapter<String> arrayAdapter_source = new ArrayAdapter<>(getApplication(), android.R.layout.simple_dropdown_item_1line, loc_source);

                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                        arrayAdapter_source.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

                        spinner_place.setAdapter(arrayAdapter);


                        Button add = add_item.findViewById(R.id.add);
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (item_name.toString().isEmpty() || spinner_place.getSelectedItem() == "choose from bellow") {
                                    Toast.makeText(getApplicationContext(), "you have to fill all fields", Toast.LENGTH_LONG).show();
                                    item_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_black_40dp));
                                    item_name.setHint("*");
                                } else if (item_name.toString().length() < 11) {
                                    Snackbar.make(view, "Too many words", Snackbar.LENGTH_LONG).show();
                                    add_item.dismiss();
                                } else {

                                    item_name_public = item_name.getText().toString();
                                    CountDownTimer count = new CountDownTimer(1000, 1000) {
                                        @Override
                                        public void onTick(long l) {

                                        }

                                        @Override
                                        public void onFinish() {
                                            AlertDialog.Builder confirm = new AlertDialog.Builder(take_location.this);
                                            confirm.setTitle("confirmation");
                                            confirm.setCancelable(false);
                                            confirm.setMessage("Are you sure you would like to add this order");
                                            confirm.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    spinner_place_choosen = spinner_place.getSelectedItem().toString();

                                                    if (imageuri == null) {
                                                        Toast.makeText(getApplicationContext(), "choose image", Toast.LENGTH_LONG).show();
                                                    } else {

                                                        if (spinner_place_choosen.equals("AMMAN")) {
                                                            storageReference.child("AMMAN");
                                                            upload_file();

                                                        } else if (spinner_place_choosen.equals("IRBID")) {
                                                            storageReference.child("IRBID");
                                                            upload_file();

                                                        } else if (spinner_place_choosen.equals("AQABA")) {
                                                            storageReference.child("AQABA");
                                                            upload_file();

                                                        }
                                                    }
                                                    ;
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                }
                                            });
                                            confirm.show();
                                        }
                                    }.start();


                                }

                            }
                        });

                    }
                }.start();
            }
        });
    }
}
