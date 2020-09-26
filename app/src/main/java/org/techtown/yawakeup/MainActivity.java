package org.techtown.yawakeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onAlarm(View view){
        Intent intent= new Intent(this,Alarm_Activity.class);
        startActivity(intent);
    }

    public void onCheck(View view){
        Intent intent= new Intent(this,Check_Activity.class);
        startActivity(intent);
    }
    public void onMute(View view){
        Intent intent= new Intent(this,Mute_Activity.class);
        startActivity(intent);
    }
    public void onShare(View view){
        Intent intent= new Intent(this,Share_Activity.class);
        startActivity(intent);
    }
    public void onSet(View view){
        Intent intent= new Intent(this,Set_Activity.class);
        startActivity(intent);
    }
    public void onAcc(View view){
        Intent intent= new Intent(this,Acc_Activity.class);
        startActivity(intent);
    }
}