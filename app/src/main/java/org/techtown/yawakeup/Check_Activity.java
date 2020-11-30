package org.techtown.yawakeup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;




import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;
import org.techtown.yawakeup.alarm.MathAlarm_Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Check_Activity extends Activity implements View.OnClickListener {

    private Button getSleepTime;
    SimpleDateFormat dayFormat;
    String minusDay, day;
    int dayBefore= 5*24*60*60*1000;
    int dayAfter=24*60*60*1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        //잠자는 시간
        getSleepTime = findViewById(R.id.setWake);
        getSleepTime.setOnClickListener(this);


        BarChart mBarChart = (BarChart) findViewById(R.id.bar_chart);

        long now = System.currentTimeMillis();
        long before = System.currentTimeMillis() - dayBefore;

        dayFormat = new SimpleDateFormat("dd",Locale.getDefault());


        //오늘 날짜
        day = dayFormat.format(new Date(now));
        //7일전 날짜
        minusDay = dayFormat.format(new Date(before));

        SharedPreferences dp = getSharedPreferences("11_wake",MODE_PRIVATE); // 달 기준으로 저장


        for(int i = Integer.parseInt(minusDay); i <=Integer.parseInt(day); i++ ){
            String str = dp.getString(Integer.toString(i),"0");
            mBarChart.addBar(new BarModel(i + "일",Integer.parseInt(str)/100,0xFF1FF4AC ));
        }

        mBarChart.startAnimation();

    }

    @Override
    public void onClick(View v) {
        long x= System.currentTimeMillis();

        SharedPreferences sp = getSharedPreferences("11_sleep",MODE_PRIVATE); // 달 기준으로 저장
        SharedPreferences.Editor editor = sp.edit();
        //저장 오늘 날짜 두자리수 기준으로, 시간 분을 넘겨줌
        editor.putLong(dayFormat.format(new Date(x)), x);
        editor.apply();
        Toast.makeText(this, "수면 시간 입력 완료! 잘자요~", Toast.LENGTH_SHORT).show();

    }

}
