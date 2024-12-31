package ntutee.team3.JavaFinalProject;

import android.app.AlarmManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;



public class MainActivity extends UnityPlayerActivity {
    private RecyclerView recyclerView;
    private AlarmAdapter adapter;
    private ArrayList<Alarm> alarmList;
    private AlarmManager alarmManager;
    private ActivityResultLauncher<Intent> alarmLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.unityView);
        linearLayout.addView(mUnityPlayer);

        recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab_alarm = findViewById(R.id.fab_add_alarm);
        FloatingActionButton fab_cloud = findViewById(R.id.fab_cloud_weather);

        alarmList = new ArrayList<>();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        adapter = new AlarmAdapter(alarmList, this, alarmManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        UnityAnimationController.SetRandomAnimation();

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
        fab_alarm.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditAlarmActivity.class);
            alarmLauncher.launch(intent);
        });

        fab_cloud.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
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
