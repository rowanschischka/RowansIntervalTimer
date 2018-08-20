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
    private static long startTime;
    private static int currentIntervalTime;
    private static int workIntervalMilli;
    private static int restIntervalMilli;
    private static Timer timerWorkInterval;
    private static Timer timerRestInterval;
    private static Timer timerUpdateUI;
    private static boolean intervalSwitch;
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
    ToneGenerator tg;
    public Handler switchIntervalHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            LinearLayout layout = findViewById(R.id.runningTimer);
            startTime = SystemClock.uptimeMillis();
            if (intervalSwitch) {
                layout.setBackgroundColor(getResources().getColor(R.color.background_work));
                currentIntervalTime = workIntervalMilli;
                tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
            } else {
                layout.setBackgroundColor(getResources().getColor(R.color.background_rest));
                currentIntervalTime = restIntervalMilli;
                tg.startTone(ToneGenerator.TONE_PROP_BEEP);
            }
            intervalSwitch = !intervalSwitch;
        }
    };

    @Override
    protected void onStop() {
        timerUpdateUI.cancel();
        timerWorkInterval.cancel();
        timerRestInterval.cancel();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.running_timer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        String saved_work_min = getString(R.string.saved_work_min);
        String saved_work_sec = getString(R.string.saved_work_sec);
        String saved_rest_min = getString(R.string.saved_rest_min);
        String saved_rest_sec = getString(R.string.saved_rest_sec);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_time), Context.MODE_PRIVATE);
        int work_min = (sharedPref.getInt(saved_work_min, 0));
        int work_sec = (sharedPref.getInt(saved_rest_sec, 30));
        workIntervalMilli = (work_min * 60 + work_sec) * 1000;

        int rest_min = (sharedPref.getInt(saved_rest_min, 0));
        int rest_sec = (sharedPref.getInt(saved_work_sec, 10));
        restIntervalMilli = (rest_min * 60 + rest_sec) * 1000;

        timerUpdateUI = new Timer(true);
        timerWorkInterval = new Timer(true);
        timerRestInterval = new Timer(true);
    }

    @Override
    protected void onStart() {
        intervalSwitch = true;
        int startDelayMilli = 10000;
        currentIntervalTime = startDelayMilli;
        startTime = SystemClock.uptimeMillis();
        int updatePeriodUI = 100;
        TimerTask updateUITimerTask = new UpdateUITimerTask();
        timerUpdateUI.scheduleAtFixedRate(updateUITimerTask, 0, updatePeriodUI);
        TimerTask workTimerTask = new SwitchIntervalTimerTask();
        timerWorkInterval.scheduleAtFixedRate(workTimerTask, startDelayMilli, workIntervalMilli + restIntervalMilli);
        TimerTask restTimerTask = new SwitchIntervalTimerTask();
        timerRestInterval.scheduleAtFixedRate(restTimerTask, startDelayMilli + workIntervalMilli, workIntervalMilli + restIntervalMilli);
        super.onStart();
    }

    public void onClickStop(View view) {
        finish();
    }

    private class SwitchIntervalTimerTask extends TimerTask {

        @Override
        public void run() {
            switchIntervalHandler.sendEmptyMessage(0);
        }
    }

    private class UpdateUITimerTask extends TimerTask {

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
            } else {
                Message displayMessage = setTimeTextHandler.obtainMessage(0, 0L);
                displayMessage.sendToTarget();
            }
        }
    }
}