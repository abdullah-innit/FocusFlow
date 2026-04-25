package com.focusflow;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class SecondaryController {

    @FXML private ListView<String> historyList;
    @FXML private Label totalSessionsLabel;
    @FXML private Label totalTimeLabel;

    @FXML
    public void initialize() {
        List<Session> sessions = FileStorageService.loadSessions();
        historyList.getItems().clear();

        if (sessions.isEmpty()) {
            historyList.getItems().add("No sessions yet. Start focusing!");
        } else {
            for (Session s : sessions) {
                historyList.getItems().add(s.toString());
            }
        }

        int total = sessions.size();
        int totalMins = sessions.stream().mapToInt(Session::getDurationMinutes).sum();
        totalSessionsLabel.setText("Total Sessions: " + total);
        totalTimeLabel.setText("Total Focus Time: " + totalMins + " mins");
    }

    @FXML
    private void switchToTimer() throws IOException {
        App.setRoot("primary");
    }
}