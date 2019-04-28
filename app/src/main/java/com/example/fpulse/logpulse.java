package com.example.fpulse;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Map;


public class logpulse extends AppCompatActivity {

    LineChart mpLineChart;
    DatabaseReference mDatabase;
    GraphView graphView;
    LineGraphSeries series;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logpulse);

        graphView = (GraphView) findViewById(R.id.graph1);
        series = new LineGraphSeries();
        graphView.addSeries(series);
        mDatabase = FirebaseDatabase.getInstance().getReference("logs");


        }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.addValueEventListener(new ValueEventListener() {
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
    }
    //
//    private ArrayList<Entry> dataValues1(){
//        final ArrayList<Entry> dataval = new ArrayList<Entry>();
//        mDatabase = FirebaseDatabase.getInstance().getReference("logs");
//
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                int i=0,c = 0;
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    UsersGetter logs = snapshot.getValue(UsersGetter.class);
//                    Log.d("mytag",snapshot.getKey().toString());
//                    Log.d("mytag", logs.getPulseValue());
//                    c = Integer.parseInt(logs.getPulseValue().toString());
//
//                    dataval.add(new Entry(i,c));
//
//                }
//                Log.d("mytag", "TEST");
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return dataval;
//    }
}
