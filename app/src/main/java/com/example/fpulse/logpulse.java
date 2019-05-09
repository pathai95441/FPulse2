package com.example.fpulse;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import com.fourmob.datetimepicker.date.DatePickerDialog;


public class logpulse extends AppCompatActivity {

    LineChart mpLineChart;
    DatabaseReference mDatabase;
    GraphView graphView;
    LineGraphSeries series;
    EditText D,M,Y;
    Button dateBtn;
    private Calendar mCalendarThis;
    TextView dateText;
    //private DatePickerDialog mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logpulse);
        dateText = (TextView) findViewById(R.id.dateText);
        dateBtn = (Button) findViewById(R.id.dateBtn);
        graphView = (GraphView) findViewById(R.id.graph1);
        //series = new LineGraphSeries();
        //graphView.addSeries(series);
        mDatabase = FirebaseDatabase.getInstance().getReference("Logs").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mCalendarThis = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Log.d("mytag","date picker call back");
                mCalendarThis.set(Calendar.YEAR, year);
                mCalendarThis.set(Calendar.MONTH, monthOfYear);
                mCalendarThis.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date date = mCalendarThis.getTime();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String textDate = df.format(date);
                Log.d("mytag",textDate);
                dateText.setText(textDate);
                mDatabase.child(textDate).addValueEventListener(new ValueEventListener() {
                    String hourTmp = "";
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LineGraphSeries<DataPoint> arrlist = new LineGraphSeries();
                        String dateFromDB = dataSnapshot.getKey();
                        Log.d("mytag",dateFromDB);
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
                                    Log.d("mytag",date.toString());
                                    UsersGetter user = mydatasnapshot.getValue(UsersGetter.class);
                                    DataPoint dataTmp = new DataPoint(date, Double.parseDouble(user.getPulseValue()));
                                    arrlist.appendData(dataTmp,false,23);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        arrlist.setDrawDataPoints(true);
                        arrlist.setDataPointsRadius(10);
                        graphView.removeAllSeries();
                        graphView.addSeries(arrlist);
                        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(logpulse.this));
                        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(logpulse.this){
                            @Override
                            public String formatLabel(double value, boolean isValueX) {
                                if(isValueX) {
                                    DateFormat df = new SimpleDateFormat("HH");
                                    mCalendar.setTimeInMillis((long) value);
                                    String timestr = df.format(mCalendar.getTime());
                                    Log.d("mytag","from graph :"+timestr);
                                    return (timestr);
                                }else{
                                    return super.formatLabel(value, isValueX);
                                }

                            }
                        });
                        //graphView.getGridLabelRenderer().setNumHorizontalLabels(4); // only 4 because of the space
                        //graphView.getViewport().setXAxisBoundsManual(true);
                        //graphView.getGridLabelRenderer().setHumanRounding(false);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        };
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mytag","button clicked");
                new DatePickerDialog(logpulse.this,date , mCalendarThis
                        .get(Calendar.YEAR), mCalendarThis.get(Calendar.MONTH),
                        mCalendarThis.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        }

}
