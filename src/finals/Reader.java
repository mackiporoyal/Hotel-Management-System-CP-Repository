package finals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    public static void main(String[] args) {
        String filePath = "HotelDatabase.txt";
        
        List<String[]> bookingDatabase = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] columns = line.split("\\|", -1);
                
                bookingDatabase.add(columns);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading the file: " + e.getMessage());
            return; 
        }

        System.out.println("=== HOTEL BOOKINGS DATABASE ===");
        System.out.println("Total Bookings Found: " + bookingDatabase.size() + "\n");

        for (String[] row : bookingDatabase) {
            String bookingNumber = row[0];
            String dateIn        = row[1];
            String dateOut       = row[2];
            String roomType      = row[3];
            String adultNames    = row[4];
            String childNames    = row[5];
            String totalAdult    = row[6];
            String totalChild    = row[7];
            String swimPasses    = row[8]; 
            String buffetPasses  = row[9];
            

            if (childNames.isEmpty()) {
                childNames = "None";
            }

            System.out.println("Booking # " + bookingNumber);
            System.out.println("---------------------------------");
            System.out.println("Check-in:      " + dateIn);
            System.out.println("Check-out:     " + dateOut);
            System.out.println("Room Type:     " + roomType);
            System.out.println("Adults:        " + adultNames);
            System.out.println("Children:      " + childNames);
            System.out.println("Total Adult:   " + totalAdult);
            System.out.println("Total Child:   " + totalChild);
            System.out.println("Swim Passes:   " + swimPasses);
            System.out.println("Buffet Passes: " + buffetPasses);
            System.out.println("=================================\n");
        }
    }
}