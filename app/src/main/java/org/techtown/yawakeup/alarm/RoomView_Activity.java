package org.techtown.yawakeup.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.techtown.yawakeup.Post;
import org.techtown.yawakeup.R;

import java.util.Calendar;
import java.util.List;

public class RoomView_Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle, mDate, mTime, mCount, mContents;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton edit, modify, delete;

    private Calendar alarmCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_view);

        mTitle = findViewById(R.id.room_view_title);
        //mDate = findViewById(R.id.room_view_date);
        mTime = findViewById(R.id.room_view_time);
        mCount = findViewById(R.id.room_view_count);
        mContents = findViewById(R.id.room_view_contents);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.post_fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.post_fab_close);

        edit = (FloatingActionButton) findViewById(R.id.room_edit_fad);
        modify = (FloatingActionButton) findViewById(R.id.room_modify_fad);
        delete = (FloatingActionButton) findViewById(R.id.room_delete_fad);

        edit.setOnClickListener(this);
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);


        Intent intent = getIntent();

        mTitle.setText(intent.getStringExtra("title"));
       // mDate.setText("날짜 : " + intent.getStringExtra("date"));
        mTime.setText("시간 : " + intent.getStringExtra("time"));
        mCount.setText("모집인원 : " + intent.getStringExtra("count"));
        mContents.setText("내용 : " + intent.getStringExtra("contents"));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        SharedPreferences sp = getSharedPreferences("AlarmStatus",MODE_PRIVATE);
        int alarmStatus= sp.getInt("알람 설정 상태", 0);

        switch (id) {
            case R.id.room_edit_fad:
                anim();
               // Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.room_modify_fad:
                anim();
                if(alarmStatus != 1){
                   setAlarm();
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("알람 설정 상태", 1); // 1 알람 설정, 0 알람 없음
                     edit.apply();
                   Toast.makeText(this, "알람방 참여 완료! 알람이 정해진 시간에 맞춰졌습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "이미 알람 방에 참여되어 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.room_delete_fad:
                anim();
                if(alarmStatus == 0 ){
                    Toast.makeText(this, "알람 방에 참여되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                } else if(alarmStatus == 1){
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("알람 설정 상태", 0); // 1 알람 설정, 0 알람 없음
                    Toast.makeText(this, "알람방 나오기 완료! 알람이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    edit.apply();
                    cancelAlarm();
                }

                //Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void anim() {

        if (isFabOpen) {
            edit.setImageResource(R.drawable.ic_add);
            delete.startAnimation(fab_close);
            modify.startAnimation(fab_close);
            delete.setClickable(false);
            modify.setClickable(false);
            isFabOpen = false;
        } else {
            edit.setImageResource(R.drawable.ic_close);
            delete.startAnimation(fab_open);
            modify.startAnimation(fab_open);
            delete.setClickable(true);
            modify.setClickable(true);
            isFabOpen = true;
        }
    }

    public static class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

        private List<Room> datas;

        public RoomAdapter(List<Room> datas) {
            this.datas = datas;
        }

        @NonNull
        @Override
        public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RoomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
            Room data = datas.get(position);
            holder.title.setText(data.getTitle());
            holder.time.setText("시간 : " + data.getTime());
            holder.count.setText("1/" + data.getCount());
        }

        public int getItemCount(){
            return datas.size();
        }

        class RoomViewHolder extends RecyclerView.ViewHolder{
            private TextView title;
            private TextView time;
            private TextView count;

            public RoomViewHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.item_room_title);
                time = itemView.findViewById(R.id.item_room_time);
                count = itemView.findViewById(R.id.item_room_count);
            }
        }


    }
    void setAlarm(){
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        int alarmHour = Integer.parseInt(time.substring(0,2));
        int alarmMinute = Integer.parseInt(time.substring(3,5));
        alarmCalendar= Calendar.getInstance();
        alarmCalendar.setTimeInMillis(System.currentTimeMillis());
        alarmCalendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        alarmCalendar.set(Calendar.MINUTE,alarmMinute);
        alarmCalendar.set(Calendar.SECOND,0);
        Toast.makeText(RoomView_Activity.this, "알람이" + alarmHour + "시" + alarmMinute + "분 에 설정 됐습니다.",Toast.LENGTH_SHORT).show();
        //timePickerDialog 에서 설정한 시간을 알림으로 설정
        if(alarmCalendar.before(Calendar.getInstance())) alarmCalendar.add(Calendar.DATE,1);
        //알람 시간이 현재시간보다 느 때 하루 뒤로 맞춤
        Intent alarmIntent = new Intent(getApplicationContext(),Alarm_Receiver.class);
        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent.setAction(Alarm_Receiver.ACTION_RESTART_SERVICE);
        PendingIntent alarmCallPendingIntent = PendingIntent.getBroadcast(RoomView_Activity.this,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Log.e("TAG", "위에");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), alarmCallPendingIntent);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
           // Log.e("TAG", "아래거");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(),alarmCallPendingIntent);
        }// 알람 설정

    }
    void cancelAlarm(){
        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(),Alarm_Receiver.class);
        alarmIntent.setAction(Alarm_Receiver.ACTION_RESTART_SERVICE);
        PendingIntent alarmCallPendingIntent = PendingIntent.getBroadcast(RoomView_Activity.this,0,alarmIntent,0);
        alarmManager.cancel(alarmCallPendingIntent);
        alarmCallPendingIntent.cancel();
    }


}