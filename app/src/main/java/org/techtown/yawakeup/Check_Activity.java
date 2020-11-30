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
    private TextView checkTotal, checkEx;
    private Button getSleepTime;
    SimpleDateFormat dayFormat;
    String minusDay, day;
    int dayBefore= 5*24*60*60*1000;
    int dayAfter=24*60*60*1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        checkTotal = findViewById(R.id.checkTotal);
        checkEx = findViewById(R.id.checkExt);

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
        SharedPreferences.Editor ed = dp.edit();
        ed.apply();


        int sleepTotal = 0;
        for(int i = Integer.parseInt(minusDay); i <=Integer.parseInt(day); i++ ){
            String str = dp.getString(Integer.toString(i),"0");
            sleepTotal += Integer.parseInt(str);
            mBarChart.addBar(new BarModel(i + "일",Integer.parseInt(str)/100,0xFF1FF4AC ));
        }

        int howT = sleepTotal / 700;
        Log.e("몇시간 잤니 ?", Integer.toString(howT));
        checkTotal.setText("이번 주 평균 수면 시간은\n" +Integer.toString(howT) + "시간!");
        if (howT < 7 ){
            checkEx.setText("피곤한 삶을 살고 계시네요.\n잠이 부족하면 면역력이 약해지고, 몸이 망가져요.\n오늘은 일찍 자세요~");
        } else if (howT > 7 && howT < 8 || howT == 7 || howT ==8){
            checkEx.setText("완벽한 수면 패턴! 건강한 당신!");
        } else if(howT >8){
            checkEx.setText("과수면! 과수면! 과수면!\n과도한 잠은 비만과 무기력 증을 유발할 수 있습니다.\n내일은 조금 더 일찍 일어나볼까요?");
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
