package finals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HiSirTacckychan {

    public static void main(String[] args) {
        String filePath = "bookings.txt";
        
        // The List acting as our dynamic 2D array
        List<String[]> bookingDatabase = new ArrayList<>();

        // 1. Read the text file and populate the array
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                // Split the line by the pipe character, keeping empty columns
                String[] columns = line.split("\\|", -1);
                
                // Add this row's array to our main database list
                bookingDatabase.add(columns);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading the file: " + e.getMessage());
            return; 
        }

        // 2. Show the array data in the console
        System.out.println("=== HOTEL BOOKINGS DATABASE ===");
        System.out.println("Total Bookings Found: " + bookingDatabase.size() + "\n");

        for (String[] row : bookingDatabase) {
            // Mapping the array indexes to your expanded text file columns
            String bookingNumber = row[0];
            String dateIn        = row[1];
            String dateOut       = row[2];
            String roomType      = row[3];
            String adultNames    = row[4];
            String childNames    = row[5];
            String totalGuests   = row[6];
            String swimPasses    = row[7];
            String buffetPasses  = row[8]; 
            

            // Handling the case where there are no children so it doesn't look blank
            if (childNames.trim().isEmpty()) {
                childNames = "None";
            }

            // Printing the formatted output to the console
            System.out.println("Booking # " + bookingNumber);
            System.out.println("---------------------------------");
            System.out.println("Check-in:      " + dateIn);
            System.out.println("Check-out:     " + dateOut);
            System.out.println("Room Type:     " + roomType);
            System.out.println("Adults:        " + adultNames);
            System.out.println("Children:      " + childNames);
            System.out.println("Total Count:   " + totalGuests);
            System.out.println("Swim Passes:   " + swimPasses);
            System.out.println("Buffet Passes: " + buffetPasses);
            System.out.println("=================================\n");
        }
    }
}