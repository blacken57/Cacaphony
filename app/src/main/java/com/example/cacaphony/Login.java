package com.example.cacaphony;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    public static final String TAG = "TAGGING IN HERE";
    private static final int RC_SIGN_IN_D = 0;
    private static final int RC_SIGN_IN_C = 1 ;
    private EditText mEmail, mPassword;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton mGoogleCust, mGoogleDel;
    private Button mLoginButton;
    FirebaseAuth mFAuth, mAuth;
    ProgressBar mProgressBar;
    String userID;
    FirebaseFirestore fStore;
    TextView mRegisterButton;
    double cust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.Email2);
        mPassword = findViewById(R.id.pass2);
        mLoginButton = findViewById(R.id.registering2);
        mGoogleCust = findViewById(R.id.signInButtonCust);
        mGoogleDel = findViewById(R.id.signInButtonDel);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mAuth =FirebaseAuth.getInstance();
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
                            String userId = mFAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fStore.collection("Customers").document(userId);
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            cust = document.getDouble("Customer");
                                            Log.d(TAG, "The value of cust is: "+ cust);
                                            if(cust == 1)
                                            {
                                                startActivity(new Intent(getApplicationContext(), CustomerHomePage.class));
                                            }
                                            else
                                            {
                                                startActivity(new Intent(getApplicationContext(), DriverHomePage.class));
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
                });
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        mGoogleDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN_D);
       //         startActivity(new Intent(Login.this, MainActivity.class));
            }
        });
        mGoogleCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN_C);
         //       startActivity(new Intent(Login.this, CustomerDashboard.class));
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_C) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogleC(account);
            } catch (ApiException e) {
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == RC_SIGN_IN_D) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogleD(account);
            } catch (ApiException e) {
                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogleC(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "User Singed in", Toast.LENGTH_SHORT).show();
                            getUserDetails(user, RC_SIGN_IN_C);
                            startActivity(new Intent(Login.this, CustomerHomePage.class));
                        }
                        else {
                            Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void firebaseAuthWithGoogleD(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "User Singed in", Toast.LENGTH_SHORT).show();
                            getUserDetails(user, RC_SIGN_IN_D);
                            //starts delivery's activity
                            startActivity(new Intent(Login.this, DriverHomePage.class));
                        }
                        else {
                            Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void getUserDetails(FirebaseUser user, int i){
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("Customers").document(userID);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Login.this);
        Map<String,Object> users = new HashMap<>();
        users.put("fName", acct.getDisplayName() );
        users.put("email", acct.getEmail());
        users.put("password", "0");
        users.put("Customer", i);
        documentReference.set(users,SetOptions.merge());
    }
}
