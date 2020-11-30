package org.techtown.yawakeup.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.techtown.yawakeup.R;

public class IntentWeather extends AppCompatActivity {

    class BtnOnClickListener implements Button.OnClickListener {
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.weather_button:
                    intentWeather();
                    break;
                case R.id.covid_button:
                    intentCovid();
                    break;
                case R.id.dust_button:
                    intentDust();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_weather);
       BtnOnClickListener onClickListener = new BtnOnClickListener();

       Button buttonWeather = (Button) findViewById(R.id.weather_button);
       buttonWeather.setOnClickListener(onClickListener);
       Button buttonCovid = (Button) findViewById(R.id.covid_button);
       buttonCovid.setOnClickListener(onClickListener);
       Button buttonDust = (Button) findViewById(R.id.dust_button);
       buttonDust.setOnClickListener(onClickListener);

    }

    private void intentWeather() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EB%82%A0%EC%94%A8"));
        startActivity(intent);
    }

    private void intentCovid() {
        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=tab_sug.top&where=nexearch&query=%EC%BD%94%EB%A1%9C%EB%82%98+%ED%99%95%EC%A7%84%EC%9E%90&oquery=%EB%82%A0%EC%94%A8&tqi=U8MBWlp0Yidsss23QF4ssssstyV-270845&acq=%EC%BD%94%EB%A1%9C%EB%82%98+%ED%99%95&acr=1&qdt=0"));
        startActivity(intent2);
    }

    private void intentDust() {
        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EB%AF%B8%EC%84%B8%EB%A8%BC%EC%A7%80"));
        startActivity(intent3);
    }
}