package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderTracking extends AppCompatActivity {
    private static final String TAG = "RRREEEEEEEEEEEEEE";
    TextView Menu,Restaurant;
    TextView first,second,third,fourth;
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    String Item, Restro;
    Double status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        Menu = findViewById(R.id.MenuItem);
        Restaurant = findViewById(R.id.Resto);
        first = findViewById(R.id.Confirmed);
        second = findViewById(R.id.Assign);
        third = findViewById(R.id.pickup);
        fourth = findViewById(R.id.delivered);

        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userId = mFAuth.getUid();
        DocumentReference documentReference = fStore.collection("Orders").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Restro = document.getString("Restaurant");
                        Item = document.getString("MenuItem");
                        status = document.getDouble("Status");

                        Restaurant.setText(Restro);
                        Menu.setText(Item);

                        if(status==1)
                        {
                            first.setBackgroundColor(Color.parseColor("#00FF00"));
                            first.setText("Order Confirmed(Done)");
                        }
                        if(status==2)
                        {
                            first.setBackgroundColor(Color.parseColor("#00FF00"));
                            first.setText("Order Confirmed(Done)");
                            second.setBackgroundColor(Color.parseColor("#00FF00"));
                            second.setText("Delivery guy Assigned");
                        }
                        if(status==3)
                        {
                            first.setBackgroundColor(Color.parseColor("#00FF00"));
                            first.setText("Order Confirmed(Done)");
                            second.setBackgroundColor(Color.parseColor("#00FF00"));
                            second.setText("Delivery guy Assigned");
                            third.setBackgroundColor(Color.parseColor("#00FF00"));
                            third.setText("Order picked up(Done)");
                        }
                        if(status==4)
                        {
                            first.setBackgroundColor(Color.parseColor("#00FF00"));
                            first.setText("Order Confirmed(Done)");
                            second.setBackgroundColor(Color.parseColor("#00FF00"));
                            second.setText("Delivery guy Assigned");
                            third.setBackgroundColor(Color.parseColor("#00FF00"));
                            third.setText("Order picked up(Done)");
                            fourth.setBackgroundColor(Color.parseColor("#00FF00"));
                            fourth.setText("Order Arrived(Complete)");
                        }


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}
