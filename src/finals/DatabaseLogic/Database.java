package finals.DatabaseLogic;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final Path storageRPath = Paths.get("src/finals/DatabaseLogic/HotelDatabase.txt");
    private final List<String[]> lines = new ArrayList<>();

    public List<String[]> getLines() {
        return lines;
    }
    
    // 1. Raw Read: Pulls everything fresh from the file into memory
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




    // 3. Raw Overwrite: Overwrites the database file entirely (used for updates)
    public void overwriteDatabase(List<String[]> updatedLines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageRPath.toFile(), false))) {
            for (String[] line : updatedLines) {
                writer.write(String.join("|", line) + "\n");
            }
        } catch (IOException e) {
            System.err.println("[Database Error] Failed to update file: " + e.getMessage());
        }
    }

 
    // create booking logic mostlyly uses this to generate the next booking ID and to next line to the database or text file
    public int getNextBookingId() {
        readAllLines();
        if (lines.isEmpty()) {
            return 1;
        }
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
    
 // You can place this method in your RoomAvailability class or Database class
    public void confirmBookingAndLockRoom(char[][] roomArray, int startRoomNum, int targetRoom) {
        int current = startRoomNum;
        
        for (int row = 0; row < roomArray.length; row++) {
            if (roomArray[row][0] == '■') continue; // Skip hidden rows
            
            for (int col = 0; col < roomArray[row].length; col++) {
                if (current == targetRoom) {
                    
                    roomArray[row][col] = 'X'; // <-- THE CASHIER LOCKS THE ROOM HERE!
                    return;
                }
                current++;
            }
        }
    }
    
}