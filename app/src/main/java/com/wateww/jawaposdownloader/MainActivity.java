package com.wateww.jawaposdownloader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES = "PREFERENCE" ;
    private static final String PREF_TIME_HOUR = "PREF_TIME_HOUR" ;
    private static final String PREF_TIME_MINUTE = "PREF_TIME_MINUTE" ;
    private static final String PREF_SWITCH_ON = "PREF_SWITCH_ON";
    private static final String TIME_FORMAT = "%02d:%02d" ;

    private TimePicker mTimePicker;
    private TextView mTimeText;
    private Switch mSwitch;
    private PendingIntent pendingIntent;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        mTimeText = (TextView)findViewById(R.id.txt_time);
        mTimeText.setText(String.format(Locale.getDefault(), TIME_FORMAT,
                sharedpreferences.getInt(PREF_TIME_HOUR, 0),
                sharedpreferences.getInt(PREF_TIME_MINUTE, 0)));

        mTimePicker = (TimePicker)findViewById(R.id.time_picker);
        mTimePicker.setIs24HourView(true);

        mSwitch = (Switch)findViewById(R.id.switch_download);
        mSwitch.setChecked(sharedpreferences.getBoolean(PREF_SWITCH_ON, false));
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(PREF_SWITCH_ON, b);
                editor.apply();
                if (b) {
                    Toast.makeText(MainActivity.this, "Downloader is ON", Toast.LENGTH_SHORT).show();
                    startTimer();
                } else {
                    Toast.makeText(MainActivity.this, "Downloader is OFF", Toast.LENGTH_SHORT).show();
                    cancelTimer();
                }
            }
        });

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
    }

    public void setTime(View view) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        //noinspection deprecation
        int hour = mTimePicker.getCurrentHour();
        //noinspection deprecation
        int minute = mTimePicker.getCurrentMinute();
        mTimeText.setText(String.format(Locale.getDefault(), TIME_FORMAT, hour, minute));
        editor.putInt(PREF_TIME_HOUR, hour);
        editor.putInt(PREF_TIME_MINUTE, minute);
        editor.apply();

        if (mSwitch.isChecked()) {
            cancelTimer();
            startTimer();
        }
    }

    private void cancelTimer() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }

    private void startTimer() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 60 * 24;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, sharedpreferences.getInt(PREF_TIME_HOUR, 0));
        calendar.set(Calendar.MINUTE, sharedpreferences.getInt(PREF_TIME_MINUTE, 0) - 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.HOUR_OF_DAY, 24);
        }

        manager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), interval, pendingIntent);
    }
}
