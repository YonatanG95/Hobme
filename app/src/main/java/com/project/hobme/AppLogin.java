package com.project.hobme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.hobme.UI.DetailActivity;
import com.project.hobme.Utilities.FirebaseMethods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
     * Created by HP on 5/2/2017.
     */

public class AppLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private EditText mEmail, mPassword;
    private Context mContext;
    private ProgressBar mProgressBar;
    private TextView mPleaseWait;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen_layout);
        initWidgets();
        setupFirebaseAuth();
        init();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public void initWidgets()
    {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mPleaseWait = (TextView) findViewById(R.id.pleaseWait);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mContext = AppLogin.this;
        mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    public void init() {
        /*
         If the user is logged in then navigate to HomeActivity and call 'finish()'
          */
        if (mAuth.getCurrentUser() != null) {
            Log.d("CheckNew","moving to main page");
            Intent intent = new Intent(AppLogin.this, DetailActivity.class);
            startActivity(intent);
            finish();
        }

        TextView linkSignUp = (TextView) findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Check", "onClick: navigating to register screen");
                Intent intent = new Intent(AppLogin.this, AppRegister.class);
                startActivity(intent);
            }
        });

        //initialize the button for logging in
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Check", "onClick: attempting to log in.");

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(mContext, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mPleaseWait.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(AppLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("Check", "signInWithEmail:onComplete:" + task.isSuccessful());
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w("Check", "signInWithEmail:failed", task.getException());

                                        Toast.makeText(AppLogin.this, getString(R.string.auth_failed),
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        mPleaseWait.setVisibility(View.GONE);
                                    } else {
                                        try {
                                            //if (CHECK_IF_VERIFIED) {
                                            if (user.isEmailVerified()) {
                                                Log.d("Check", "onComplete: success. email is verified.");
                                                Intent intent = new Intent(AppLogin.this, DetailActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(mContext, "Email is not verified \n check your email inbox.", Toast.LENGTH_SHORT).show();
                                                mProgressBar.setVisibility(View.GONE);
                                                mPleaseWait.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }
//                                            } else {
//                                                Log.d(TAG, "onComplete: success. email is verified.");
//                                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                                                startActivity(intent);
//                                            }

                                        } catch (NullPointerException e) {
                                            Log.e("Check", "onComplete: NullPointerException: " + e.getMessage());
                                        }
                                    }

                                    // ...
                                }
                            });
                }

            }
        });
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d("Check", "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("Check", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Check", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
}
