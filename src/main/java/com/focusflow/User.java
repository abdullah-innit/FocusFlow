package com.focusflow;

public class User {
    private String name;
    private int focusPoints;
    private int totalSessions;

    public User(String name, int focusPoints, int totalSessions) {
        this.name = name;
        this.focusPoints = focusPoints;
        this.totalSessions = totalSessions;
    }

    public String getName() { return name; }
    public int getFocusPoints() { return focusPoints; }
    public int getTotalSessions() { return totalSessions; }

    public void addPoints(int points) {
        this.focusPoints += points;
        this.totalSessions++;
    }

    @Override
    public String toString() {
        return name + "," + focusPoints + "," + totalSessions;
    }
}