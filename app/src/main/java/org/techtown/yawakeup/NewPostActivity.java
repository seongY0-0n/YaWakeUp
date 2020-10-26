package org.techtown.yawakeup;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText EditText_title, EditText_place, EditText_count, EditText_contents;
    private TextView TextView_Date, TextView_Time;

    private DatabaseReference mDatabase;

    private String dateString, timeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        EditText_title = findViewById(R.id.post_title_et);
        EditText_place = findViewById(R.id.post_place_et);
        EditText_count = findViewById(R.id.post_count_et);
        EditText_contents = findViewById(R.id.post_contents_et);

        TextView_Date = (TextView) findViewById(R.id.post_date_tv);
        TextView_Time = (TextView) findViewById(R.id.post_time_tv);

        Button post_date_btn = findViewById(R.id.post_date_btn);
        Button post_time_btn = findViewById(R.id.post_time_btn);
        Button post_save_btn = findViewById(R.id.post_save_btn);


        //날짜 버튼 리스너
        post_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        //시간 버튼 리스너
        post_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTime();
            }
        });

        post_save_btn.setOnClickListener(this);

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
        }, 2020, 8, 16);

        datePickerDialog.show();

    }

    //시간 set
    public void showTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int time = (view.getCurrentMinute() * 60 + view.getCurrentHour() * 60 * 60) * 1000;
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                timeString = format.format(time);

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
        String place = EditText_place.getText().toString();
        String count = EditText_count.getText().toString();
        String contents = EditText_contents.getText().toString();

        writeNewPost("1", title, dateString, timeString, place, count, contents);

        SharedPreferences alarmSp = this.getSharedPreferences("AlarmStatus", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = alarmSp.edit();
        edit.putInt("알람 설정 상태", 1); // 1 알람 설정, 0 알람 없음
        edit.commit();

        startActivity(new Intent(this, Share_Activity.class));
        finish();

    }

    private void writeNewPost(String userId, String title, String date, String time,
                              String place, String count, String contents) {

        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, title, date, time, place, count, contents);

        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        // childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(NewPostActivity.this, "글 작성 완료.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Write failed
                Toast.makeText(NewPostActivity.this, "글 작성 실패. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}