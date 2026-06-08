package finals.features.checkbooking;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import finals.core.ui.UIElement;
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
        String bookingStatus = "INACTIVE"; 
        
        String databaseRow = String.format("%s|%s|%s|%s|%d|%s|%s|%d|%d|%d|%d|%s|%s",
                paddedId, timeIn, timeOut, roomType, roomNumber, joinedAdults, joinedChildren,
                totalAdult, totalChild, swimPasses, buffetPasses, paymentStatus, bookingStatus);

        this.appendLine(databaseRow);

        String border = UIElement.createBorder("═");
        String thinBorder = UIElement.createBorder("─");

        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("SYSTEM TRANSACTION NOTIFICATION");
        System.out.println("\t\t╠" + thinBorder + "╣");
        UIElement.printRow(" ");
        UIElement.printCenteredRow("SUCCESS: THE NEW GUEST REGISTRATION RECORD HAS BEEN SAVED");
        UIElement.printCenteredRow("[ Booking ID Reference: " + paddedId + " | Please proceed to payment processing to activate the booking. ]");
        UIElement.printRow(" ");
        System.out.println("\t\t╚" + border + "╝\n");
        
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

    public void editBookingMenu() {
        Scanner sc = new Scanner(System.in);
        String border = "═".repeat(120);
        String thinBorder = "─".repeat(120);
        
        System.out.print("\n\t\t\t  ► Enter Booking ID or Guest Name to Edit: ");
        String searchKey = sc.nextLine().trim();
        
        try {
            List<String[]> stringArrays = this.readAllLines();
            int targetIndex = -1;
            String[] data = null;

            for (int i = 0; i < stringArrays.size(); i++) {
                String[] currentFields = stringArrays.get(i);
                if (currentFields.length >= 13) {
                    if (currentFields[0].trim().equalsIgnoreCase(searchKey) || 
                        currentFields[5].toLowerCase().contains(searchKey.toLowerCase())) {
                        targetIndex = i;
                        data = currentFields;
                        break;
                    }
                }
            }

            if (targetIndex == -1 || data == null) {
                System.out.println("\t\t          [!] Record not found in database.");
                return;
            }

            for (int j = 0; j < data.length; j++) {
                data[j] = data[j].trim();
            }

            String oldRoomNum = data[4];
            
            // FIXED: Immutable baseline snapshot captures to prevent tracking leakage inside loops
            final int frozenOldSwim = Integer.parseInt(data[9]);
            final int frozenOldBuffet = Integer.parseInt(data[10]);

            while (true) {
                System.out.println("\n\t\t╔" + border + "╗");
                UIElement.printCenteredRow("BOOKING MODIFIER");
                System.out.println("\t\t╠" + thinBorder + "╣");
                UIElement.printRow(String.format("  [1] Check-In Date  : %-30s │ [7] Total Adults     : %-30s", data[1], data[7]));
                UIElement.printRow(String.format("  [2] Check-Out Date : %-30s │ [8] Total Children   : %-30s", data[2], data[8]));
                UIElement.printRow(String.format("  [3] Room Type      : %-30s │ [9] Pool Pass Qty    : %-30s", data[3], data[9]));
                UIElement.printRow(String.format("  [4] Room Number    : %-30s │ [10] Buffet Pass Qty : %-30s", data[4], data[10]));
                UIElement.printRow(String.format("  [5] Adult Name(s)  : %-30s │ [11] Payment Status  : %-30s", data[5], data[11]));
                UIElement.printRow(String.format("  [6] Child Name(s)  : %-30s │ [12] Monitor Status  : %-30s", data[6], data[12]));
                System.out.println("\t\t╠" + thinBorder + "╣");
                UIElement.printRow("  [ 0 ] Save Changes & Synchronize Database Core │ [ -1 ] Discard Changes & Exit");
                System.out.println("\t\t╚" + border + "╝");
                System.out.print("\t\t          ► Select field index to alter: ");
                int fieldChoice = sc.nextInt();
                sc.nextLine(); 

                if (fieldChoice == -1) {
                    System.out.println("\t\t          [Notice] Modifications discarded.");
                    return;
                }
                if (fieldChoice == 0) break;

                System.out.print("\t\t          ► Enter New Value: ");
                String newValue = sc.nextLine().trim();

                if (fieldChoice >= 1 && fieldChoice <= 12) {
                    if (fieldChoice == 4) {
                        System.out.println("\t\t          [System] Changing room target. Resetting Room " + oldRoomNum + " back to [VR]");
                        data[4] = newValue;
                    } else if (fieldChoice == 11) {
                        data[11] = newValue.toUpperCase(); 
                    } else if (fieldChoice == 12) {
                        data[12] = newValue.toUpperCase(); 
                    } else {
                        switch(fieldChoice) {
                            case 1: data[1] = newValue; break;
                            case 2: data[2] = newValue; break;
                            case 3: data[3] = newValue; break;
                            case 5: data[5] = newValue; break;
                            case 6: data[6] = newValue; break;
                            case 7: data[7] = newValue; break;
                            case 8: data[8] = newValue; break;
                            case 9: data[9] = newValue; break;
                            case 10: data[10] = newValue; break;
                        }
                    }
                    System.out.println("\t\t          [Success] Booking information updated.");
                } else {
                    System.out.println("\t\t          [!] Invalid field choice index flag.");
                }
            }

            // FIXED: Evaluates against the frozen variables safely outside the edit loop context
            int currentSwimPasses = Integer.parseInt(data[9]);
            int currentBuffetPasses = Integer.parseInt(data[10]);
            
            if (data[11].equalsIgnoreCase("FULLY_PAID")) {
                if (currentSwimPasses > frozenOldSwim || currentBuffetPasses > frozenOldBuffet) {
                    data[11] = "PARTIALLY_PAID";
                    System.out.println("\t\t          [System] Additional amenities detected! Payment state updated to: [PARTIALLY_PAID]");
                }
            }

            if (data[12].equalsIgnoreCase("CHECKED_OUT") || data[12].equalsIgnoreCase("INACTIVE")) {
                System.out.println("\t\t          [System] Room " + data[4] + " status flag synchronized to [VR].");
            } else if (data[12].equalsIgnoreCase("ACTIVE")) {
                System.out.println("\t\t          [System] Room " + data[4] + " status flag synchronized to [OCC].");
            }

            stringArrays.set(targetIndex, data);
            this.overwriteDatabase(stringArrays);
            System.out.println("\t\t          [Success] Complete hotel database synchronized seamlessly.");

        } catch (Exception e) {
            System.out.println("\t\t          [!] File subsystem pipe failure: " + e.getMessage());
        }
    }
}