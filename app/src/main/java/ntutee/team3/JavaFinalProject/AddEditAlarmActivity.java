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

        Intent intent = getIntent();
        if (intent.hasExtra("hour")) {
            int hour = intent.getIntExtra("hour", 0);
            int minute = intent.getIntExtra("minute", 0);
            days = intent.getBooleanArrayExtra("days");

            timePicker.setHour(hour);
            timePicker.setMinute(minute);

            for (int i = 0; i < checkBoxes.length; i++) {
                checkBoxes[i].setChecked(days[i]);
            }
        }


        days = new boolean[7]; // 預設每週七天皆未選中

        saveButton.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            int isAnyDaySelected = 0;


            for (int i = 0; i < checkBoxes.length; i++) {
                days[i] = checkBoxes[i].isChecked();
                if(days[i] == true)
                    isAnyDaySelected++;
            }

            Calendar today = Calendar.getInstance();
            int today1 = today.get(Calendar.DAY_OF_WEEK);

            if (isAnyDaySelected==0){
                today1--;
                if (today1 >= 0 && today1 < 7) {
                    days[today1] = true;
                    //Toast.makeText(this, "今天是星期: " + days[today1], Toast.LENGTH_SHORT).show();
                }
            }

            //Toast.makeText(this, "今天是星期: " + days[today1], Toast.LENGTH_SHORT).show();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // 設置鬧鐘，只在選中的日子響鈴
            for (int i = 0; i < 7; i++) {
                if (days[i]) {
                    int dayOfWeek = i + 1; // Calendar.SUNDAY = 1, Calendar.MONDAY = 2, ...
                    calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                    String dayName = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"}[i];
                    Toast.makeText(this, dayName , Toast.LENGTH_SHORT).show();

                    // 設定 AlarmManager 觸發鬧鐘
                    setAlarm(AddEditAlarmActivity.this, calendar);
                }
            }



            Intent resultIntent = new Intent();
            resultIntent.putExtra("hour", hour);
            resultIntent.putExtra("minute", minute);
            resultIntent.putExtra("days", days);

            if (intent.hasExtra("position")) {
                resultIntent.putExtra("position", intent.getIntExtra("position", -1));
            }


            setResult(RESULT_OK, resultIntent);
            finish();
        });

    }
    private void setAlarm(Context context, Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);

        // 使用 FLAG_IMMUTABLE，因為 PendingIntent 不需要被修改
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

}