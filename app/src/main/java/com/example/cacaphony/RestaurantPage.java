package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RestaurantPage extends AppCompatActivity {
    private static final String TAG = "HELLO THERE ;_;" ;
    TextView one,two,three;
    String UserID;
    String[] Rests = new String[10];
    String[] ID = new String[10];
    int count=0;
    String phone, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);
        //one = findViewById(R.id.card1);
        two = findViewById(R.id.card2);
        three = findViewById(R.id.card3);
        FirebaseAuth mFAuth;
        final FirebaseFirestore fStore;
        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserID = mFAuth.getUid();


        DocumentReference documentReference = fStore.collection("Customers").document(UserID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getString("fName");
                        phone = document.getString("Phone number");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        fStore.collection("Restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Rests[i] = document.getString("Name");
                                ID[i] = document.getId();
                                Log.d(TAG, document.getId() + " => " + Rests[i]);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                i++;
                                one.setText(Rests[0]);
                                two.setText(Rests[1]);
                                three.setText(Rests[2]);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("RestroID", ID[0]);
                user.put("Restaurant",Rests[0]);
                user.put("UserID",UserID);
                user.put("UserName",name);
                user.put("UserPhone",phone);
                documentReference.set(user);
                startActivity(new Intent(getApplicationContext(),CustomerLocation.class));
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("RestroID", ID[1]);
                user.put("Restaurant",Rests[1]);
                user.put("UserID",UserID);
                user.put("UserName",name);
                user.put("UserPhone",phone);
                documentReference.set(user);
                startActivity(new Intent(getApplicationContext(),CustomerLocation.class));
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(UserID);
                Map<String,Object> user = new HashMap<>();
                user.put("RestroID", ID[2]);
                user.put("Restaurant",Rests[2]);
                user.put("UserID",UserID);
                user.put("UserName",name);
                user.put("UserPhone",phone);
                documentReference.set(user);
                startActivity(new Intent(getApplicationContext(),CustomerLocation.class));
            }
        });
    }
}
