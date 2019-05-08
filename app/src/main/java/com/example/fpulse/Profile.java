package com.example.fpulse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ScheduledExecutorService;

//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;

public class Profile extends AppCompatActivity {
    TextView pulsetxt;
    Button link;
    Button log;
    ImageView imagepro;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    long cal = Calendar.getInstance().getTimeInMillis();


    GraphView graphView;
    LineGraphSeries series;

    private DatabaseReference mDatabase,mDatabase2;
   // private MapView mapView;
    private static final String Keymap = "AIzaSyAR2wn6_41CeUoJHACczqrXH35eTh-pOdE";
    String url = "http://api.iottweet.com/livejson.php?userid=003664&key=l81scsm8i2b6";
    ////5656565656565
    private ScheduledExecutorService scheduleTaskExecutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        link = (Button) findViewById(R.id.button2);
        log = (Button) findViewById(R.id.logbutton3);
        imagepro = (ImageView) findViewById(R.id.Imageprofile);
        pulsetxt = (TextView) findViewById(R.id.pulsetxt) ;
        graphView = (GraphView) findViewById(R.id.graphpulse) ;
        getdatauser();

        series = new LineGraphSeries();
        graphView.addSeries(series);
        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("kusrc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Toast.makeText(Profile.this, dateFormat.format(cal), Toast.LENGTH_LONG).show();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Profile.this,MapsActivity.class);
                startActivity(homeIntent);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Profile.this,logpulse.class);
                startActivity(homeIntent);
            }
        });
        mDatabase2 = FirebaseDatabase.getInstance().getReference("Logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dateFormat.format(cal).toString());
        mDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp=new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index=0;
                for(DataSnapshot mydatasnapshot : dataSnapshot.getChildren()){
                    UsersGetter user = mydatasnapshot.getValue(UsersGetter.class);
                    dp[index] = new DataPoint(index,Integer.parseInt(user.getPulseValue().toString()));
                    index++;
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        onStart();


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

                    Picasso.get()
                            .load(user.getPicUrl())
//                    .resize(50, 50)
//                    .centerCrop()
                            .into(imagepro);if(user.getPulseshow().equals("-1")){
                    pulsetxt.setText("ยังไม่ได้ติดตั้ง อุปกรณ์");
                }else{
                    pulsetxt.setText(user.getPulseshow());
                }


//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mDatabase = FirebaseDatabase.getInstance().getReference("Users").child("kusrc").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                DataPoint[] dp=new DataPoint[1];
//
//                int index=0;
//
//                UsersGetter user = dataSnapshot.getValue(UsersGetter.class);
//                dp[index] = new DataPoint(index,Integer.parseInt(user.getPulseshow().toString()));
//                index++;
//                series.resetData(dp);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
