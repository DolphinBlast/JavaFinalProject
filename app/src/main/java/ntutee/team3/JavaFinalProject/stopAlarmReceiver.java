package ntutee.team3.JavaFinalProject;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class stopAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 停止鬧鐘聲音
        if (AlarmReceiver.mediaPlayer != null && AlarmReceiver.mediaPlayer.isPlaying()) {
            AlarmReceiver.mediaPlayer.stop();
            AlarmReceiver.mediaPlayer.release();
            AlarmReceiver.mediaPlayer = null; // 確保釋放資源
        }

        // 取消通知
        int notificationId = intent.getIntExtra("notification_id", -1);
        if (notificationId != -1) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(notificationId);
        }

        // 顯示提示訊息
        Toast.makeText(context, "Alarm Sound Stopped", Toast.LENGTH_SHORT).show();
    }
}
