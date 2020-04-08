package com.example.cacaphony;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cacaphony.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    public static final String TAG = "TAG";
    private EditText mEmail, mPassword;
    private Button mLoginButton;
    FirebaseAuth mFAuth;
    ProgressBar mProgressBar;
    String userID;
    FirebaseFirestore fStore;
    TextView mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.Email2);
        mPassword = findViewById(R.id.pass2);
        mLoginButton = findViewById(R.id.registering2);

        mFAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mProgressBar = findViewById(R.id.progressBar);
        mRegisterButton = findViewById(R.id.Reg);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Emails = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(Emails)) {
                    mEmail.setError("Email field Empty");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password field is Empty");
                    return;
                }
                if (password.length() <= 6) {
                    mPassword.setError("Password length should be greater than 6");
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);

                mFAuth.signInWithEmailAndPassword(Emails, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressBar.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }
}
