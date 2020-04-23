package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class DriverHomePage extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView mNavigationView;
    TextView texty,per;
    FirebaseFirestore fStore;
    FirebaseAuth fAUth;
    String DelId, Restaurant, Menu, Customer, Name, Phone;
    int check = 0;
    double lat2, long2, Radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_page);
        mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fAUth = FirebaseAuth.getInstance();
        DelId = fAUth.getUid();
        fStore = FirebaseFirestore.getInstance();
        texty = findViewById(R.id.Information);
        per = findViewById(R.id.Personal);


        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.order:
                    {
                        DocumentReference doc = fStore.collection("Customers").document(DelId);
                        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot f = task.getResult();
                                try {
                                    lat2 = f.getDouble("Latitude");
                                    long2 = f.getDouble("Longitude");
                                    Radius = f.getDouble("Radius");
                                    Log.d("HELLOOOO", "But, the secondary value is: " + lat2 + " " + long2);
                                    startActivity(new Intent(DriverHomePage.this, DriverOrders.class));
                                }catch(Exception e)
                                {
                                    Toast.makeText(DriverHomePage.this, "Update your Radius and Location", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        break;
                    }
                    case R.id.settings:
                    {
                        startActivity(new Intent(DriverHomePage.this, EditDel.class));
                        break;
                    }
                    case R.id.dashboard:
                    {
                        startActivity(new Intent(DriverHomePage.this, PresentOrder.class));
                        break;
                    }
                }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
            }
        });

        try{
        fStore.collection("Orders").whereEqualTo("DeliveryId",DelId).whereEqualTo("Assigned",true).whereLessThan("Status",4.0).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Restaurant = documentSnapshot.getString("Restaurant");
                            Menu = documentSnapshot.getString("MenuItem");
                            Customer = documentSnapshot.getString("UserName");
                            double stat = documentSnapshot.getDouble("Status");
                            if(stat<4) {
                                texty.setText("Your present order:\nRestaurant: " + Restaurant + " Menu Item: " + Menu + "\nfor " + Customer);
                            }
                            else{texty.setText("Your previous order:\nRestaurant: " + Restaurant + " Menu Item: " + Menu + "\nfor " + Customer);
                            }
                        }
                    }
                });

    }
        catch(Exception e){
            texty.setText("No Orders Yet");
        }

        DocumentReference documentReference = fStore.collection("Customers").document(DelId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Name = document.getString("fName");
                        String phone = document.getString("Phone number");
                        String email = document.getString("email");
                        per.setText(Name+"\n"+"Phone Number: "+phone+"\nEmail: "+email);
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
