 package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DriverOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    OrderAdapter adapter;
    List<Orders>  ordersList;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_orders);
        fStore = FirebaseFirestore.getInstance();
        ordersList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(ordersList.size()>0){
            ordersList.clear();
        }
        fStore.collection("Orders").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            Orders orders = new Orders(documentSnapshot.getString("UserID"),
                                    documentSnapshot.getString("Restaurant"), documentSnapshot.getString("UserName"),
                                    documentSnapshot.getString("UserPhone")/*, documentSnapshot.getDouble("Amount")*//*,
                                    documentSnapshot.getBoolean("Assigned")*/);
                       /*     if(documentSnapshot.getBoolean("Assigned") == false){
                                ordersList.add(orders);
                            }*/
                            ordersList.add(orders);
                        }
                        adapter = new OrderAdapter(DriverOrders.this, ordersList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverOrders.this, "failed :(", Toast.LENGTH_SHORT).show();
                        Log.v("Failed", e.getMessage());
                    }
                });
        /*CollectionReference listCollectionReference = fStore.collection("Orders");
        Query listQuery = listCollectionReference
                .whereEqualTo("Assigned",false);
        listQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        List list = document.toObject(List.class);
                        arrayList.add(list);
                    }
                }
            }
        });
*/


    }
}
