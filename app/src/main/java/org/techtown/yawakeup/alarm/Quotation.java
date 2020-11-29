package org.techtown.yawakeup.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        Button goIntent = (Button) findViewById(R.id.goto_intentweather);
        goIntent.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Quotation.this, IntentWeather.class);
                startActivity(intent);
            }
        });




    }
}