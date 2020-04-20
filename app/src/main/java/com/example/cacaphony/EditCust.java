package com.example.cacaphony;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EditCust extends AppCompatActivity {
    private static final String TAG = "EDIT ACTIVITY" ;
    EditText mNameChange,mPhoneChange,mPassChange;
    Button changePass,changeName,changePhone,mBack;
    FirebaseAuth mFAuth;
    FirebaseFirestore fStore;
    String userID;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cust);
        mNameChange = findViewById(R.id.fullNameEdit);
        mPhoneChange = findViewById(R.id.phoneEdit);
        mPassChange = findViewById(R.id.passEdit);
        changePass = findViewById(R.id.passwordChange);
        changeName = findViewById(R.id.nameChange);
        changePhone = findViewById(R.id.PhoneChange);
        mBack = findViewById(R.id.Dashy);
        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPassChange.getText().toString().trim();
                if (password.length() <= 6) {
                    mPassChange.setError("Password length should be greater than 6");
                    return;
                }
                userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("password",password);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditCust.this, "Changed password successfully", Toast.LENGTH_SHORT).show();
                FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                user1.updatePassword(password)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                }
                            }
                        });

            }
            });


        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameChange.getText().toString().trim();
                if(name.length()<1){
                    mNameChange.setError("Set a Name you stupid beach");
                }
                userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("fName",name);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditCust.this, "Changed Name successfully", Toast.LENGTH_SHORT).show();
            }
        });

        changePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhoneChange.getText().toString().trim();
                if (phone.length() <= 6) {
                    mPhoneChange.setError("Give a proper phone number");
                    return;
                }
                userID = mFAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("Customers").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("Phone number",phone);
                documentReference.set(user, SetOptions.merge());
                Toast.makeText(EditCust.this, "Changed Phone successfully", Toast.LENGTH_SHORT).show();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerHomePage.class));
            }
        });
    }
}

