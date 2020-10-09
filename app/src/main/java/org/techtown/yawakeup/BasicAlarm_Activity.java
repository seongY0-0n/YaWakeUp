package org.techtown.yawakeup;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class BasicAlarm_Activity extends AppCompatActivity{
        ImageButton alarmButton;
        int alarmHour=0, alarmMinute=0;
        Calendar alarmCalendar;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_basicalarm);
                alarmButton = (ImageButton)findViewById(R.id.alarmButton);
                //버튼 누르면 시계 화면 출력
                alarmButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                                //시간 셋팅
                                TimePickerDialog timePickerDialog = new TimePickerDialog(BasicAlarm_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                alarmHour= hourOfDay;
                                                alarmMinute=minute;
                                                setAlarm();
                                        }
                                },alarmHour,alarmMinute, false);
                                timePickerDialog.show();
                        }
                });



                //알람 해제 종류 방법 설정
                SharedPreferences alarmSp = this.getSharedPreferences("AlarmCancelMode",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = alarmSp.edit();
                edit.putInt("알람해제방법", 1); // 우선 수학 문제만
                edit.commit();

        }

        void setAlarm(){
                alarmCalendar= Calendar.getInstance();
                alarmCalendar.setTimeInMillis(System.currentTimeMillis());
                alarmCalendar.set(Calendar.HOUR_OF_DAY,alarmHour);
                alarmCalendar.set(Calendar.MINUTE,alarmMinute);
                alarmCalendar.set(Calendar.SECOND,0);
                Toast.makeText(BasicAlarm_Activity.this, alarmHour + "시" + alarmMinute + "분 에 설정 됨",Toast.LENGTH_SHORT).show();
                //timePickerDialog 에서 설정한 시간을 알림으로 설정
                Log.e("TAG", "Still yet");
                if(alarmCalendar.before(Calendar.getInstance())) alarmCalendar.add(Calendar.DATE,1);
                //알람 시간이 현재시간보다 느 때 하루 뒤로 맞춤
                Intent alarmIntent = new Intent(getApplicationContext(),Alarm_Receiver.class);
                AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmIntent.setAction(Alarm_Receiver.ACTION_RESTART_SERVICE);
                PendingIntent alarmCallPendingIntent = PendingIntent.getBroadcast(BasicAlarm_Activity.this,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.e("TAG", "위에");
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                        Log.e("TAG", "아래거");
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),alarmCallPendingIntent);
                }// 알람 설정

        }
}