package finals.Booking;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import finals.DatabaseLogic.Database;

public class BookingManager extends Database {
    
    // ========================================================
    // FUNCTION 1: CREATE NEW BOOKING
    // ========================================================
    // I added 'int roomNumber' right after 'roomType'
    public void createBooking(String timeIn, String timeOut, String roomType, int roomNumber, 
                              List<String> adultNames, List<String> childNames, 
                              int totalAdult, int totalChild, int swimPasses, int buffetPasses) {
        
        int nextId = this.getNextBookingId();
        String paddedId = String.format("%05d", nextId);
        
        String joinedAdults = String.join(", ", adultNames);
        String joinedChildren = childNames.isEmpty() ? "none" : String.join(", ", childNames);
        
        // Separated the statuses so Cashier can edit Payment Status later
        String paymentStatus = "UNPAID"; 
        String bookingStatus = "ACTIVE"; 
        
        // We now have 13 columns to hold everything properly!
        // Format: ID | CheckIn | CheckOut | RoomType | RoomNum | Adults | Children | TotalAd | TotalCh | Swim | Buffet | Payment | BookingStatus
        String databaseRow = String.format("%s|%s|%s|%s|%d|%s|%s|%d|%d|%d|%d|%s|%s",
                paddedId, timeIn, timeOut, roomType, roomNumber, joinedAdults, joinedChildren,
                totalAdult, totalChild, swimPasses, buffetPasses, paymentStatus, bookingStatus);

        this.appendLine(databaseRow);

        System.out.println("\t\t\t\t===================================================");
        System.out.println("\t\t\t\t            BOOKING SUCCESSFULLY CREATED           ");
        System.out.println("\t\t\t\t===================================================");
    }

    // ========================================================
    // FUNCTION 2: MANAGE BOOKING (Search/Update)
    // ========================================================
    public void manageBooking(int searchType, String input) {
        List<String[]> currentRecords = this.readAllLines();
        boolean found = false;

        for (String[] row : currentRecords) {
            // We now require at least 13 columns
            if (row.length < 13) continue; 

            boolean match = false;
            if (searchType == 1) { // Match everything / List all
                match = true;
            } else if (searchType == 2 && row[0].trim().equalsIgnoreCase(input.trim())) { // Match ID
                match = true;
            } else if (searchType == 3 && row[5].toLowerCase().contains(input.toLowerCase())) { 
                // Name is at index [5]
                match = true;
            }

            if (match) {
                displayBookingCard(row);
                found = true;
            }
        }

        if (!found) {
            System.out.println("\t\t\t\t No matches found for: " + input);
        }
    }

    private void displayBookingCard(String[] column) {
        // column[3] = Room Type, column[4] = Room Number, column[5] = Adults, column[6] = Children
        // column[11] = Payment Status, column[12] = Booking Status
        
        System.out.printf("| %20s",  column[0] + " | " + column[1] + " to " + column[2] + " | Room " + column[4] + " | " + column[5] + (column[6].equalsIgnoreCase("none") ? "" : ", " + column[6]) + " | Status: " + column[12] + "\n" );
        
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

    // ========================================================
    // FUNCTION 3: CHECK ROOM AVAILABILITY
    // ========================================================

    private void printCalendar(int year, int month, List<Integer> bookedDays) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7; 
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        System.out.println("\t\t\t\tSun   Mon   Tue   Wed   Thu   Fri   Sat");
        System.out.print("\t\t\t\t");

        for (int i = 0; i < startDayOfWeek; i++) {
            System.out.print("      ");
        }

        for (int day = 1; day <= daysInMonth; day++) {
            if (bookedDays.contains(day)) {
                System.out.print(" XX   "); 
            } else {
                System.out.printf("%3d   ", day); 
            }

            if ((day + startDayOfWeek) % 7 == 0) {
                System.out.println();
                System.out.print("\t\t\t\t");
            }
        }
        System.out.println();
    }
}