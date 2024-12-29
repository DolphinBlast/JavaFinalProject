package ntutee.team3.JavaFinalProject;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
    private ArrayList<Alarm> alarmList;
    private AlarmManager alarmManager;
    private ActivityResultLauncher<Intent> alarmLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab_add_alarm);

        alarmList = new ArrayList<>();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        adapter = new AlarmAdapter(alarmList, this, alarmManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 Activity Result API
        alarmLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        int hour = data.getIntExtra("hour", 0);
                        int minute = data.getIntExtra("minute", 0);
                        boolean[] days = data.getBooleanArrayExtra("days");
                        int position = data.getIntExtra("position", -1);
                        int requestCode = data.getIntExtra("requestCode", 0);

                        if (position == -1) { // 新增鬧鐘

                            Alarm alarm = new Alarm(requestCode, hour, minute, days);
                            alarmList.add(alarm);
                            adapter.notifyItemInserted(alarmList.size() - 1);
                            Toast.makeText(this, "Alarm Added", Toast.LENGTH_SHORT).show();
                        } else { // 編輯鬧鐘
                            Alarm alarm = alarmList.get(position);
                            alarm.setHour(hour);
                            alarm.setMinute(minute);
                            alarm.setDays(days);
                            adapter.notifyItemChanged(position);
                            Toast.makeText(this, "Alarm Edited", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // 設置浮動按鈕點擊事件
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditAlarmActivity.class);
            alarmLauncher.launch(intent);
        });
    }

    public void editAlarm(int position, Alarm alarm) {
        Intent intent = new Intent(this, AddEditAlarmActivity.class);
        intent.putExtra("hour", alarm.getHour());
        intent.putExtra("minute", alarm.getMinute());
        intent.putExtra("days", alarm.getDays());
        intent.putExtra("position", position);
        intent.putExtra("requestCode", alarm.getRequestCode()); // 傳遞舊的 requestCode
        alarmLauncher.launch(intent);
    }
}
