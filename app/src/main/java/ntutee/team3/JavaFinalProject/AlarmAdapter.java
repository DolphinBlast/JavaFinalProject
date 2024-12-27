package ntutee.team3.JavaFinalProject;

import android.app.AlarmManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
            alarmList.remove(position);
            notifyItemRemoved(position);
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
}