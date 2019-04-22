package com.example.fpulse;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Add extends AppCompatActivity {
    ImageView create;
    ImageView addfirend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        create = (ImageView) findViewById(R.id.creates);
        addfirend = (ImageView) findViewById(R.id.addfreind);

        addfirend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntents = new Intent(Add.this,Addfriend.class);
                startActivity(homeIntents);
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Add.this,Createprofile.class);
                startActivity(homeIntent);
            }
        });
    }
}
