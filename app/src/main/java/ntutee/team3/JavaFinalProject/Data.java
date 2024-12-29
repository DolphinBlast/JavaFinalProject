package ntutee.team3.JavaFinalProject;


public class Data
{
     String success; // 是否成功
     Result result;  // 結果
     Records records; // 天氣記錄

    public static class Result
    {
        String resource_id; // 資源ID
        Field[] fields;     // 欄位描述

        public static class Field
        {
            String id;   // 欄位ID
            String type; // 欄位類型
        }
    }

    public static class Records
    {
        String datasetDescription; // 資料集描述
        Location[] location;       // 地點陣列

        public static class Location
        {
            String locationName;         // 地點名稱
            WeatherElement[] weatherElement; // 天氣元素陣列

            public static class WeatherElement
            {
                String elementName;   // 元素名稱 (Wx, PoP, MinT, MaxT, CI 等)
                Time[] time;          // 時間段陣列

                public static class Time
                {
                    String startTime;        // 開始時間
                    String endTime;          // 結束時間
                    Parameter parameter;     // 參數資料

                    public static class Parameter
                    {
                        String parameterName;  // 參數名稱 (陰天、多雲等)
                        String parameterValue; // 參數值 (可選)
                        String parameterUnit;
                    }// 參數單位 (可選)
                }
            }
        }
    }
}
