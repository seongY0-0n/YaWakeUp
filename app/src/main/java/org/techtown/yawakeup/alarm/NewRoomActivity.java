package org.techtown.yawakeup.alarm;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.techtown.yawakeup.R;
import org.techtown.yawakeup.Share_Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewRoomActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText EditText_title, EditText_count, EditText_contents;
    private TextView TextView_Date, TextView_Time;

    private DatabaseReference mDatabase;

    private String dateString, timeString;

    private int alarmHour, alarmMinute; //알람 설정용 변

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        EditText_title = findViewById(R.id.room_title_et);
        EditText_count = findViewById(R.id.room_count_et);
        EditText_contents = findViewById(R.id.room_contents_et);

        TextView_Date = (TextView) findViewById(R.id.room_date_tv);
        TextView_Time = (TextView) findViewById(R.id.room_time_tv);

        Button room_date_btn = findViewById(R.id.room_date_btn);
        Button room_time_btn = findViewById(R.id.room_time_btn);
        Button room_save_btn = findViewById(R.id.room_save_btn);


        //날짜 버튼 리스너
        room_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        //시간 버튼 리스너
        room_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });

        room_save_btn.setOnClickListener(this);

    }


    //날짜 set
    public void showDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView_Date.setText("날짜 : " + year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendar.set(year, month, dayOfMonth);
                dateString = sdf.format(calendar.getTime());

                Log.v("date", dateString);

            }
        }, 2020, 10, 23);

        datePickerDialog.show();

    }

    //시간 set
    public void showTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int time = (minute * 60 + hourOfDay * 60 * 60) * 1000;

                alarmHour=hourOfDay;
                alarmMinute=minute;
                timeString = Integer.toString(time- (540*60000)); // 왠지 모르지만 계속 9시간이 추가 되서 방이 생성돼서 수동으로 빼줌
                Log.v("time", timeString);

                //분이 한자리일 때 앞에 0 붙여주기
                String m = "";
                if (minute < 10) m = "0" + minute;
                else m += minute;

                TextView_Time.setText("시간 : " + hourOfDay + "시 " + m + "분");

            }
        }, 12, 00, true);

        timePickerDialog.show();
    }


    @Override
    public void onClick(View v) {
        String title = EditText_title.getText().toString();
        String count = EditText_count.getText().toString();
        String contents = EditText_contents.getText().toString();

        writeNewRoom("1", title, dateString, timeString, count, contents);
        setAlarm(); // 방 만듬과 동시에 알람 설정
        startActivity(new Intent(this, Alarm_Activity.class));
        finish();
    }

    private void writeNewRoom(String userId, String title, String date, String time,
                             String count, String contents) {

        String key = mDatabase.child("rooms").push().getKey();

        Room room = new Room(userId, title, date, time, count, contents);

        Map<String, Object> roomValues = room.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/rooms/" + key, roomValues);

        // childUpdates.put("/user-rooms/" + userId + "/" + key, roomValues);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NewRoomActivity.this, "글 작성 완료.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Write failed
                Toast.makeText(NewRoomActivity.this, "글 작성 실패. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setAlarm(){


        Calendar alarmCalendar= Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        alarmCalendar.set(Calendar.MINUTE,alarmMinute);
        alarmCalendar.set(Calendar.SECOND,0);
        Toast.makeText(NewRoomActivity.this, "알람이" + alarmHour + "시" + alarmMinute + "분 에 설정 됐습니다.",Toast.LENGTH_SHORT).show();
        //timePickerDialog 에서 설정한 시간을 알림으로 설정
        Log.e("TAG", "Still yet");
        if(alarmCalendar.before(Calendar.getInstance())) alarmCalendar.add(Calendar.DATE,1);
        //알람 시간이 현재시간보다 느 때 하루 뒤로 맞춤
        Intent alarmIntent = new Intent(getApplicationContext(),Alarm_Receiver.class);
        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent.setAction(Alarm_Receiver.ACTION_RESTART_SERVICE);
        PendingIntent alarmCallPendingIntent = PendingIntent.getBroadcast(NewRoomActivity.this,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("TAG", "위에");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Log.e("TAG", "아래거");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),alarmCallPendingIntent);
        }// 알람 설정

    }

}