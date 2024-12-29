package ntutee.team3.JavaFinalProject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditAlarmActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private boolean[] days;



    public int generateRequestCode(int dayOfWeek, int hour, int minute) {
        return dayOfWeek * 100 + hour * 10 + minute;
    }


    private void cancelAlarm(Context context, int requestCode) {
        // 1. 獲取 AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 2. 準備對應的 PendingIntent
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 3. 使用 AlarmManager 取消鬧鐘
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to cancel alarm: AlarmManager unavailable", Toast.LENGTH_SHORT).show();
        }
    }


    private void setAlarm(Context context, Calendar calendar, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        alarmIntent.putExtra("requestCode", requestCode);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (alarmManager != null) {
            // 若時間已過，將時間推遲到下一週
            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

            // 使用 setExactAndAllowWhileIdle 確保在低功耗模式下觸發
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_alarm);

        timePicker = findViewById(R.id.time_picker);
        Button saveButton = findViewById(R.id.button_save);

        CheckBox[] checkBoxes = new CheckBox[]{
                findViewById(R.id.checkbox_sunday),
                findViewById(R.id.checkbox_monday),
                findViewById(R.id.checkbox_tuesday),
                findViewById(R.id.checkbox_wednesday),
                findViewById(R.id.checkbox_thursday),
                findViewById(R.id.checkbox_friday),
                findViewById(R.id.checkbox_saturday)
        };



        saveButton.setOnClickListener(v -> {

            days = new boolean[7];// 預設每週七天皆未選中

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            int oldRequestCode = getIntent().getIntExtra("requestCode", -1);

            if (oldRequestCode != -1) {
                cancelAlarm(AddEditAlarmActivity.this, oldRequestCode);
                Toast.makeText(this, "old"+oldRequestCode, Toast.LENGTH_SHORT).show();
            }

            int isAnyDaySelected = 0;

            for (int i = 0; i < checkBoxes.length; i++) {
                days[i] = checkBoxes[i].isChecked();
                if (days[i]) {
                    isAnyDaySelected++;
                }
            }

            // 如果未選中任何日期，默認為當天
            if (isAnyDaySelected == 0) {
                int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1; // 星期從0開始
                if (today >= 0 && today < 7) {
                    days[today] = true;
                }
            }

            // 設置鬧鐘，只在選中的日子響鈴
            for (int i = 0; i < 7; i++) {
                if (days[i]) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    int dayOfWeek = i + 1; // Calendar.SUNDAY = 1, Calendar.MONDAY = 2, ...
                    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);



                    // 使用唯一的 requestCode 區分不同的 PendingIntent
                    int requestCode = generateRequestCode(dayOfWeek, hour, minute);
                    setAlarm(AddEditAlarmActivity.this, calendar, requestCode);
                    Toast.makeText(this, "set"+requestCode, Toast.LENGTH_SHORT).show();


                    String dayName = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}[i];
                    Toast.makeText(this, "Alarm set for " + dayName, Toast.LENGTH_SHORT).show();
                }
            }

            int dayOfWeek = -1; // 初始化為一個無效值（例如 -1）

            for (int i = 0; i < 7; i++) {
                if (days[i]) {
                    dayOfWeek = i + 1;
                    break; // 找到第一個匹配的天數後即可退出迴圈
                }
            }

            if (dayOfWeek == -1) {
                throw new IllegalStateException("No day selected for the alarm");
            }


            int newRequestCode = generateRequestCode(dayOfWeek, hour, minute);



            // 返回設置結果
            Intent resultIntent = new Intent();
            resultIntent.putExtra("hour", hour);
            resultIntent.putExtra("minute", minute);
            resultIntent.putExtra("days", days);
            resultIntent.putExtra("requestCode", newRequestCode);


            if (getIntent().hasExtra("position")) {
                resultIntent.putExtra("position", getIntent().getIntExtra("position", -1));
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }


}