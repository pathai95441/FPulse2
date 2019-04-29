package com.example.fpulse;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//import androidx.annotation.Nullable;

//import com.google.android.gms.maps.model.LatLng;

public class Home extends AppCompatActivity {
    ImageView myps;
    ImageView adds;
    TextView Tags;
    TextView val;
    Button logout;
    GraphView graphView;
    ListView listView;
    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 001;
    final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    private DatabaseReference mDatabase,mDatabase2;
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
        logout = (Button) findViewById(R.id.logoutbtn);
        Tags = (TextView) findViewById(R.id.Tags) ;
        val = (TextView) findViewById(R.id.pulsesval);
        createNotificationChannel();

        getdatauser();
        mDatabase2 = FirebaseDatabase.getInstance().getReference("Users").child("kusrc");
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<String> images = new ArrayList<String>();
                ArrayList<String> pulsevals = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UsersGetter user = snapshot.getValue(UsersGetter.class);
                    //Log.d("mytag",user.getName().toString());
                    if(!snapshot.getKey().equals(UID)){
                        names.add(user.getName() );
                        images.add(user.getPicUrl());
                        pulsevals.add(user.getPulseshow());
                        if(Integer.parseInt(user.getPulseshow())<60 && Integer.parseInt(user.getPulseshow())!=-1){
                            showNotification1(user.getName());
                            Log.d("gun","Alert low");

                        }else if(Integer.parseInt(user.getPulseshow())>110 && Integer.parseInt(user.getPulseshow())!=-1){
                            showNotification2(user.getName());
                        }
                    }


                }
                MyAdapter adapter = new MyAdapter(Home.this, names, images,pulsevals);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(Home.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Home.this, Createprofile.class);
                startActivity(homeIntent);
            }
        });

        myps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Home.this, Profile.class);
                startActivity(homeIntent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(Home.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent homeIntent = new Intent(Home.this, MainActivity.class);
                        startActivity(homeIntent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }





    class MyAdapter extends ArrayAdapter<String>{

        Context context;
        ArrayList<String> names;
        ArrayList<String> images;
        ArrayList<String> pulsevals;
        MyAdapter(Context c, ArrayList<String>  name, ArrayList<String> image,ArrayList<String> pulseval){
            super(c,R.layout.user_layout,R.id.NAMESV,name );
            this.context = c;
            this.images = image;
            this.names = name;
            this.pulsevals = pulseval;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.user_layout,parent,false);
            ImageView imageView = row.findViewById(R.id.IMGV);
            TextView textView = row.findViewById(R.id.NAMESV);
            TextView textView1 = row.findViewById(R.id.pulseshow);
            //imageView.setImageResource(images.get(position));
            Picasso.get()
                    .load(images.get(position))
//                    .resize(50, 50)
//                    .centerCrop()
                    .into(imageView);
            textView.setText(names.get(position));
            if(pulsevals.get(position).equals("-1")){
                textView1.setText("ยังไม่ได้ติดตั้ง อุปกรณ์");
            }else{
            textView1.setText(pulsevals.get(position));
            }

            return row;
        }
    }
    private void getdatauser(){
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("kusrc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        if(mDatabase == null){
//            Intent homeIntent = new Intent(Home.this, Createprofile.class);
//            startActivity(homeIntent);
//        }
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                UsersGetter user = dataSnapshot.getValue(UsersGetter.class);
                if(user != null) {
                        Picasso.get()
                                .load(user.getPicUrl())
//                    .resize(50, 50)
//                    .centerCrop()
                                .into(myps);
                        Tags.setText(user.getName() + "Tags : " + user.getTags().toString());
                }else{
                    Intent i = new Intent(Home.this,Createprofile.class);
                    startActivity(i);
                    Log.d("gun","ERROR");
                }
                if(user.getPulseshow().equals("-1")){
                    val.setText("ยังไม่ได้ติดตั้ง อุปกรณ์");
                }else{
                    val.setText(user.getPulseshow());
                }
                if(Integer.parseInt(user.getPulseshow())<60 && Integer.parseInt(user.getPulseshow())!=-1){
                    showNotification1(user.getName());
                    Log.d("gun","Alert low");

                }else if(Integer.parseInt(user.getPulseshow())>110 && Integer.parseInt(user.getPulseshow())!=-1){
                    showNotification2(user.getName());
                }
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    public void showNotification1(String name){

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("อันตราย!!!!")
                .setContentText("คุณ "+name+" มีช๊พจรเต้นต่ำกว่าปกติ!!!!")
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Personal Notification";
            String description = "Include all the personal notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);

            notificationChannel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
    public void showNotification2(String name){

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("อันตราย!!!!")
                .setContentText("คุณ "+name+" มีช๊พจรเต้นสูงกว่าปกติ!!!!")
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);

    }


    }

