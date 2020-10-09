package org.techtown.yawakeup;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class ShakeAlarm_Activity extends AppCompatActivity implements SensorEventListener {
    TextView timeText;
    Boolean flag = true;
    java.util.Calendar calendar;

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_shake);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        // 잠금 화면 위로 activity 띄워줌
        timeText=(TextView)findViewById(R.id.timetext);



        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag == true) {
                    try {
                        calendar = java.util.Calendar.getInstance();
                        if (calendar.get(java.util.Calendar.HOUR_OF_DAY) > 0 && calendar.get(java.util.Calendar.HOUR_OF_DAY) < 12) {
                            timeText.setText("AM " + calendar.get(java.util.Calendar.HOUR_OF_DAY) + "시 " + calendar.get(java.util.Calendar.MINUTE) + "분 " + calendar.get(java.util.Calendar.SECOND) + "초");
                        } else if (calendar.get(java.util.Calendar.HOUR_OF_DAY) == 12) {
                            timeText.setText("PM " + calendar.get(java.util.Calendar.HOUR_OF_DAY) + "시 " + calendar.get(java.util.Calendar.MINUTE) + "분 " + calendar.get(java.util.Calendar.SECOND) + "초");
                        } else if (calendar.get(java.util.Calendar.HOUR_OF_DAY) > 12 && calendar.get(java.util.Calendar.HOUR_OF_DAY) < 24) {
                            timeText.setText("PM " + (calendar.get(java.util.Calendar.HOUR_OF_DAY) - 12) + "시 " + calendar.get(java.util.Calendar.MINUTE) + "분 " + calendar.get(java.util.Calendar.SECOND) + "초");
                        } else if (calendar.get(java.util.Calendar.HOUR_OF_DAY) == 0) {
                            timeText.setText("AM 0시 " + calendar.get(java.util.Calendar.MINUTE) + "분 " + calendar.get(Calendar.SECOND) + "초");
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }).start(); // 실시간으로 시계 출력




    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
