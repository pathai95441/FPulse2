package com.example.fpulse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Profile.this,MapsActivity.class);
                startActivity(homeIntent);
            }
        });
        //Bundle mapviewbundle = null;
       // if(savedInstanceState != null){
           // mapviewbundle =savedInstanceState.getBundle(Keymap);

      //  }
        //mapView = (MapView) findViewById(R.id.mapViewa);
        //mapView.onCreate(mapviewbundle);

        //mapView.getMapAsync((OnMapReadyCallback) this);

        pulsetxt = (TextView) findViewById(R.id.pulsetxt);
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        //Schedule a task to run every 5 seconds (or however long you want)
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Do stuff here!
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Do stuff to update UI here!
                        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String string) {
                                //parseJsonData(string);
                                try {
                                    //code
                                    JSONObject object = new JSONObject(string);
                                    String myRequiredData = object.getString("livejson");
                                    JSONArray array = new JSONArray(myRequiredData);
                                    //Log.d("mosTag",array.toString());
                                    JSONObject obj = new JSONObject(array.get(0).toString());
                                    Log.d("mosTag",obj.getString("slot0"));
                                    pulsetxt.setText(obj.getString("slot0"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                                //dialog.dismiss();
                            }
                        });
                        RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
                        rQueue.add(request);

                    }
                });

            }
        }, 0, 1, TimeUnit.SECONDS); // or .MINUTES, .HOURS etc.



        //
    }
}
