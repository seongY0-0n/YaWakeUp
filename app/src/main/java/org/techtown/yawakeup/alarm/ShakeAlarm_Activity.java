package org.techtown.yawakeup.alarm;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.yawakeup.R;

import java.util.Calendar;

public class ShakeAlarm_Activity extends AppCompatActivity implements SensorEventListener {
    TextView timeText;
    Button cancel;
    Boolean flag = true;
    java.util.Calendar calendar;
    MediaPlayer mediaPlayer;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private int shakeCount;
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private long mShakeTime;
    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;


    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_shake);


        cancel=findViewById(R.id.shakeCancel);

        shakeCount=0;
        //센서 객체
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        // 잠금 화면 위로 activity 띄워줌
        timeText=(TextView)findViewById(R.id.timetext);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.solo);   // 소리를 재생할 MediaPlayer
        mediaPlayer.setLooping(true);   // 무한반복
        mediaPlayer.start();

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
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    flag = false;
                    finish();
            }
        }); // 버튼 눌러서 해제




    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//            Log.e("ddd","shake");
//            if(shakeCount==10){
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                finish();
//            }
//            float x = event.values[0];
//            float y = event.values[1];
//            float z = event.values[2];
//
//            float gravityX = x / SensorManager.GRAVITY_EARTH;
//            float gravityY = y / SensorManager.GRAVITY_EARTH;
//            float gravityZ = z / SensorManager.GRAVITY_EARTH;
//
//            Float f =  gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ;
//            double squaredD = Math.sqrt(f.doubleValue());
//            float gForce = (float)squaredD;
//            Log.d("TAG","흔들림 감지");
//            Toast.makeText(getApplicationContext(),String.valueOf(gForce)
//                    +" 흔들림이 감지 되었습니다.   " + SensorManager.GRAVITY_EARTH,Toast.LENGTH_SHORT).show();
//            if (gForce > HOLD_GRAVITY){
//                long currentTime=System.currentTimeMillis();
//                if(mShakeTime + 500 > currentTime){
//                    return;
//                }
//                mShakeTime=currentTime;
//                shakeCount++;
//                Log.d("TAG","흔들림 감지");
//                Toast.makeText(getApplicationContext(),String.valueOf(gForce)
//                        +" 흔들림이 감지 되었습니다.   " + SensorManager.GRAVITY_EARTH,Toast.LENGTH_SHORT).show();
//            }
//
//        }
        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            double dForce = Math.sqrt(gX * gX + gY * gY + gZ * gZ);
            float gForce= (float)dForce;
            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }
                Log.d("TAG","흔들림 감지");

                mShakeTimestamp = now;
                mShakeCount++;

                mListener.onShake(mShakeCount);
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.e("LOG", "onPause()");
        sensorManager.unregisterListener(this);
    }
}
