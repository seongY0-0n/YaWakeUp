package org.techtown.yawakeup.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    int dap;
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




        int t = new Random().nextInt(4);

        if(t==0){
            first = (int) (Math.random()*1000);
            second = (int) (Math.random()*1000);
            dap=plus(first,second);
            mathText.setText(first + "\n + " + second );
        }else if(t==1){
            first = (int) (Math.random()*1000);
            second = (int) (Math.random()*1000);
            dap=minus(first,second);
            mathText.setText(first + "\n - " + second );
        }else if(t==2){
            dap=times(first,second);
            mathText.setText(first + "\n X " + second );
        }else if(t==3) {
            dap = divide(first, second);
            mathText.setText(first + "\n % " + second );
        }

        swipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer = answerText.getText().toString();
                if (answer.equals(Integer.toString((dap)))) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    flag = false;

                    //현재 시간 가져오기
                    long x= System.currentTimeMillis();
                    //저장하기 위해 며칠인지 셋팅하기 위해
                    SimpleDateFormat date = new SimpleDateFormat("dd",Locale.getDefault());

                    SharedPreferences sp = getSharedPreferences("11_sleep",MODE_PRIVATE);

                    SharedPreferences dp = getSharedPreferences("11_wake",MODE_PRIVATE); // 달 기준으로 저장
                    SharedPreferences.Editor timeEdit = dp.edit();

                    //오늘 날짜 in 며칠
                    String today = date.format(new Date(x));
                    //인티져 타
                    int t = Integer.parseInt(today);
                    //잠든 시간 계산
                    //
                    long y;
                    String yesterday;
                    SimpleDateFormat d = new SimpleDateFormat("HHmm",Locale.getDefault());
                    //만약에 오늘 자고 오늘 일어남
                   if(sp.getLong(Integer.toString(t),0) != 0){
                       //잠든 시간 가져오기
                       y= sp.getLong(Integer.toString(t),0);
                       yesterday = sameDate(d.format(new Date(y)), d.format(new Date(x)));
                   } else { // 어제 자고 오늘 일어남
                       y = sp.getLong(Integer.toString(t - 1), 0);
                       yesterday = diffDate(d.format(new Date(y)),d.format(new Date(x)));
                   }

                   timeEdit.putString(today,yesterday);
                   timeEdit.apply();



                    SharedPreferences p = getSharedPreferences("AlarmStatus",MODE_PRIVATE);
                    SharedPreferences.Editor edit = p.edit();
                    edit.putInt("알람 설정 상태", 0); // 1 알람 설정, 0 알람 없음
                    edit.apply();
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
    public int plus(int x, int y){
        return x+y;
    }
    public int minus(int x, int y){
        return x-y;
    }
    public int times(int x, int y){
        return x*y;
    }
    public int divide(int x, int y){
        return x/y;
    }
    public String sameDate(String s1, String s2){
        //s1이 잔시간 s2가 일어난 시간
        String d1=s1.substring(0,2);
        String d2=s1.substring(2,4);

        int si1 = Integer.parseInt(d1)*3600;
        int bun1 = Integer.parseInt(d2)*60;
        int jjin1= si1+bun1; // 일어

        String c1=s2.substring(0,2);
        String c2=s2.substring(2,4);

        int si2 = Integer.parseInt(c1)*3600;
        int bun2 = Integer.parseInt(c2)*60;
        int jjin2= si2+bun2;//잔

        int real= jjin1 - jjin2;

        int time = real/3600;
        int minute = real%3600/60;

        String str= Integer.toString(time) + Integer.toString(minute);
        return str ;
    }
    public String diffDate(String s1, String s2){
        String d1 = s1.substring(0,2);
        String d2 = s1.substring(2,4);

        int si = Integer.parseInt(d1)*3600;
        int bun = Integer.parseInt(d2)*60;

        int std1 = 24*3600 - (si+bun);

        d1= s2.substring(0,2);
        d2= s2.substring(2,4);

        si = Integer.parseInt(d1)*3600;
        bun = Integer.parseInt(d2)*60;

        int std2 = si+bun;
        int result = std1+std2;
        int si_result = result/3600;
        int bun_result = result%3600/60;

        String str = Integer.toString(si_result) + Integer.toString(bun_result);
        return str;

    }
}