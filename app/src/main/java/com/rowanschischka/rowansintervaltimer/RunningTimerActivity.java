package com.rowanschischka.rowansintervaltimer;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class RunningTimerActivity extends AppCompatActivity {
    public Handler setTimeTextHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            TextView timeText = findViewById(R.id.timeText);
            long time = (long) msg.obj;
            long seconds = time / 1000;
            long milli = (time - seconds * 1000) / 100;
            String text = seconds + "." + milli;
            timeText.setText(text);
        }
    };
    int updatePeriod = 100;
    long startTime;
    int time1Millis;
    int time2Millis;
    int currentIntervalTime;
    boolean interval = true;
    ToneGenerator tg;
    public Handler switchIntervalHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            LinearLayout layout = findViewById(R.id.runningTimer);
            startTime = SystemClock.uptimeMillis();
            if (interval) {
                currentIntervalTime = time2Millis;
                layout.setBackgroundColor(getResources().getColor(R.color.time2));
                tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
            } else {
                currentIntervalTime = time1Millis;
                layout.setBackgroundColor(getResources().getColor(R.color.time1));
                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            }
            interval = !interval;
        }
    };
    Timer timer;

    @Override
    protected void onPause() {
        timer.cancel();
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        String saved_MIN1 = getString(R.string.saved_MIN1);
        String saved_SEC1 = getString(R.string.saved_SEC1);
        String saved_MIN2 = getString(R.string.saved_MIN2);
        String saved_SEC2 = getString(R.string.saved_SEC2);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_time), Context.MODE_PRIVATE);
        int min1 = (sharedPref.getInt(saved_MIN1, 0));
        int sec1 = (sharedPref.getInt(saved_SEC1, 30));
        time1Millis = (min1 * 60 + sec1) * 1000;
        int min2 = (sharedPref.getInt(saved_MIN2, 0));
        int sec2 = (sharedPref.getInt(saved_SEC2, 10));
        time2Millis = (min2 * 60 + sec2) * 1000;

        currentIntervalTime = time1Millis;

        startTime = SystemClock.uptimeMillis();

        TimerTask timerTask = new MyTimerTask();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, updatePeriod);
    }

    public void onClickStop(View view) {
        finish();
    }

    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            long upTime = SystemClock.uptimeMillis();
            long elapsedTimeMilli = upTime - startTime;

            long displayTime = currentIntervalTime - elapsedTimeMilli;

            Log.d("TIME",
                    "elapsedTimeMilli " + elapsedTimeMilli +
                            " displayTime " + displayTime +
                            "currentIntervalTime " + currentIntervalTime
            );
            if (displayTime >= 0) {
                Message displayMessage = setTimeTextHandler.obtainMessage(0, displayTime);
                displayMessage.sendToTarget();
            }
            //swap time interval
            if (displayTime <= 0) {
                Message switchInterval = switchIntervalHandler.obtainMessage();
                switchInterval.sendToTarget();
            }
        }
    }
}