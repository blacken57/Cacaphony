package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PresentOrder extends AppCompatActivity {

    private static final String TAG = "8888888888888";
    Button OTP,Map,Status;
    TextView Info;
    EditText Num;
    String otp,name,phone,menuItem,Restaurant,DelID,CustID,OrderID;
    FirebaseFirestore fStore;
    FirebaseAuth fAUth;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_order);
        OTP = findViewById(R.id.SubmitOTPButton);
        Map = findViewById(R.id.Map);
        Status = findViewById(R.id.PickedUp);

        Info = findViewById(R.id.Infosss);
        Num = findViewById(R.id.SubOTP);

        fAUth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        DelID = fAUth.getUid();

        fStore.collection("Orders").whereEqualTo("DeliveryId",DelID).whereEqualTo("Assigned",true).whereLessThan("Status",4.0).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            CustID = documentSnapshot.getString("UserID");
                            otp = documentSnapshot.getString("OTP");
                            menuItem = documentSnapshot.getString("MenuItem");
                            Restaurant = documentSnapshot.getString("Restaurant");
                            OrderID = documentSnapshot.getId();
                            price = documentSnapshot.getDouble("Price");
                            DocumentReference documentReference = fStore.collection("Customers").document(CustID);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String time;
                                            name = document.getString("fName");
                                            phone = document.getString("Phone number");
                                            try{time= document.getString("Time");
                                            if(time==null){time = "N/A";}}
                                            catch (Exception e){ time = "Not ordered";}
                                            Info.setText("Customer Name: "+name+"\n"+"Phone number: "+phone+"\n"+"Restaurant: "+Restaurant+"\n"+"Item: "+menuItem+", Price: "+price +"\nOrder Timing: " +time);

                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                        break;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v("Failed", e.getMessage());
                    }
                });




        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });

        Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(OrderID);
                java.util.Map<String,Object> user = new HashMap<>();
                user.put("Status",3);
                Toast.makeText(PresentOrder.this, "Reached here "+ OrderID, Toast.LENGTH_SHORT).show();
                documentReference.set(user, SetOptions.merge());
            }
        });

        OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = fStore.collection("Orders").document(OrderID);
                String res = Num.getText().toString();
                if(res.equals(otp)){
                    java.util.Map<String,Object> user = new HashMap<>();
                    user.put("Status",4);
                    documentReference.set(user, SetOptions.merge());
                    Toast.makeText(PresentOrder.this, "Order Succesfully Completed Completion.", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }
}
