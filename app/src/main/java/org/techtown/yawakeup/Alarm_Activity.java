package org.techtown.yawakeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class Alarm_Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
    }
    public void setAlarm(View view){
        //버튼 누르면 액티비티 전환
        Intent intent = new Intent(this,BasicAlarm_Activity.class);
        startActivity(intent);
    }



}
