package com.focusflow;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrimaryController {

    @FXML private Text timerText;
    @FXML private TextField taskInput;
    @FXML private Button startBtn;
    @FXML private Button resetBtn;
    @FXML private Label statusLabel;
    @FXML private Label sessionCountLabel;
    @FXML private ProgressBar progressBar;

    private Timeline timeline;
    private int totalSeconds = 25 * 60;
    private int remainingSeconds = totalSeconds;
    private boolean isRunning = false;
    private boolean isWorkSession = true;
    private int sessionCount = 0;

    private static final int WORK_MINUTES = 25;
    private static final int SHORT_BREAK = 5;

    @FXML
    public void initialize() {
        updateTimerDisplay();
        progressBar.setProgress(1.0);
        sessionCountLabel.setText("Sessions Today: 0");
        statusLabel.setText("Ready to Focus");
    }

    @FXML
    private void handleStartPause() {
        if (isRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        if (taskInput.getText().trim().isEmpty()) {
            statusLabel.setText("⚠ Please enter a task first!");
            return;
        }
        isRunning = true;
        startBtn.setText("⏸ Pause");
        statusLabel.setText(isWorkSession ? "🔥 Focusing..." : "☕ On Break...");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            remainingSeconds--;
            updateTimerDisplay();
            double progress = (double) remainingSeconds / totalSeconds;
            progressBar.setProgress(progress);

            if (remainingSeconds <= 0) {
                timeline.stop();
                handleSessionComplete();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void pauseTimer() {
        isRunning = false;
        timeline.pause();
        startBtn.setText("▶ Resume");
        statusLabel.setText("⏸ Paused");
    }

    private void handleSessionComplete() {
        isRunning = false;
        startBtn.setText("▶ Start");

        if (isWorkSession) {
            sessionCount++;
            sessionCountLabel.setText("Sessions Today: " + sessionCount);

            // Save session to file
            String task = taskInput.getText().trim();
            String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Session session = new Session(task, WORK_MINUTES, time);
            FileStorageService.saveSession(session);

            statusLabel.setText("✅ Session Complete! Take a break.");
            isWorkSession = false;
            totalSeconds = SHORT_BREAK * 60;
        } else {
            statusLabel.setText("Break over! Ready to focus?");
            isWorkSession = true;
            totalSeconds = WORK_MINUTES * 60;
        }

        remainingSeconds = totalSeconds;
        updateTimerDisplay();
        progressBar.setProgress(1.0);
    }

    @FXML
    private void handleReset() {
        if (timeline != null) timeline.stop();
        isRunning = false;
        isWorkSession = true;
        totalSeconds = WORK_MINUTES * 60;
        remainingSeconds = totalSeconds;
        startBtn.setText("▶ Start");
        statusLabel.setText("Ready to Focus");
        progressBar.setProgress(1.0);
        updateTimerDisplay();
    }

    @FXML
    private void switchToHistory() throws IOException {
        App.setRoot("secondary");
    }

    private void updateTimerDisplay() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }
}