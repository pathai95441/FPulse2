package com.example.fpulse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button signup = (Button) findViewById(R.id.button3);
        final EditText emailText = (EditText) findViewById(R.id.editTextEmail);
        final EditText passText = (EditText) findViewById(R.id.editTextPassword);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailText.getText().toString();
                final String pass = passText.getText().toString();
                if(!email.isEmpty()) {
                    if (!pass.isEmpty()) {
                        mAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>(){
                                    @Override
                                    public void onComplete(Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(SignUp.this, "Authentication success!!.", Toast.LENGTH_SHORT).show();
                                            mAuth.signInWithEmailAndPassword(email, pass)
                                                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete( Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(SignUp.this,"Sign in success!!!!",Toast.LENGTH_SHORT).show();
                                                                Intent i = new Intent(SignUp.this,Home.class);
                                                                startActivity(i);
                                                                finish();
                                                            }else{
                                                                Toast.makeText(SignUp.this,"Sign In ERRORRRRRRR",Toast.LENGTH_SHORT).show();
                                                            }
                                                            // ...
                                                        }
                                                    });
                                        }
                                        // ...
                                    }
                                });
                    }
                }
            }
        });
    }
}
