package finals.features.booking;

import java.util.List;
import finals.database.DatabaseHandler;

public class BookingManager extends DatabaseHandler {
    
    public void createBooking(String timeIn, String timeOut, String roomType, int roomNumber, 
                              List<String> adultNames, List<String> childNames, 
                              int totalAdult, int totalChild, int swimPasses, int buffetPasses) {
        
        int nextId = this.getNextBookingId();
        String paddedId = String.format("%05d", nextId);
        String joinedAdults = String.join(", ", adultNames);
        String joinedChildren = childNames.isEmpty() ? "none" : String.join(", ", childNames);
        
        String paymentStatus = "UNPAID"; 
        String bookingStatus = "ACTIVE"; 
        
        String databaseRow = String.format("%s|%s|%s|%s|%d|%s|%s|%d|%d|%d|%d|%s|%s",
                paddedId, timeIn, timeOut, roomType, roomNumber, joinedAdults, joinedChildren,
                totalAdult, totalChild, swimPasses, buffetPasses, paymentStatus, bookingStatus);

        this.appendLine(databaseRow);

        System.out.println("\t\t\t\t===================================================");
        System.out.println("\t\t\t\t            BOOKING SUCCESSFULLY CREATED           ");
        System.out.println("\t\t\t\t===================================================");
    }

    public void manageBooking(int searchType, String input) {
        List<String[]> currentRecords = this.readAllLines();
        boolean found = false;

        for (String[] row : currentRecords) {
            if (row.length < 13) continue; 

            boolean match = false;
            if (searchType == 1) match = true;
            else if (searchType == 2 && row[0].trim().equalsIgnoreCase(input.trim())) match = true;
            else if (searchType == 3 && row[5].toLowerCase().contains(input.toLowerCase())) match = true;

            if (match) {
                displayBookingCard(row);
                found = true;
            }
        }
        if (!found) System.out.println("\t\t\t\t No matches found for: " + input);
    }

    private void displayBookingCard(String[] column) {
        System.out.printf("| %20s", column[0] + " | " + column[1] + " to " + column[2] + " | Room " + column[4] + " | " + column[5] + (column[6].equalsIgnoreCase("none") ? "" : ", " + column[6]) + " | Status: " + column[12] + "\n" );
        System.out.println("\t\t\t\t--------------------------------------------------");
        System.out.println("\t\t\t\t Booking ID: " + column[0]);
        System.out.println("\t\t\t\t Check-in:   " + column[1] + "  |  Check-out: " + column[2]);
        System.out.println("\t\t\t\t Room:       " + column[3] + " (Room " + column[4] + ")"); 
        System.out.println("\t\t\t\t Adults:     " + column[5]);
        if (!column[6].isEmpty() && !column[6].equalsIgnoreCase("none")) {
            System.out.println("\t\t\t\t Children:   " + column[6]);
        }
        System.out.println("\t\t\t\t Payment:    " + column[11]);
        System.out.println("\t\t\t\t Status:     " + column[12]);
        System.out.println("\t\t\t\t--------------------------------------------------\n");
    }
}