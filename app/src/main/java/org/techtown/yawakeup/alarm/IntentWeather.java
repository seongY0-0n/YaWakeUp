package org.techtown.yawakeup.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntentWeather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_weather);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=tab_hty.top&where=nexearch&query=%EC%BD%94%EB%A1%9C%EB%82%98+%ED%99%95%EC%A7%84%EC%9E%90&oquery=%EC%BD%94%EB%A1%9C%EB%82%98&tqi=UJAIjsp0JXossuaoZf0ssssssu4-012773"));
                startActivity(intent);
                try {
                    Thread.sleep(15000);

                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EB%82%A0%EC%94%A8"));
                startActivity(intent2);
                try {
                    Thread.sleep(30000);

                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EB%AF%B8%EC%84%B8%EB%A8%BC%EC%A7%80"));
                startActivity(intent3);
            }
        });
    }
}