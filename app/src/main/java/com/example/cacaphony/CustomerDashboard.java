package com.example.cacaphony;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerDashboard extends AppCompatActivity {
    private static final String TAG = "DASHBOARD ACTIVITY";
    TextView mName;
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    String name,userId;
    Button Settings,Restaurant,Tracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        Settings = findViewById(R.id.set);
        Restaurant = findViewById(R.id.Restros);
        Tracking = findViewById(R.id.Orderss);
        mName = findViewById(R.id.name);
        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mFAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Customers").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getString("fName");
                        mName.setText(name);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditCust.class));
            }
        });

        Restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(userId);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(CustomerDashboard.this,"You have already Ordered", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                Log.d(TAG, "No such document");
                                startActivity(new Intent(getApplicationContext(), RestaurantPage2.class));
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            }
        });

        Tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderTracking.class));
            }
        });
    }
}
