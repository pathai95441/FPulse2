package com.example.fpulse;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    Calendar mCalendar;


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

        //series = new LineGraphSeries();
        //graphView.addSeries(series);
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
        mDatabase2 = FirebaseDatabase.getInstance().getReference("Logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(dateFormat.format(cal));
        mDatabase2.addValueEventListener(new ValueEventListener() {
            String hourTmp = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LineGraphSeries<DataPoint> arrlist = new LineGraphSeries();
                String dateFromDB = dataSnapshot.getKey();
                Log.d("timeTag",dateFromDB);
                DataPoint[] dp=new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index=0;
                for(DataSnapshot mydatasnapshot : dataSnapshot.getChildren()){
                    String timeForm = mydatasnapshot.getKey().toString();
                    String dateForm = dateFromDB;
                    String [] arrOfStr = timeForm.split(":");
                    if(!arrOfStr[0].equals(hourTmp)){
                        hourTmp = arrOfStr[0];
                        String dateNtimeStr = dateForm+"T"+timeForm+"Z";
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        try {
                            Date date = format.parse(dateNtimeStr);
                            Log.d("timeTag",date.toString());
                            UsersGetter user = mydatasnapshot.getValue(UsersGetter.class);
                            DataPoint dataTmp = new DataPoint(date, Double.parseDouble(user.getPulseValue()));
                            arrlist.appendData(dataTmp,true,24);

//                            dp[index] = new DataPoint(index,Integer.parseInt(user.getPulseValue().toString()));
//                            index++;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }
                arrlist.setDrawDataPoints(true);
                arrlist.setDataPointsRadius(10);
                graphView.removeAllSeries();
                graphView.addSeries(arrlist);
                graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(Profile.this){
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if(isValueX) {
                            DateFormat df = new SimpleDateFormat("HH");
                            mCalendar.setTimeInMillis((long) value);
                            String timestr = df.format(mCalendar.getTime());
                            return (timestr);
                        }else{
                            return super.formatLabel(value, isValueX);
                        }

                    }
                });
                //graphView.getGridLabelRenderer().setNumHorizontalLabels(12); // only 4 because of the space
                //graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getGridLabelRenderer().setHumanRounding(false);

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

}
