package org.techtown.yawakeup;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public class Alarm_Service extends Service {
    String TAG= "TAG+Service";
    int cancelMode;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"AlarmService");


        //알람 해제 방법 불러오기
        SharedPreferences sp = getSharedPreferences("AlarmCancelMode",MODE_PRIVATE);
        cancelMode= sp.getInt("알람해제방법", 0);
        Intent alarmIntent;
        if(cancelMode==1){
            //수학 문제 풀기로
            alarmIntent = new Intent(getApplicationContext(), MathAlarm_Activity.class);
        } else{
            alarmIntent = new Intent(getApplicationContext(), ShakeAlarm_Activity.class);
        }

        PendingIntent pintent= PendingIntent.getActivity(getApplicationContext(),0, alarmIntent,0);

        //알림창에 알림
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(this,"first");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setContentTitle("알람 재생중");
        builder.setContentText("일어나!");
        builder.setContentIntent(pintent)
                .setAutoCancel(false);

        if(Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.O){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(new NotificationChannel("first","기본채널",NotificationManager.IMPORTANCE_DEFAULT));
        }
        startForeground(1, builder.build());

        startActivity(alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP));

        return super.onStartCommand(intent, flags, startId);
    }


}
