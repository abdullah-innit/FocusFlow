package com.focusflow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileStorageService {

    private static final String FILE_PATH = "sessions.txt";

    public static void saveSession(Session session) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(session.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving session: " + e.getMessage());
        }
    }

    public static List<Session> loadSessions() {
        List<Session> sessions = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return sessions;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(" \\| ");
                    if (parts.length == 3) {
                        String dateTime = parts[0];
                        int duration = Integer.parseInt(parts[1].replace(" mins", "").trim());
                        String task = parts[2];
                        sessions.add(new Session(task, duration, dateTime));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading sessions: " + e.getMessage());
        }
        return sessions;
    }
}