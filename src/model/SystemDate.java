package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SystemDate implements Serializable {
    private LocalDateTime hourOpen;

    public int getCurrenHour() {
        return caculatorMinute();
    }

    public String getHourOpenString() {
        return hourOpen.getHour() + " : " + hourOpen.getMinute();
    }

    public void setHourOpen(LocalDateTime hourOpen) {
        this.hourOpen = hourOpen;
    }

    private int caculatorMinute() {
        var currentHour = LocalDateTime.now();
        int days = currentHour.getDayOfMonth() - hourOpen.getDayOfMonth();
        int hour = currentHour.getHour() - hourOpen.getHour();
        int minute = currentHour.getMinute() - hourOpen.getMinute();
        return (days * 24 * 60) + (hour * 60) + minute;
    }
}
