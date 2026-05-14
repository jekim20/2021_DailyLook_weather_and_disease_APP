package com.example.swipeex;

import android.annotation. SuppressLint ;
import android.app.Notification ;
import android.app.NotificationChannel ;
import android.app.NotificationManager ;
import android.app.PendingIntent;
import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;
import android.util.Log ;

import androidx.core.app.NotificationCompat;

public class RebootActivity extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    boolean connected = true;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "ON 알림", importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        assert notificationManager != null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, default_notification_channel_id);
        Intent notificationIntent = new Intent(context.getApplicationContext(), LoginActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("도씨의 데일리룩");
        String action = intent.getAction();
        Log.e("REBOOT", action);
        assert action != null;
        builder.setContentText("저를 클릭해 알림 설정 하시고 오늘의 날씨와 데일리룩을 확인하세요!")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        if (connected) {
            notificationManager.notify(1, notification);
            connected = false;
        } else {
            notificationManager.cancel(1);
            connected = true;
        }
    }
}
