package org.techtown.yawakeup.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.yawakeup.R;

import java.util.Random;

public class Vocabulary extends AppCompatActivity {
    private TextView eng_word;
    private TextView kor_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        eng_word = findViewById(R.id.eng_word);
        kor_word = findViewById(R.id.kor_word);

        String[] eng = getResources().getStringArray(R.array.eng_word);
        String[] kor = getResources().getStringArray(R.array.kor_word);
        int th = new Random().nextInt(eng.length);

        eng_word.setText(eng[th]);
        kor_word.setText(kor[th]);

        Button goIntent = (Button) findViewById(R.id.goto_intentweather);
        goIntent.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vocabulary.this, IntentWeather.class);
                startActivity(intent);
            }
        });
    }
}