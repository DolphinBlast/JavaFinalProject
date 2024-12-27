package ntutee.team3.JavaFinalProject;

public class Alarm {
    private int hour;
    private int minute;
    private boolean[] days; // 一周的天數

    public Alarm(int hour, int minute, boolean[] days) {
        this.hour = hour;
        this.minute = minute;
        this.days = days;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setDays(boolean[] days) {
        if (days != null && days.length == 7) {
            this.days = days.clone();
        }
    }


    public String getFormattedDays() {
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        StringBuilder formattedDays = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (days[i]) {
                if (formattedDays.length() > 0) {
                    formattedDays.append(", ");
                }
                formattedDays.append(dayNames[i]);
            }
        }
        return formattedDays.toString();
    }
}