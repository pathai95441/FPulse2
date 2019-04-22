package com.example.fpulse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


//import androidx.annotation.Nullable;

//import com.google.android.gms.maps.model.LatLng;

public class Home extends AppCompatActivity {
    ImageView myps;
    ImageView adds;
    ListView listView;
    private DatabaseReference mDatabase;
      //String[] NAMES ={"set","set","set","set","set","set","set","set"};
      //int[] Image = {R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        myps = (ImageView) findViewById(R.id.profile);
        adds = (ImageView) findViewById(R.id.addimg);
        listView = findViewById(R.id.ListView);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<String> images = new ArrayList<String>();
                //int[] Image = {R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,R.drawable.add,};
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    UsersGetter user = snapshot.getValue(UsersGetter.class);
                    Log.d("mytag",snapshot.getKey().toString());
                    Log.d("mytag",user.getSerial());
                    names.add(snapshot.getKey().toString());
                    images.add(user.getPicUrl());

                }
                MyAdapter adapter = new MyAdapter(Home.this,names,images);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Home.this,Add.class);
                startActivity(homeIntent);
            }
        });

        myps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Home.this,Profile.class);
                startActivity(homeIntent);
            }
        });
    }
    class MyAdapter extends ArrayAdapter<String>{

        Context context;
        ArrayList<String> names;
        ArrayList<String> images;

        MyAdapter(Context c, ArrayList<String>  name, ArrayList<String> image){
            super(c,R.layout.user_layout,R.id.NAMESV,name );
            this.context = c;
            this.images = image;
            this.names = name;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.user_layout,parent,false);
            ImageView imageView = row.findViewById(R.id.IMGV);
            TextView textView = row.findViewById(R.id.NAMESV);

            //imageView.setImageResource(images.get(position));
            Picasso.get()
                    .load(images.get(position))
//                    .resize(50, 50)
//                    .centerCrop()
                    .into(imageView);
            textView.setText(names.get(position));
            return row;
        }
    }

    }

