package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;
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

public class CustomerHomePage extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView mNavigationView;
    TextView mName,mInfo;  String name;
    private static final String TAG = "DASHBOARD ACTIVITY";
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_page);
        mDrawerLayout = findViewById(R.id.drawerCustomer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = mFAuth.getCurrentUser().getUid();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = findViewById(R.id.nav_view_cust);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rests: {
                        Log.d(TAG, "Not working. rests");
                        userId = mFAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("Orders").document(userId);
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()&&document.get("Longitude")!=null && document.getDouble("Status") !=4) {
                                        Toast.makeText(CustomerHomePage.this, "You have already Ordered", Toast.LENGTH_SHORT).show();
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


                        break;
                    }
                    case R.id.settings: {
                        startActivity(new Intent(CustomerHomePage.this, EditCust.class));
                        break;
                    }
                    case R.id.custDashboard: {
                        startActivity(new Intent(CustomerHomePage.this, OrderTracking.class));
                        break;
                    }
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        /*mName = findViewById(R.id.customer_name);
        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        String userId = mFAuth.getCurrentUser().getUid();
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
        });*/

        mName = findViewById(R.id.fullName);
        mInfo = findViewById(R.id.Infos);

        DocumentReference documentReference = fStore.collection("Customers").document(userId);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        name = document.getString("fName");
                        String phone = document.getString("Phone number");
                        String email = document.getString("email");
                        String time;
                        try{time = document.getString("Time");}catch(Exception e){time = "Not ordered before";}
                        if(time==null)
                        {
                            time = "Not ordered yet";
                        }
                        mName.setText(name+"\n"+"Phone Number: "+phone+"\nEmail: "+email+"\nLast Ordered: "+time);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        DocumentReference documentReferences = fStore.collection("Orders").document(userId);
        documentReferences.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        try {
                        String Del = document.getString("Restaurant");
                        String menu = document.getString("MenuItem");
                        double price = document.getDouble("Price");
                        double status = document.getDouble("Status");
                        String time = document.getString("Time");
                        if (status == 4) {
                            mInfo.setText("Previous Order Info: \n" +
                                    "Restaurant: " + Del +
                                    "\nItem Purchased: " + menu +
                                    "\nPrice: " + price);
                        } else {
                            mInfo.setText("Present Order Info: \n" +
                                    "Restaurant: " + Del +
                                    "\nItem Purchased: " + menu +
                                    "\nPrice: " + price);
                        }
                    }
                        catch(Exception e)
                        {
                            Log.d(TAG,e.toString());
                            mInfo.setText("Order Not Complete");
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
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

