package com.focusflow;

public class Session {
    private String taskName;
    private int durationMinutes;
    private String dateTime;

    public Session(String taskName, int durationMinutes, String dateTime) {
        this.taskName = taskName;
        this.durationMinutes = durationMinutes;
        this.dateTime = dateTime;
    }

    public String getTaskName() { return taskName; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getDateTime() { return dateTime; }

    @Override
    public String toString() {
        return dateTime + " | " + durationMinutes + " mins | " + taskName;
    }
}