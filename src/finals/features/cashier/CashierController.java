package finals.features.cashier;

import finals.core.ui.UIElement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.InputMismatchException;

public class CashierController implements RoomPricing {
    
    public CashierController() {
        File logFile = new File(FINANCE_LOG_PATH);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating log file: " + e.getMessage());
        }
    }

    /**
     * Helper method to scan the finance logs and calculate the sum total 
     * of all payments already transacted for a specific booking ID.
     */
    private double getPreviousPaymentsTotal(String bookingId) {
        double previousTotal = 0.0;
        try {
            if (!Files.exists(Paths.get(FINANCE_LOG_PATH))) return 0.0;
            List<String> logLines = Files.readAllLines(Paths.get(FINANCE_LOG_PATH));
            for (String line : logLines) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split("\\|");
                
                if (data.length >= 10 && data[2].trim().equalsIgnoreCase(bookingId.trim())) {
                    previousTotal += Double.parseDouble(data[7].trim());
                }
            }
        } catch (Exception e) {
            return 0.0;
        }
        return previousTotal;
    }

    /**
     * Utility method to print clean, standardized key-value metadata pairs
     */
    private void printStandardSummaryRow(String label, String value) {
        UIElement.printRow(String.format("  %-14s: %-90s", label, value));
    }

    public void displayCashierMenu() {
        Scanner sc = new Scanner(System.in);
        String border = UIElement.createBorder("═");

        while (true) {
            System.out.println("\n\t\t╔" + border + "╗");
            UIElement.printCenteredRow("FOLIO MANAGEMENT"); // REBRANDED TITLE
            System.out.println("\t\t╠" + border + "╣");
            UIElement.printRow(" ");
            UIElement.printRow("          [ 1 ] Process Folio / Payment");
            UIElement.printRow("          [ 2 ] View Pending Bookings (Unpaid/Downpayment/Partially Paid)");
            UIElement.printRow("          [ 3 ] Financial Dashboard");
            UIElement.printRow("          [ 4 ] Process Guest Check-Out");
            UIElement.printRow("          [-1 ] Exit");
            UIElement.printRow(" ");
            System.out.println("\t\t╚" + border + "╝");
            System.out.print("\t\t          ► Choice: ");
            
            int choice = 0;
            
            // ADDED: Try-catch block to prevent application crashes from bad scanner inputs
            try {
                choice = sc.nextInt();
                sc.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("\t\t          [!] Invalid input detected. Please enter a valid number.");
                sc.nextLine(); // Clear the bad input from the buffer
                continue;      // Restart the loop safely
            }
            
            if (choice == -1) {
                break;
            }
            
            switch(choice) {
                case 1: 
                    listUnpaidBookings(); 
                    System.out.print("\n\t\t          ► Enter Booking ID: "); 
                    processPayment(sc.nextLine()); 
                    break;
                case 2: 
                    listUnpaidBookings(); 
                    break;
                case 3: 
                    displayFinancialDashboard(); 
                    break;
                case 4:
                    processCheckOut();
                    break;
                default:
                    System.out.println("\t\t          Invalid option choice.");
            }
        }
    }

    public void processPayment(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            System.out.println("\t\t          [!] Invalid ID. Entry cannot be empty.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(HOTEL_DB_PATH));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(bookingId)) {
                    String[] data = lines.get(i).split("\\|");
                    
                    LocalDate checkIn = LocalDate.parse(data[1].trim());
                    LocalDate checkOut = LocalDate.parse(data[2].trim());
                    long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

                    String roomTypeStr = data[3].trim();
                    if (roomTypeStr.equals("1")) roomTypeStr = "Standard"; // Normalize old entries

                    double roomPrice = calculateTotal(roomTypeStr, nights);
                    int swimPasses = Integer.parseInt(data[9].trim());
                    int buffetPasses = Integer.parseInt(data[10].trim());
                    double amenitiesTotal = calculateExtras(swimPasses, buffetPasses);
                    
                    int totalGuests = Integer.parseInt(data[7].trim());
                    double grandTotal = calculateFinalTotal(roomTypeStr, nights, swimPasses, buffetPasses, totalGuests);

                    double previousPaidAmount = getPreviousPaymentsTotal(bookingId);
                    
                    // FIXED FALLBACK CHECK: If logs are clean but status was downpayment, assume 30% baseline deduction
                    if (previousPaidAmount == 0 && data[11].trim().equalsIgnoreCase("DOWNPAYMENT")) {
                        previousPaidAmount = grandTotal * 0.30;
                    }

                    // FIXED: Dynamic calculation gap loop to capture ONLY the upcharge difference amount
                    double currentRemainingBalance = grandTotal - previousPaidAmount;

                    String border = UIElement.createBorder("═");
                    System.out.println("\n\t\t╔" + border + "╗");
                    UIElement.printCenteredRow("FOLIO BILLING STATEMENT & BREAKDOWN"); // REBRANDED
                    System.out.println("\t\t╠" + border + "╣");
                    UIElement.printRow(" ");
                    
                    // Call unified text block layout definitions
                    printStandardSummaryRow("Booking ID", bookingId);
                    printStandardSummaryRow("Guest Name", data[5].trim());
                    printStandardSummaryRow("Room Type", roomTypeStr + " (Room " + data[4].trim() + ")");
                    printStandardSummaryRow("Duration", nights + " Night(s)");
                    
                    UIElement.printRow(" ");
                    UIElement.printRow("  CHARGES LIST:");
                    UIElement.printRow(String.format("    ► Room Base Price:                           PHP %-50.2f", roomPrice));
                    UIElement.printRow(String.format("    ► Amenities (Swim Passes: %d, Buffet: %d):     PHP %-50.2f", swimPasses, buffetPasses, amenitiesTotal));
                    UIElement.printRow(" ");
                    System.out.println("\t\t╠" + border + "╣");
                    UIElement.printRow(String.format("  TOTAL INITIAL INVOICE DUE:                    PHP %-50.2f", grandTotal));
                    UIElement.printRow(String.format("  LESS PREVIOUS PAYMENT MADE:                    PHP %-50.2f", previousPaidAmount));
                    UIElement.printRow(String.format("  CURRENT OUTSTANDING FOLIO BALANCE:             PHP %-50.2f", currentRemainingBalance));
                    System.out.println("\t\t╚" + border + "╝");

                    if (currentRemainingBalance <= 0) {
                        System.out.println("\t\t          [Notice] This booking folio has already been settled in full.");
                        return;
                    }

                    Scanner sc = new Scanner(System.in);
                    int paymentChoice = 0;

                    // FIXED BRANCH ROUTING FOR EDITED / DOWNPAYMENT FOLIOS
                    if (previousPaidAmount > 0 || data[11].trim().equalsIgnoreCase("PARTIALLY_PAID")) {
                        System.out.println("\n\t\t╔" + border + "╗");
                        UIElement.printCenteredRow("FOLIO MANAGEMENT"); // REBRANDED MENU BOX
                        System.out.println("\t\t╠" + border + "╣");
                        UIElement.printRow("            [ 1 ] Go Back to Pending Bookings");
                        UIElement.printRow("            [ 2 ] Settle Outstanding Folio Upcharge Balance (100%)");
                        System.out.println("\t\t╚" + border + "╝");
                        System.out.print("\t\t          ► Option: ");
                        paymentChoice = sc.nextInt();
                        
                        if (paymentChoice == 1) {
                            System.out.println("\t\t          [Notice] Operation cancelled. Returning...");
                            return;
                        }
                    } else {
                        System.out.println("\n\t\t╔" + border + "╗");
                        UIElement.printCenteredRow("FOLIO MANAGEMENT"); // REBRANDED MENU BOX
                        System.out.println("\t\t╠" + border + "╣");
                        UIElement.printRow("            [ 1 ] Downpayment (30%)");
                        UIElement.printRow("            [ 2 ] Full Payment (100%)");
                        System.out.println("\t\t╚" + border + "╝");
                        System.out.print("\t\t          ► Option: ");
                        paymentChoice = sc.nextInt();
                    }

                    double amountToPay = 0;
                    String paymentStatusText = "";

                    if (paymentChoice == 1 && previousPaidAmount == 0) {
                        amountToPay = calculateDownpayment(grandTotal);
                        paymentStatusText = "DOWNPAYMENT";
                    } else if (paymentChoice == 2) {
                        amountToPay = currentRemainingBalance; // Settle ONLY the calculated delta gap amount
                        paymentStatusText = "FULLY_PAID";
                    } else {
                        System.out.println("\t\t          [!] Selection failed. Restarting...");
                        return;
                    }

                    System.out.print("\t\t          ► Enter Cash Received: PHP ");
                    double cashReceived = sc.nextDouble();

                    if (cashReceived < amountToPay) {
                        System.out.println("\t\t          [!] Insufficient amount. Required: PHP " + String.format("%.2f", amountToPay));
                        return;
                    }

                    double change = cashReceived - amountToPay;
                    System.out.println("\t\t          ► Change: PHP " + String.format("%.2f", change));
                    
                    double updatedRemainingBalance = currentRemainingBalance - amountToPay;

                    System.out.println("\n\t\t╔" + border + "╗");
                    UIElement.printCenteredRow("FOLIO TRANSACTION RECEIPT SNAPSHOT"); // REBRANDED
                    System.out.println("\t\t╠" + border + "╣");
                    UIElement.printRow(String.format("  ► AMOUNT PAID NOW       : PHP %-50.2f", amountToPay));
                    UIElement.printRow(String.format("  ► REMAINING BALANCE DUE : PHP %-50.2f", updatedRemainingBalance));
                    System.out.println("\t\t╚" + border + "╝");

                    data[11] = paymentStatusText; 
                    data[12] = "ACTIVE";         
                    
                    lines.set(i, String.join("|", data));
                    Files.write(Paths.get(HOTEL_DB_PATH), lines);
                    
                    String amenitiesFormattedLog = "Swim:" + swimPasses + "/Buffet:" + buffetPasses;
                    logTransaction(generateTransactionId(), bookingId, (int)nights, data[5].trim(), roomTypeStr, data[4].trim(), amountToPay, updatedRemainingBalance, amenitiesFormattedLog);
                    
                    System.out.println("\t\t          [Success] Folio synchronized. Monitor State activated to: ACTIVE");
                    return;
                }
            }
            System.out.println("\t\t          [!] ID Not Found.");
        } catch (InputMismatchException e) {
            System.out.println("\t\t          [!] Invalid input detected. Payment aborted safely.");
        } catch (Exception e) { 
            System.out.println("\t\t          [!] Error handling payment: " + e.getMessage()); 
        }
    }

    public void listUnpaidBookings() {
        String border = UIElement.createBorder("═");
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("PENDING RESERVATION FOLIOS (UNPAID / DOWNPAYMENT / PARTIALLY PAID)");
        System.out.println("\t\t╠" + border + "╣");
        
        String headerRow = String.format("  %-12s │ %-18s │ %-22s │ %-15s │ %-15s │ %-15s ", 
                                         "Booking ID", "Guest Name", "Room Type", "Rem. Balance", "Payment Status", "Monitor Status");
        UIElement.printRow(headerRow);
        System.out.println("\t\t╠" + border + "╣");

        try {
            List<String> databaseLines = Files.readAllLines(Paths.get(HOTEL_DB_PATH));
            for (String line : databaseLines) {
                if (line.contains("UNPAID") || line.contains("DOWNPAYMENT") || line.contains("PARTIALLY_PAID")) {
                    String[] dataFields = line.split("\\|");
                    
                    String id = dataFields[0].trim();
                    String guestName = dataFields[5].trim();
                    String roomTypeStr = dataFields[3].trim();
                    String roomNumStr = dataFields[4].trim();
                    
                    if (roomTypeStr.equals("1")) roomTypeStr = "Standard";
                    String roomDetails = roomTypeStr + " (Room " + roomNumStr + ")";
                    String payStatus = "[" + dataFields[11].trim() + "]";
                    String monitorStatus = "[" + dataFields[12].trim() + "]";
                    
                    LocalDate checkIn = LocalDate.parse(dataFields[1].trim());
                    LocalDate checkOut = LocalDate.parse(dataFields[2].trim());
                    long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
                    int totalGuests = Integer.parseInt(dataFields[7].trim());
                    int sPasses = Integer.parseInt(dataFields[9].trim());
                    int bPasses = Integer.parseInt(dataFields[10].trim());
                    
                    double initialGrandTotal = calculateFinalTotal(roomTypeStr, nights, sPasses, bPasses, totalGuests);
                    double previousPaidAmount = getPreviousPaymentsTotal(id);
                    
                    if (previousPaidAmount == 0 && dataFields[11].trim().equalsIgnoreCase("DOWNPAYMENT")) {
                        previousPaidAmount = initialGrandTotal * 0.30;
                    }
                    
                    double outstandingRemainingBalance = initialGrandTotal - previousPaidAmount;
                    String balanceFormatted = String.format("PHP %.2f", outstandingRemainingBalance);

                    if (guestName.length() > 18) guestName = guestName.substring(0, 15) + "...";
                    if (roomDetails.length() > 22) roomDetails = roomDetails.substring(0, 19) + "...";
                    if (balanceFormatted.length() > 15) balanceFormatted = balanceFormatted.substring(0, 12) + "...";
                    
                    String dataRow = String.format("  %-12s │ %-18s │ %-22s │ %-15s │ %-15s │ %-15s ", 
                                                   id, guestName, roomDetails, balanceFormatted, payStatus, monitorStatus);
                    UIElement.printRow(dataRow);
                }
            }
        } catch (IOException e) { 
            UIElement.printRow("Error reading text database lines."); 
        }
        System.out.println("\t\t╚" + border + "╝");
    }

    public void processCheckOut() {
        String border = UIElement.createBorder("═");
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("GUEST CHECK-OUT PROCESSING");
        System.out.println("\t\t╠" + border + "╣");

        String headerRow = String.format("  %-12s │ %-18s │ %-22s │ %-15s │ %-15s │ %-15s ", 
                                         "Booking ID", "Guest Name", "Room Type", "Rem. Balance", "Payment Status", "Monitor Status");
        UIElement.printRow(headerRow);
        System.out.println("\t\t╠" + border + "╣");

        boolean hasFullyPaid = false;

        try {
            List<String> databaseLines = Files.readAllLines(Paths.get(HOTEL_DB_PATH));
            for (String line : databaseLines) {
                if (line.contains("FULLY_PAID") && line.contains("ACTIVE")) {
                    hasFullyPaid = true;
                    String[] dataFields = line.split("\\|");
                    
                    String id = dataFields[0].trim();
                    String guestName = dataFields[5].trim();
                    String roomTypeStr = dataFields[3].trim();
                    String roomNumStr = dataFields[4].trim();
                    
                    if (roomTypeStr.equals("1")) roomTypeStr = "Standard";
                    String roomDetails = roomTypeStr + " (Room " + roomNumStr + ")";
                    String payStatus = "[" + dataFields[11].trim() + "]";
                    String monitorStatus = "[" + dataFields[12].trim() + "]";
                    
                    String balanceFormatted = "PHP 0.00";

                    if (guestName.length() > 18) guestName = guestName.substring(0, 15) + "...";
                    if (roomDetails.length() > 22) roomDetails = roomDetails.substring(0, 19) + "...";
                    
                    String dataRow = String.format("  %-12s │ %-18s │ %-22s │ %-15s │ %-15s │ %-15s ", 
                                                   id, guestName, roomDetails, balanceFormatted, payStatus, monitorStatus);
                    UIElement.printRow(dataRow);
                }
            }
            System.out.println("\t\t╚" + border + "╝");

            if (!hasFullyPaid) {
                System.out.println("\t\t          [Notice] No fully paid active bookings available for check-out.");
                return;
            }

            Scanner sc = new Scanner(System.in);
            System.out.print("\n\t\t          ► Enter Booking ID to Check-Out (or -1 to cancel): ");
            String targetId = sc.nextLine().trim();

            if (targetId.equals("-1")) return;

            boolean found = false;
            for (int i = 0; i < databaseLines.size(); i++) {
                String[] data = databaseLines.get(i).split("\\|");
                if (data[0].trim().equalsIgnoreCase(targetId) && data[11].trim().equalsIgnoreCase("FULLY_PAID") && data[12].trim().equalsIgnoreCase("ACTIVE")) {
                    data[12] = "CHECKED_OUT"; 
                    databaseLines.set(i, String.join("|", data));
                    Files.write(Paths.get(HOTEL_DB_PATH), databaseLines);
                    System.out.println("\t\t          [Success] Booking " + targetId + " checked out. Room " + data[4].trim() + " is now released to [VR].");
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("\t\t          [!] Booking ID not found or not eligible for check-out (must be FULLY_PAID and ACTIVE).");
            }

        } catch (IOException e) {
            System.out.println("\t\t          [!] Error processing check-out: " + e.getMessage());
        }
    }

    private void logTransaction(String transId, String bookingId, int nights, String guestName, String roomType, String roomNum, double paidNow, double remBalance, String amenities) {
        try {
            FileWriter fw = new FileWriter(FINANCE_LOG_PATH, true);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(LocalDate.now() + "|" + transId + "|" + bookingId + "|" + nights + "|" + guestName + "|" + roomType + "|" + roomNum + "|" + paidNow + "|" + remBalance + "|" + amenities);
            pw.close();
        } catch (IOException e) { 
            System.out.println("\t\t [!] Error logging transaction data row."); 
        }
    }

    public static void displayFinancialDashboard() {
        String border = UIElement.createBorder("═");
        Scanner sc = new Scanner(System.in);

        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("HOTEL MANAGEMENT SYSTEM - FINANCIAL DASHBOARD");
        UIElement.printCenteredRow("[ Select a dynamic filter view for revenue data ]");
        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow(" ");
        UIElement.printRow("            [ 1 ] Daily Revenue Summary");
        UIElement.printRow("            [ 2 ] Weekly Revenue Summary (7 Days)");
        UIElement.printRow("            [ 3 ] Monthly Revenue Summary (30 Days)");
        UIElement.printRow("            [ 4 ] Semi-Annual Revenue Summary (6 Months)");
        UIElement.printRow("            [ 5 ] Annual Revenue Summary (365 Days)");
        UIElement.printRow("            [ 6 ] View All-Time Complete Revenue History");
        UIElement.printRow(" ");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Choice: ");
        
        int viewType = 6;
        
        // ADDED: Try-catch block for numerical dashboard inputs
        try {
            viewType = sc.nextInt();
            sc.nextLine(); 
        } catch (InputMismatchException e) {
            System.out.println("\t\t          [!] Invalid input. Defaulting to View All-Time History [ 6 ].");
            sc.nextLine(); // Clear buffer
        }
        
        LocalDate today = LocalDate.now();
        double totalRevenue = 0.0;

        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("[ REVENUE AUDIT REPORT ]");
        System.out.println("\t\t╠" + border + "╣");
        
        // FIXED ALIGNMENT: Reduced sub-column layout block widths to mathematically equal exactly 114 visible padding blocks + pipes
        String headers = String.format(" %-10s │ %-5s │ %-10s │ %-15s │ %-19s │ %-12s │ %-12s │ %-15s", 
                         "Date", "ID", "Duration", "Guest Name", "Room Type", "Paid Now", "Rem. Balance", "Amenities Pass");
        UIElement.printRow(headers);
        System.out.println("\t\t╠" + border + "╣");

        try {
            List<String> logs = Files.readAllLines(Paths.get(FINANCE_LOG_PATH));
            for (String line : logs) {
                if (line.trim().isEmpty()) continue;
                String[] data = line.split("\\|");
                
                LocalDate logDate = LocalDate.parse(data[0].trim());
                String bookingId = data[2].trim();
                String nightsStr = data[3].trim() + " Night(s)";
                String guestName = data[4].trim();
                String roomType = data[5].trim();
                String roomNum = data[6].trim();
                
                double amountPaidNow = Double.parseDouble(data[7].trim());
                double snapshotRemainingBalance = Double.parseDouble(data[8].trim());
                String amenitiesInfo = data[9].trim();

                boolean showRecord = false;
                long daysDifference = ChronoUnit.DAYS.between(logDate, today);

                switch (viewType) {
                    case 1: if (logDate.equals(today)) showRecord = true; break;
                    case 2: if (daysDifference >= 0 && daysDifference <= 7) showRecord = true; break;
                    case 3: if (daysDifference >= 0 && daysDifference <= 30) showRecord = true; break;
                    case 4: if (daysDifference >= 0 && daysDifference <= 182) showRecord = true; break; 
                    case 5: if (daysDifference >= 0 && daysDifference <= 365) showRecord = true; break;
                    case 6: showRecord = true; break; 
                    default: showRecord = true; break;
                }

                if (showRecord) {
                    String paidNowStr = String.format("PHP %.0f", amountPaidNow);
                    String remBalanceStr = String.format("PHP %.0f", snapshotRemainingBalance);
                    String fullRoomDetails = roomType + " (Room " + roomNum + ")";

                    // Truncating safely inside the newly optimized cell limits
                    if (bookingId.length() > 5) bookingId = bookingId.substring(0, 5);
                    if (nightsStr.length() > 10) nightsStr = nightsStr.substring(0, 10);
                    if (guestName.length() > 15) guestName = guestName.substring(0, 12) + "...";
                    if (fullRoomDetails.length() > 20) fullRoomDetails = fullRoomDetails.substring(0, 17) + "...";
                    if (paidNowStr.length() > 12) paidNowStr = paidNowStr.substring(0, 12);
                    if (remBalanceStr.length() > 12) remBalanceStr = remBalanceStr.substring(0, 12);
                    if (amenitiesInfo.length() > 15) amenitiesInfo = amenitiesInfo.substring(0, 12) + "...";

                    // FIXED ALIGNMENT: Row formatter uses the exact same sub-width tracking limits as the headers block above
                    String rowContent = String.format(" %-10s │ %-5s │ %-10s │ %-15s │ %-19s │ %-12s │ %-12s │ %-15s", 
                                        data[0].trim(), bookingId, nightsStr, guestName, fullRoomDetails, paidNowStr, remBalanceStr, amenitiesInfo);
                    UIElement.printRow(rowContent);
                    
                    totalRevenue += amountPaidNow;
                }
            }
        } catch (Exception e) { 
            UIElement.printRow("          [!] Error loading text log history."); 
        }

        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow(String.format("  TOTAL COMBINED REVENUE COLLECTED : PHP %-50.2f", totalRevenue));
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Press ENTER to return to Dashboard...");
        sc.nextLine();
    }
    
    private String generateTransactionId() {
        try {
            File logFile = new File(FINANCE_LOG_PATH);
            if (!logFile.exists()) return "00001";
            List<String> logs = Files.readAllLines(Paths.get(FINANCE_LOG_PATH));
            return String.format("%05d", logs.size() + 1);
        } catch (IOException e) { 
            return "00001"; 
        }
    }

    @Override
    public double calculateTotal(String roomType, long nights) {
        String type = roomType.trim().toLowerCase();
        double basePrice = 0.0;
        
        if (type.contains("standard")) basePrice = STANDARD_PRICE;
        else if (type.contains("deluxe")) basePrice = DELUXE_PRICE;
        else if (type.contains("junior") || type.contains("jr")) basePrice = JR_SUITE_PRICE;
        else if (type.contains("penthouse")) basePrice = PENTHOUSE_PRICE;
        else basePrice = SUITE_PRICE;
        
        return basePrice * nights;
    }

    @Override
    public double calculateExtras(int swimPasses, int buffetPasses) { 
        return (swimPasses * SWIM_PASS_PRICE) + (buffetPasses * BUFFET_PASS_PRICE); 
    }
    
    @Override
    public double calculateDownpayment(double total) { 
        return total * 0.30; 
    }

    public double calculateFinalTotal(String roomType, long totalNights, int swimPasses, int buffetPasses, int actualGuests) { 
        String type = roomType.toLowerCase();
        int maximumCapacity = CAP_DELUXE;
        
        if (type.contains("standard")) maximumCapacity = CAP_STANDARD;
        else if (type.contains("penthouse")) maximumCapacity = CAP_PENTHOUSE;

        double excessCharges = Math.max(0, actualGuests - maximumCapacity) * EXCESS_PAX_PRICE * totalNights;
        return calculateTotal(roomType, totalNights) + calculateExtras(swimPasses, buffetPasses) + excessCharges; 
    }
}