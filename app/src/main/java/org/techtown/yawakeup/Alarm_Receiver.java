package org.techtown.yawakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;



//알람 리시버, 알람 울리는걸 확인 하고 service 실행

public class Alarm_Receiver extends BroadcastReceiver {
    public static final String ACTION_RESTART_SERVICE = "Restart";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_RESTART_SERVICE)){
            Log.e("TAG", "fㅣ시");
            Intent in =new Intent(context, Alarm_Service.class);
            if(Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.O){
                context.startForegroundService(in);
            } else {
                context.startService(in);
            }
        }
    }
}
