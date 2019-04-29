package com.example.fpulse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


public class logpulse extends AppCompatActivity {

    LineChart mpLineChart;
    DatabaseReference mDatabase;
    GraphView graphView;
    LineGraphSeries series;
    EditText D,M,Y;
    Button okbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logpulse);
        D = (EditText) findViewById(R.id.DeditText);
        M = (EditText) findViewById(R.id.MeditText2);
        Y = (EditText) findViewById(R.id.YeditText5);
        okbtn = (Button) findViewById(R.id.OKbtn);
        graphView = (GraphView) findViewById(R.id.graph1);
        series = new LineGraphSeries();
        graphView.addSeries(series);
        mDatabase = FirebaseDatabase.getInstance().getReference("Logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()) .child(Y.getText().toString()+"-"+M.getText().toString()+"-"+D.getText().toString());
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
