package org.techtown.yawakeup.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.techtown.yawakeup.R;

import java.util.Random;

public class Quotation extends AppCompatActivity {

    private TextView quotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);


        quotation = findViewById(R.id.quotation);
        String[] array = getResources().getStringArray(R.array.quotation);

        int th = new Random().nextInt(array.length);
        quotation.setText(array[th]);




    }
}