package com.example.fpulse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singin);
        Button signBTN = (Button) findViewById(R.id.signInButton);
        Button signUp = (Button) findViewById(R.id.signUpButton);
        final EditText emailText = (EditText) findViewById(R.id.Emailed);
        final EditText passText = (EditText) findViewById(R.id.passworded);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(SignIn.this,Home.class);
                    startActivity(i);
                    finish();
                } else {
                    // User is signed out
                }
                // ...
            }
        };
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this,SignUp.class);
                startActivity(i);
            }
        });

        signBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String pass = passText.getText().toString();
                if(!email.isEmpty()){
                    if(!pass.isEmpty()){
                        //Toast.makeText(SignIn.this,email+"---"+pass,Toast.LENGTH_SHORT).show();
                        mAuth.signInWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete( Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignIn.this,"Sign in success!!!!",Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(SignIn.this,Home.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            Toast.makeText(SignIn.this,"Sign In ERRORRRRRRR",Toast.LENGTH_SHORT).show();
                                        }
                                        // ...
                                    }
                                });
                    }else{
                        Toast.makeText(SignIn.this,"pass error",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignIn.this,"email error",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
