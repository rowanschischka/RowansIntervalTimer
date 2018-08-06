package com.rowanschischka.rowansintervaltimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {
    private static final int MIN1= 0;
    private static final int SEC1=1;
    private static final int MIN2=2;
    private static final int SEC2=3;

    private NumberPicker[] intervalValues= new NumberPicker[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intervalValues[MIN1] = findViewById(R.id.min1);
        intervalValues[SEC1] = findViewById(R.id.sec1);
        intervalValues[MIN2] = findViewById(R.id.min2);
        intervalValues[SEC2] = findViewById(R.id.sec2);

        for(NumberPicker np : intervalValues){
            np.setMaxValue(59);
            np.setMinValue(0);
        }
    }

    public void onClickStart(View view) {

    }
}
