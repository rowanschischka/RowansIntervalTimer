package com.rowanschischka.rowansintervaltimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {

    NumberPicker NumberPickerMinute1;
    NumberPicker NumberPickerSecond1;
    NumberPicker NumberPickerMinute2;
    NumberPicker NumberPickerSecond2;

    String saved_MIN1;
    String saved_SEC1;
    String saved_MIN2;
    String saved_SEC2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saved_MIN1 = getString(R.string.saved_MIN1);
        saved_SEC1 = getString(R.string.saved_SEC1);
        saved_MIN2 = getString(R.string.saved_MIN2);
        saved_SEC2 = getString(R.string.saved_SEC2);
        NumberPicker intervalValues[] = {
                NumberPickerMinute1 = findViewById(R.id.min1),
                NumberPickerSecond1 = findViewById(R.id.sec1),
                NumberPickerMinute2 = findViewById(R.id.min2),
                NumberPickerSecond2 = findViewById(R.id.sec2),
        };

        for (NumberPicker np : intervalValues) {
            np.setMaxValue(59);
            np.setMinValue(0);
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_time), Context.MODE_PRIVATE);
        NumberPickerMinute1.setValue(sharedPref.getInt(saved_MIN1, 0));
        NumberPickerSecond1.setValue(sharedPref.getInt(saved_SEC1, 30));
        NumberPickerMinute2.setValue(sharedPref.getInt(saved_MIN2, 0));
        NumberPickerSecond2.setValue(sharedPref.getInt(saved_SEC2, 10));
    }

    public void onClickStart(View view) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_time), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(saved_MIN1, NumberPickerMinute1.getValue());
        //editor.apply();
        editor.putInt(saved_SEC1, NumberPickerSecond1.getValue());
        //editor.apply();
        editor.putInt(saved_MIN2, NumberPickerMinute2.getValue());
        //editor.apply();
        editor.putInt(saved_SEC2, NumberPickerSecond2.getValue());
        editor.apply();

        Intent myIntent = new Intent(MainActivity.this, RunningTimerActivity.class);
        this.startActivity(myIntent);
    }
}
