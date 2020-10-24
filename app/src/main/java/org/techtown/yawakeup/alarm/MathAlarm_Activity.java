package org.techtown.yawakeup.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.yawakeup.R;

import java.util.Calendar;
import java.util.Random;

public class MathAlarm_Activity extends AppCompatActivity {
    java.util.Calendar calendar;
    Button swipeButton;// 알람 해제 뷰
    TextView mathText; // 문제 출력 뷰
    TextView timeText; // 실시간 시간 출력 뷰
    EditText answerText; // 정답 입력
    MediaPlayer mediaPlayer; // 뮤직 플레이어
    //수학 문제
    int first;
    int second;
    int cancelMode = 0;
    String answer; // 문제 정답
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_math);
        calendar = java.util.Calendar.getInstance();
        swipeButton = findViewById(R.id.button10);
        timeText = (TextView) findViewById(R.id.alarmText);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        // 잠금 화면 위로 activity 띄워줌


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





        // 문제 맞추기
        mathText= findViewById(R.id.mathText);
        answerText=findViewById(R.id.answerText);
        first = (int) (Math.random()*100);
        second = (int) (Math.random()*100);
        mathText.setText(first + "\n X " + second );




        swipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = answerText.getText().toString();
                if (answer.equals(Integer.toString((first * second)))) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    flag = false;
                    SharedPreferences sp = getSharedPreferences("AlarmStatus",MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("알람 설정 상태", 0); // 1 알람 설정, 0 알람 없음
                    edit.commit();
                    finish();
                    int type = new Random().nextInt(2);
                    if(type == 0) {
                        Intent intent = new Intent(MathAlarm_Activity.this, Vocabulary.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MathAlarm_Activity.this, Quotation.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(MathAlarm_Activity.this, "아직 잠이 덜깼네요^^", Toast.LENGTH_SHORT).show();
                }
            }
        }); // 버튼 눌러서 해제
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MathAlarm_Activity.this, "어딜 나가려구 ^^", Toast.LENGTH_SHORT).show();
        //super.onBackPressed();
    }
}