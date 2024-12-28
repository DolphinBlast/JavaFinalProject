package ntutee.team3.JavaFinalProject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private final ArrayList<Alarm> alarmList;
    private final Context context;
    private final AlarmManager alarmManager;

    public AlarmAdapter(ArrayList<Alarm> alarmList, Context context, AlarmManager alarmManager) {
        this.alarmList = alarmList;
        this.context = context;
        this.alarmManager = alarmManager;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.timeTextView.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));
        holder.daysTextView.setText(alarm.getFormattedDays());

        holder.editButton.setOnClickListener(v -> {
            ((MainActivity) context).editAlarm(position, alarm);
        });

        holder.deleteButton.setOnClickListener(v -> {
            cancelAlarm(alarm);

            alarmList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, daysTextView;
        Button editButton, deleteButton;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.text_time);
            daysTextView = itemView.findViewById(R.id.text_days);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
    private void cancelAlarm(Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);

        // 使用與設置鬧鐘相同的 requestCode 計算邏輯
        for (int i = 0; i < alarm.getDays().length; i++) {
            if (alarm.getDays()[i]) {
                int dayOfWeek = i + 1; // Calendar.SUNDAY = 1, Calendar.MONDAY = 2, ...
                int requestCode = dayOfWeek * 100 + alarm.getHour() * 10 + alarm.getMinute();

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                // 取消鬧鐘
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
    }

}