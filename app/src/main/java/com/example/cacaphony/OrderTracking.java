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
    TextView Menu,Restaurant,jabla;
    TextView first,second,third,fourth;
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    String Item, Restro;
    Double status;
    String DelID;
    String DeliveryName;
    String Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        Menu = findViewById(R.id.MenuItem);
        Restaurant = findViewById(R.id.Resto);
        jabla = findViewById(R.id.Info);
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
                        try
                        {
                            DelID = document.getString("DeliveryId");
                            DocumentReference documents = fStore.collection("Customers").document(DelID);
                            documents.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        DeliveryName = document.getString("fName");
                                        Phone = document.getString("Phone number");
                                        jabla.setText("Delivery Name is "+DeliveryName +"\n"+"Phone number: "+Phone);

                                    }
                                }
                            });
                        }
                        catch(Exception e )
                        {
                            DeliveryName = "No one yet.";
                            Phone = "N/A";
                            jabla.setText("No one has been Assigned to deliver yet.\nWait for some time.");
                        }


                        Restaurant.setText(Restro);
                        Menu.setText(Item);

                        if(status==1)
                        {
                            first.setBackgroundColor(Color.parseColor("#72E567"));
                            first.setText("Order Confirmed(Done)");
                        }
                        if(status==2)
                        {
                            first.setBackgroundColor(Color.parseColor("#72E567"));
                            first.setText("Order Confirmed(Done)");
                            second.setBackgroundColor(Color.parseColor("#72E567"));
                            second.setText("Delivery guy Assigned");
                        }
                        if(status==3)
                        {
                            first.setBackgroundColor(Color.parseColor("#72E567"));
                            first.setText("Order Confirmed(Done)");
                            second.setBackgroundColor(Color.parseColor("#72E567"));
                            second.setText("Delivery guy Assigned");
                            third.setBackgroundColor(Color.parseColor("#72E567"));
                            third.setText("Order picked up(Done)");
                        }
                        if(status==4)
                        {
                            first.setBackgroundColor(Color.parseColor("#72E567"));
                            first.setText("Order Placed(Done)");
                            second.setBackgroundColor(Color.parseColor("#72E567"));
                            second.setText("Delivery guy Assigned");
                            third.setBackgroundColor(Color.parseColor("#72E567"));
                            third.setText("Order picked up(Done)");
                            fourth.setBackgroundColor(Color.parseColor("#72E567"));
                            fourth.setText("Order Arrived(Complete)");
                        }



                    } else {
                        Log.d(TAG, "No such document");
                        jabla.setText("You have not ordered yet.\nPlace your first Order through Restaurants!");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}
