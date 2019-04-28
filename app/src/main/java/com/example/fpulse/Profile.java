package com.example.fpulse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
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
//import com.google.android.gms.maps.MapView;
//import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Profile extends AppCompatActivity {
    TextView pulsetxt;
    Button link;
    Button log;
    ImageView imagepro;

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
        onStart();

        //Bundle mapviewbundle = null;
       // if(savedInstanceState != null){
           // mapviewbundle =savedInstanceState.getBundle(Keymap);

      //  }
        //mapView = (MapView) findViewById(R.id.mapViewa);
        //mapView.onCreate(mapviewbundle);

        //mapView.getMapAsync((OnMapReadyCallback) this);

//        pulsetxt = (TextView) findViewById(R.id.pulsetxt);
//        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
//
//        //Schedule a task to run every 5 seconds (or however long you want)
//        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                // Do stuff here!
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // Do stuff to update UI here!
//                        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String string) {
//                                //parseJsonData(string);
//                                try {
//                                    //code
//                                    JSONObject object = new JSONObject(string);
//                                    String myRequiredData = object.getString("livejson");
//                                    JSONArray array = new JSONArray(myRequiredData);
//                                    //Log.d("mosTag",array.toString());
//                                    JSONObject obj = new JSONObject(array.get(0).toString());
//                                    Log.d("mosTag",obj.getString("slot0"));
//                                    pulsetxt.setText(obj.getString("slot0"));
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError volleyError) {
//                                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
//                                //dialog.dismiss();
//                            }
//                        });
//                        RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
//                        rQueue.add(request);
//
//                    }
//                });
//
//            }
//        }, 0, 1, TimeUnit.SECONDS); // or .MINUTES, .HOURS etc.



        //
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
                            .into(imagepro);
                  pulsetxt.setText(user.getPulseshow());

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
