package ntutee.team3.JavaFinalProject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class weather extends AppCompatActivity
{
    int selectedCity_index = -1;
    int selectedTime_index = -1;
    public TextView city_text,time_text,info_text;
    public Data data;
    public StringBuilder displayText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        String URL = "https://opendata.cwa.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWA-5A6556A3-BD4C-48AC-8F7C-1CDF34213863&format=JSON&locationName=&elementName=";
        Request request = new Request.Builder().url(URL).build();
        OkHttpClient okhttpClient = new OkHttpClient();//OkHttpClient okhttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        okhttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
            {
                if (response.code() == 200)
                {
                    if (response.body() == null) return;
                    data = new Gson().fromJson(response.body().string(), Data.class);
                    displayText = new StringBuilder();
                }
                else if (!response.isSuccessful())
                    Log.e("伺服器錯誤", response.code() + " " + response.message());
                else
                    Log.e("其他錯誤", response.code() + " " + response.message());
            }
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e)
            {
                if (e.getMessage() != null)
                    Log.e("查詢失敗", e.getMessage());
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        city_text = findViewById(R.id.city);
        time_text = findViewById(R.id.time);
        info_text = findViewById(R.id.tx);

        findViewById(R.id.change_city).setOnClickListener(view ->
        {
            String[] city_options = new String[data.records.location.length];
            for(int i = 0 ; i< data.records.location.length;i++)
                city_options[i]=data.records.location[i].locationName;
            AlertDialog.Builder builder = new AlertDialog.Builder(weather.this);
            builder.setTitle("選擇一個縣市");
            builder.setItems(city_options, (dialog, which) ->
            {
                selectedCity_index = Arrays.asList(city_options).indexOf(city_options[which]);
                Toast.makeText(weather.this, "你選擇了：" + city_options[which], Toast.LENGTH_SHORT).show();
            });
            builder.create().show();
        });

        findViewById(R.id.change_time).setOnClickListener(view ->
        {
            String[] time_options = new String[data.records.location[0].weatherElement[0].time.length];
            for(int i = 0 ; i< data.records.location[0].weatherElement[0].time.length;i++)
                time_options[i]=data.records.location[0].weatherElement[0].time[i].startTime.toString()+" - "
                        +data.records.location[0].weatherElement[0].time[i].endTime.toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(weather.this);
            builder.setTitle("選擇預測時間");
            builder.setItems(time_options, (dialog, which) ->
            {
                selectedTime_index = Arrays.asList(time_options).indexOf(time_options[which]);
                Toast.makeText(weather.this, "你選擇了：\n" + time_options[which], Toast.LENGTH_SHORT).show();
            });
            builder.create().show();
        });

        findViewById(R.id.get_weather).setOnClickListener(v ->
        {
            okhttpClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException
                {
                    if (response.code() == 200)
                    {
                        if (response.body() == null) return;
                        data = new Gson().fromJson(response.body().string(), Data.class);
                        displayText = new StringBuilder();
                    }
                    else if (!response.isSuccessful())
                        Log.e("伺服器錯誤", response.code() + " " + response.message());
                    else
                        Log.e("其他錯誤", response.code() + " " + response.message());
                }
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e)
                {
                    if (e.getMessage() != null)
                        Log.e("查詢失敗", e.getMessage());
                }
            });

            runOnUiThread(() ->
            {
                if (selectedCity_index == -1)
                    Toast.makeText(weather.this, "請先選擇一個縣市", Toast.LENGTH_SHORT).show();
                else if(selectedTime_index == -1)
                    Toast.makeText(weather.this, "請先選擇一個時段", Toast.LENGTH_SHORT).show();
                else
                {
                    city_text.setText(data.records.location[selectedCity_index].locationName);
                    time_text.setText(data.records.location[selectedCity_index].weatherElement[0].time[selectedTime_index].startTime
                            +"\n|\n"+data.records.location[selectedCity_index].weatherElement[0].time[selectedTime_index].endTime);
                    info_text.setText(
                                    "天氣現象(Wx) : "+data.records.location[selectedCity_index].weatherElement[0].time[selectedTime_index].parameter.parameterName+
                                    "\n降雨機率(PoP) : "+data.records.location[selectedCity_index].weatherElement[1].time[selectedTime_index].parameter.parameterName+
                                    "%\n最低氣溫(MinT) : "+data.records.location[selectedCity_index].weatherElement[2].time[selectedTime_index].parameter.parameterName+
                                    "℃\n最高氣溫(MaxT) : "+data.records.location[selectedCity_index].weatherElement[4].time[selectedTime_index].parameter.parameterName+
                                    "℃\n舒適度(CI) : "+data.records.location[selectedCity_index].weatherElement[3].time[selectedTime_index].parameter.parameterName);
                }
            });
        });
    }
}