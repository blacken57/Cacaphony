package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class EditDel extends AppCompatActivity implements LocationListener {
    private static final String TAG = "EDIT ACTIVITY" ;
    EditText mNameChange,mPhoneChange,mPassChange,mRadiusChange;
    Button changePass,changeName,changePhone,mBack, changeRadius, changeLocation;
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    String userID;
    double lati,longi;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_del);
        mNameChange = findViewById(R.id.name);
        mPhoneChange = findViewById(R.id.phone);
        mPassChange = findViewById(R.id.pass);
        mRadiusChange = findViewById(R.id.radius);
        changePass = findViewById(R.id.setPass);
        changeName = findViewById(R.id.setName);
        changePhone = findViewById(R.id.setPhone);
        changeRadius = findViewById(R.id.setRadius);
        changeLocation = findViewById(R.id.setLoc);
        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassChange.getText().toString().trim();
                if (password.length() <= 6) {
                    mPassChange.setError("Password length should be greater than 6");
                    return;
                }
                userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("password",password);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditDel.this, "Changed password successfully", Toast.LENGTH_SHORT).show();
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                user1.updatePassword(password)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                }
                            }
                        });

            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameChange.getText().toString().trim();
                if(name.length()<1){
                    mNameChange.setError("Set a Name you stupid beach");
                }
                userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("fName",name);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditDel.this, "Changed Name successfully", Toast.LENGTH_SHORT).show();
            }
        });

        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneChange.getText().toString().trim();
                if (phone.length() <= 6) {
                    mPhoneChange.setError("Give a proper phone number");
                    return;
                }
                userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("Phone number",phone);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditDel.this, "Changed Phone successfully", Toast.LENGTH_SHORT).show();
            }
        });

        changeRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rad = Double.parseDouble(mRadiusChange.getText().toString());
                userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("Radius",rad);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditDel.this, "Changed Radius successfully", Toast.LENGTH_SHORT).show();
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "No Permission Folks!" );
            Toast.makeText(this, "Permission Error", Toast.LENGTH_SHORT).show();
            requestLocationPermission();
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Yes is clicked" );
                fStore = FirebaseFirestore.getInstance();
                FirebaseAuth mFAuth = FirebaseAuth.getInstance();
                String userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Log.d(TAG, "Data ready to be put " );
                Map<String,Object> user = new HashMap<>();
                user.put("Longitude",longi);
                user.put("Latitude",lati);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditDel.this, "Changed Location successfully " + longi+ " " + lati, Toast.LENGTH_SHORT).show();


//                startActivity(new Intent(getApplicationContext(), DriverHomePage.class));
            }
        });


    }

    @Override
    public void onLocationChanged(Location location) {
        lati = location.getLatitude();
        longi =  location.getLongitude();
        Log.d(TAG, "Location found " +lati+ " "+ longi);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
