package com.example.dat.testcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements OnDateSelectedListener {

    @InjectView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        calendarView.setOnDateChangedListener(this);
    }

    @Override
    public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
        if (selected) {
            Toast.makeText(this, date.getDate().toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
