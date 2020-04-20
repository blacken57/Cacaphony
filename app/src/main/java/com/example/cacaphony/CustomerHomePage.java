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
    TextView mName;  String name;
    private static final String TAG = "DASHBOARD ACTIVITY";
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_page);
        mDrawerLayout = findViewById(R.id.drawerCustomer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        mNavigationView = findViewById(R.id.nav_view_cust);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.rests:
                    {
                        startActivity(new Intent(CustomerHomePage.this, RestaurantPage2.class));
                        break;
                    }
                    case R.id.settings:
                    {
                        startActivity(new Intent(CustomerHomePage.this, EditCust.class));
                        break;
                    }
                    case R.id.custDashboard:
                    {
                        startActivity(new Intent(CustomerHomePage.this, OrderTracking.class));
                        break;
                    }
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
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
