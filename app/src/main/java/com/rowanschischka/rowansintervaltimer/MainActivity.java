package com.rowanschischka.rowansintervaltimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {

    private NumberPicker NumberPickerWorkMinute;
    private NumberPicker NumberPickerWorkSecond;
    private NumberPicker NumberPickerRestMinute;
    private NumberPicker NumberPickerRestSecond;

    private String savedMinWork;
    private String savedSecWork;
    private String SavedMinRest;
    private String savedSevRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedMinWork = getString(R.string.saved_work_min);
        savedSecWork = getString(R.string.saved_rest_sec);
        SavedMinRest = getString(R.string.saved_rest_min);
        savedSevRest = getString(R.string.saved_work_sec);
        NumberPicker intervalValues[] = {
                NumberPickerWorkMinute = findViewById(R.id.min_work),
                NumberPickerWorkSecond = findViewById(R.id.sec_work),
                NumberPickerRestMinute = findViewById(R.id.min_rest),
                NumberPickerRestSecond = findViewById(R.id.sec_rest),
        };

        for (NumberPicker np : intervalValues) {
            np.setMaxValue(59);
            np.setMinValue(0);
        }

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_time), Context.MODE_PRIVATE);
        NumberPickerWorkMinute.setValue(sharedPref.getInt(savedMinWork, 0));
        NumberPickerWorkSecond.setValue(sharedPref.getInt(savedSecWork, 30));
        NumberPickerRestMinute.setValue(sharedPref.getInt(SavedMinRest, 0));
        NumberPickerRestSecond.setValue(sharedPref.getInt(savedSevRest, 10));
    }

    public void onClickStart(View view) {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.saved_time), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(savedMinWork, NumberPickerWorkMinute.getValue());
        //editor.apply();
        editor.putInt(savedSecWork, NumberPickerWorkSecond.getValue());
        //editor.apply();
        editor.putInt(SavedMinRest, NumberPickerRestMinute.getValue());
        //editor.apply();
        editor.putInt(savedSevRest, NumberPickerRestSecond.getValue());
        editor.apply();

        Intent myIntent = new Intent(MainActivity.this, RunningTimerActivity.class);
        this.startActivity(myIntent);
    }
}
