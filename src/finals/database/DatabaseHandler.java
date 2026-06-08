package finals.database;

import finals.core.config.ProgramConstants;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler implements ProgramConstants {
    private final Path roomStoragePath = Paths.get(ROOM_DB_PATH);
    private final Path storageRPath = Paths.get(HOTEL_DB_PATH);
    private final List<String[]> lines = new ArrayList<>();

    public List<String[]> getLines() {
        return lines;
    }
    
    public List<String[]> readAllLines() {
        lines.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(storageRPath.toFile()))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (!currentLine.trim().isEmpty()) {
                    lines.add(currentLine.split("\\|", -1));
                }
            }
        } catch (IOException e) {
            System.err.println("[Database Error] Failed to read lines: " + e.getMessage());
        }
        return lines;
    }

    public void overwriteDatabase(List<String[]> updatedLines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageRPath.toFile(), false))) {
            for (String[] line : updatedLines) {
                writer.write(String.join("|", line) + "\n");
            }
        } catch (IOException e) {
            System.err.println("[Database Error] Failed to update file: " + e.getMessage());
        }
    }

    public int getNextBookingId() {
        readAllLines();
        if (lines.isEmpty()) return 1;
        try {
            String lastIdStr = lines.get(lines.size() - 1)[0].trim();
            return Integer.parseInt(lastIdStr) + 1;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
    
    public void appendLine(String formattedRecord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageRPath.toFile(), true))) {
            writer.write(formattedRecord + "\n");
        } catch (IOException e) {
            System.err.println("[Database Error] Failed to write line: " + e.getMessage());
        }
    }   

    public List<String[]> readRoomDatabase() {
        List<String[]> roomLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(roomStoragePath.toFile()))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                if (!currentLine.trim().isEmpty()) {
                    roomLines.add(currentLine.split("\\|", -1));
                }
            }
        } catch (IOException e) {
            System.err.println("[Database Error] Failed to read Room Database: " + e.getMessage());
        }
        return roomLines;
    }

    public void confirmBookingAndLockRoom(String[][] roomArray, int startRoomNum, int targetRoom, String status) {
        int current = startRoomNum;
        for (int row = 0; row < roomArray.length; row++) {
            if (roomArray[row][0].equals("■")) continue;
            for (int col = 0; col < roomArray[row].length; col++) {
                if (current == targetRoom) {
                    roomArray[row][col] = status;
                    return;
                }
                current++;
            }
        }
    }
}