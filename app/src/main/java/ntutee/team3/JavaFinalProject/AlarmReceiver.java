package ntutee.team3.JavaFinalProject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import androidx.core.app.NotificationCompat;


import com.unity3d.player.UnityPlayer;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "ALARM_NOTIFICATION_CHANNEL";
    public static MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 播放鬧鐘聲音
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
            mediaPlayer.setLooping(true); // 設置鬧鐘聲音循環播放
            mediaPlayer.start();
        }
        UnityAnimationController.AlarmRinning = true;
        UnityAnimationController.ReplayAnimation();

        // 創建靜音通知頻道
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Notifications",
                    NotificationManager.IMPORTANCE_LOW // 靜音並降低優先級
            );

            channel.setSound(null, null);
            channel.enableVibration(false);
            channel.enableLights(false);
            notificationManager.createNotificationChannel(channel);
        }

        // Intent 來處理停止鬧鐘聲音的行為
        Intent stopIntent = new Intent(context, stopAlarmReceiver.class);
        stopIntent.putExtra("notification_id", 1); // 傳遞通知 ID，方便取消

        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                stopIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 建立靜音通知
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm) // 替換為你的圖標資源
                .setContentTitle("Alarm Ringing")
                .setContentText("Tap to stop the alarm sound")
                .setPriority(NotificationCompat.PRIORITY_LOW) // 設置通知優先級
                .setAutoCancel(true)
                .addAction(R.drawable.ic_cancel, "Stop Alarm Sound", stopPendingIntent);

        // 發送通知
        notificationManager.notify(1, notificationBuilder.build());
    }


}
