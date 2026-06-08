package finals.features.checkbooking;

import java.util.List;
import java.util.Scanner;
import finals.core.ui.UIElement;

public class BookingController extends BookingManager {
    
    private final Scanner scan = new Scanner(System.in);

    /**
     * Primary feature entry router loop called from Main menu option [ 2 ].
     */
    public void launchManagementDashboard() {
        String border = UIElement.createBorder("═");

        while (true) {
            System.out.println("\n\t\t╔" + border + "╗");
            UIElement.printCenteredRow("BOOKING MANAGEMENT SYSTEM OPERATIONS");
            System.out.println("\t\t╠" + border + "╣");
            UIElement.printRow(" ");
            UIElement.printRow("          [ 1 ] View Bookings");
            UIElement.printRow("          [ 2 ] Edit Booking Record");
            UIElement.printRow("          [ 3 ] Delete Booking Record");
            UIElement.printRow("          [-1 ] Return to Main Dashboard Menu");
            UIElement.printRow(" ");
            System.out.println("\t\t╚" + border + "╝");
            System.out.print("\t\t          ► Option Choice: ");
            
            String choice = scan.nextLine().trim();
            if (choice.equals("-1")) return;

            switch (choice) {
                case "1": handleViewWorkflow(); break;
                case "2": handleEditWorkflow(); break;
                case "3": handleDeleteWorkflow(); break;
                default: System.out.println("\t\t            [!] Invalid menu choice option.");
            }
        }
    }

    // =========================================================================
    // FEATURE WORKFLOW 1: VIEW BOOKINGS MODULE (TABLE vs CARD DETECTOR)
    // =========================================================================
    private void handleViewWorkflow() {
        String border = UIElement.createBorder("═");
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("[ VIEW RECORDS SELECTION VIEW ]");
        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow("            [ 1 ] Search and View CARD by Unique Booking ID");
        UIElement.printRow("            [ 2 ] Search and View CARD by Guest Name Pattern");
        UIElement.printRow("            [ 3 ] List ALL Booking Records in a Structured GRID TABLE");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Select View Filter: ");
        String subChoice = scan.nextLine().trim();

        switch (subChoice) {
            case "1":
                System.out.print("\t\t          ► Enter Booking ID (e.g., 00001): ");
                renderTargetedBookingCards(2, scan.nextLine().trim());
                break;
            case "2":
                System.out.print("\t\t          ► Enter Guest Name to lookup: ");
                renderTargetedBookingCards(3, scan.nextLine().trim());
                break;
            case "3":
                renderMasterGridTable();
                break;
            default:
                System.out.println("\t\t            [!] Invalid option. View process aborted.");
        }
    }

    // =========================================================================
    // UI SUB-LOGIC: RENDER SPECIFIC MODE RECORD AS A POLISHED CARD
    // =========================================================================
    private void renderTargetedBookingCards(int searchType, String searchInput) {
        List<String[]> currentRecords = this.readAllLines();
        boolean matchFound = false;
        String border = UIElement.createBorder("═");
        String thinBorder = UIElement.createBorder("─");

        for (String[] row : currentRecords) {
            if (row.length < 13) continue;

            boolean isMatch = false;
            if (searchType == 2 && row[0].trim().equalsIgnoreCase(searchInput.trim())) isMatch = true;
            else if (searchType == 3 && row[5].toLowerCase().contains(searchInput.toLowerCase())) isMatch = true;

            if (isMatch) {
                matchFound = true;
                System.out.println("\n\t\t╔" + border + "╗");
                UIElement.printRow(String.format(" GUEST BOOKING CARD                                                   ID: %-12s", row[0].trim()));
                System.out.println("\t\t╠" + thinBorder + "╣");
                UIElement.printRow(String.format("  ► CHECK-IN DURATION : %s  to  %s", row[1].trim(), row[2].trim()));
                UIElement.printRow(String.format("  ► ROOM CONFIGURATION: Room %s (%s)", row[4].trim(), row[3].trim()));
                UIElement.printRow(String.format("  ► PRIMARY OCCUPANTS : %s", row[5].trim()));
                UIElement.printRow(String.format("  ► CHILD DEPENDENTS  : %s", row[6].trim()));
                UIElement.printRow(String.format("  ► AMENITIES TICKETS : Pool Passes: %s  |  Buffet Passes: %s", row[9].trim(), row[10].trim()));
                System.out.println("\t\t╠" + thinBorder + "╣");
                UIElement.printRow(String.format("  ► ACCOUNT BALANCE   : PAYMENT STATUS -> [%s]", row[11].trim()));
                UIElement.printRow(String.format("  ► SYSTEM LOG STATUS : MONITOR STATE  -> [%s]", row[12].trim()));
                System.out.println("\t\t╚" + border + "╝");
            }
        }

        if (!matchFound) {
            System.out.println("\t\t            [!] Operational warning: Zero card matches for key: " + searchInput);
        } else {
            System.out.print("\n\t\t          ► Press ENTER to return...");
            scan.nextLine();
        }
    }

    // =========================================================================
    // UI SUB-LOGIC: RENDER ALL ENTRIES AS A COMPREHENSIVE TEXT GRID TABLE
    // =========================================================================
    private void renderMasterGridTable() {
        List<String[]> currentRecords = this.readAllLines();
        if (currentRecords.isEmpty()) {
            System.out.println("\t\t            [!] Database tracking file is currently empty.");
            return;
        }

        String border = UIElement.createBorder("═");
        String thinBorder = UIElement.createBorder("─");

        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("MASTER REVENUE & RESERVATION DATA GRID MATRIX");
        System.out.println("\t\t╠" + border + "╣");
        
        // Dynamic padding structure mapping to fit into your standard width
        String columnHeaders = String.format(" %-6s │ %-10s │ %-10s │ %-15s │ %-5s │ %-25s │ %-12s │ %-10s ", 
                                             "ID", "Check-In", "Check-Out", "Room Type", "Room", "Primary Guest", "Payment", "Status");
        UIElement.printRow(columnHeaders);
        System.out.println("\t\t╠" + border + "╣");

        int activeRecordCounter = 0;
        for (String[] columns : currentRecords) {
            if (columns.length < 13) continue;

            String cleanGuestName = columns[5].trim();
            if (cleanGuestName.length() > 25) {
                cleanGuestName = cleanGuestName.substring(0, 22) + "...";
            }

            String rowString = String.format(" %-6s │ %-10s │ %-10s │ %-15s │ %-5s │ %-25s │ %-12s │ %-10s ", 
                                             columns[0].trim(), columns[1].trim(), columns[2].trim(), 
                                             columns[3].trim(), columns[4].trim(), cleanGuestName, 
                                             columns[11].trim(), columns[12].trim());
            
            UIElement.printRow(rowString);
            activeRecordCounter++;
            
            // Build separating lines dynamically so boundaries never slip out of alignment
            if (activeRecordCounter < currentRecords.size()) {
                System.out.println("\t\t╠" + thinBorder + "╣");
            }
        }

        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow("  Total Operational Record Index Count: " + activeRecordCounter);
        System.out.println("\t\t╚" + border + "╝");
        
        System.out.print("\t\t          ► Press ENTER to exit table view...");
        scan.nextLine();
    }

    // =========================================================================
    // FEATURE WORKFLOW 2: EDIT BOOKING MODULE
    // =========================================================================
    private void handleEditWorkflow() {
        this.editBookingMenu();
    }

    // =========================================================================
    // FEATURE WORKFLOW 3: DELETE BOOKING MODULE
    // =========================================================================
    private void handleDeleteWorkflow() {
        String border = UIElement.createBorder("═");
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("[ DELETE BOOKING ]");
        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow("            [ 1 ] Search and Delete by Booking ID");
        UIElement.printRow("            [ 2 ] Search and Delete by Guest Name");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Selection: ");
        String subChoice = scan.nextLine().trim();

        String targetId = null;
        if (subChoice.equals("1")) {
            System.out.print("\t\t          ► Enter ID to Delete: ");
            targetId = scan.nextLine().trim();
        } else if (subChoice.equals("2")) {
            System.out.print("\t\t          ► Enter Name to Delete: ");
            targetId = findSingleBookingIdByName(scan.nextLine().trim());
        } else {
            System.out.println("\t\t            [!] No match found.");
            return;
        }

        if (targetId == null) return;

        List<String[]> databaseLines = this.readAllLines();
        boolean purged = false;
        
        for (int i = databaseLines.size() - 1; i >= 0; i--) {
            if (databaseLines.get(i)[0].trim().equals(targetId)) {
                databaseLines.remove(i);
                purged = true;
                break;
            }
        }

        if (purged) {
            this.overwriteDatabase(databaseLines); 
            System.out.println("\t\t            [Success] Record ID " + targetId + " completely dropped from file systems.");
        } else {
            System.out.println("\t\t            [!] Critical Error: Index tracking pointer mapping mismatch.");
        }
    }

    private String findSingleBookingIdByName(String searchName) {
        List<String[]> currentRecords = this.readAllLines();
        for (String[] row : currentRecords) {
            if (row.length >= 6 && row[5].toLowerCase().contains(searchName.toLowerCase())) {
                return row[0].trim();
            }
        }
        System.out.println("\t\t            [!] Error: No matched records found tracking name pattern: " + searchName);
        return null;
    }
}