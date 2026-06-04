package finals.Cashier.Logic;

import java.util.List;
import finals.DatabaseLogic.Database;

public class CashierFunctions extends Database{
    
    public void updateBookingStatus(String bookingId, String newStatus) {
        Database db = new Database();
        
        // 1. Pull fresh data from the text file into memory
        List<String[]> lines = db.readAllLines();
        boolean updated = false;
        
        // 2. Find the row and modify the status column (index 10)
        for (String[] line : lines) {
            if (line.length > 10 && line[0].trim().equals(bookingId.trim())) {
                line[10] = newStatus; 
                updated = true;
                break;
            }
        }

        // 3. Hand the updated data list back to the Database class to overwrite the file
        if (updated) {
            db.overwriteDatabase(lines);
            System.out.println("[Success] Booking status updated to " + newStatus);
        } else {
            System.out.println("[Notice] Booking ID not found.");
        }
    }
}