package org.techtown.yawakeup.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    }
}