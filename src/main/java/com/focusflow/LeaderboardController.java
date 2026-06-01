package com.focusflow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class LeaderboardController {

    @FXML private ListView<String> leaderboardList;
    @FXML private Label yourRankLabel;
    @FXML private Text titleText;

    private static final String LEADERBOARD_FILE = "leaderboard.txt";

    @FXML
    public void initialize() {
        loadLeaderboard();
    }

    private void loadLeaderboard() {
        List<User> users = readUsersFromFile();

        // Sort by focus points highest first
        users.sort((a, b) -> b.getFocusPoints() - a.getFocusPoints());

        leaderboardList.getItems().clear();

        if (users.isEmpty()) {
            leaderboardList.getItems().add("No users yet. Complete a session first!");
            return;
        }

        int rank = 1;
        for (User user : users) {
            String entry = rank + ".  " + user.getName()
                    + "     |  " + user.getFocusPoints() + " pts"
                    + "  |  " + user.getTotalSessions() + " sessions";
            leaderboardList.getItems().add(entry);
            rank++;
        }
    }

    public static void saveUserPoints(String name, int pointsToAdd) {
        List<User> users = readUsersFromFile();

        boolean found = false;
        for (User user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                user.addPoints(pointsToAdd);
                found = true;
                break;
            }
        }

        if (!found) {
            User newUser = new User(name, pointsToAdd, 1);
            users.add(newUser);
        }

        writeUsersToFile(users);
    }

    private static List<User> readUsersFromFile() {
        List<User> users = new ArrayList<>();
        File file = new File(LEADERBOARD_FILE);
        if (!file.exists()) return users;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String name = parts[0];
                        int points = Integer.parseInt(parts[1]);
                        int sessions = Integer.parseInt(parts[2]);
                        users.add(new User(name, points, sessions));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading leaderboard: " + e.getMessage());
        }
        return users;
    }

    private static void writeUsersToFile(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LEADERBOARD_FILE))) {
            for (User user : users) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing leaderboard: " + e.getMessage());
        }
    }

    @FXML
    private void goBack() throws IOException {
        App.setRoot("primary");
    }
}