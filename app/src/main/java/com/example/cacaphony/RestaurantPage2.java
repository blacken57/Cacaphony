package com.example.cacaphony;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RestaurantPage2 extends AppCompatActivity {
    RecyclerView recyclerView;
    Restaurant_Adapter adapter;
    List<Restros>  RestroList;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    Button bt;
    double a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page2);
        fStore = FirebaseFirestore.getInstance();
        RestroList = new ArrayList<>();
        recyclerView = findViewById(R.id.Recycler_restro);
        recyclerView.setHasFixedSize(true);
        bt = findViewById(R.id.button3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(RestroList.size()>0){
            RestroList.clear();
        }

        fStore.collection("Restaurants").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot documentSnapshot : task.getResult()){
                            Restros restru = new Restros(documentSnapshot.getString("Name"),documentSnapshot.getId(),documentSnapshot.getDouble("opening"),documentSnapshot.getDouble("closing"));
                            RestroList.add(restru);

                            Calendar toby = Calendar.getInstance();
                            //toby.set(Calendar.HOUR_OF_DAY, new Date().getHours());
                            Log.d("88888888","Current hour: "+ toby.get(Calendar.HOUR_OF_DAY));
                            a=System.currentTimeMillis();
                        }
                        adapter = new Restaurant_Adapter(RestaurantPage2.this, RestroList);
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RestaurantPage2.this, "failed :(", Toast.LENGTH_SHORT).show();
                        Log.v("Failed", e.getMessage());
                    }
                });

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerOptions.class));
            }
        });



    }
}
