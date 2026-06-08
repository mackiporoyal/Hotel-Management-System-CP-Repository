package finals.features.cashier;

import finals.core.config.ProgramConstants;
import finals.core.ui.UIElement;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CashierController implements RoomPricing {
    
    public CashierController() {
        File logFile = new File(FINANCE_LOG_PATH);
        try { if (!logFile.exists()) logFile.createNewFile(); } catch (IOException e) {}
    }
    


    public void displayCashierMenu() {
        Scanner sc = new Scanner(System.in);
        String border = UIElement.createBorder("═");

        while (true) {
            System.out.println("\n\t\t╔" + border + "╗");
            UIElement.printCenteredRow("CASHIER DASHBOARD");
            System.out.println("\t\t╠" + border + "╣");
            UIElement.printRow(" ");
            UIElement.printRow("          [ 1 ] Process Payment");
            UIElement.printRow("          [ 2 ] View Pending Bookings (Unpaid/Downpayment)");
            UIElement.printRow("          [ 3 ] Financial Dashboard");
            UIElement.printRow("          [-1 ] Exit");
            UIElement.printRow(" ");
            System.out.println("\t\t╚" + border + "╝");
            System.out.print("\t\t          ► Choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine(); 
            if (choice == -1) break;
            
            switch(choice) {
                case 1: 
                    listUnpaidBookings(); 
                    System.out.print("\n\t\t          ► Enter Booking ID: "); 
                    processPayment(sc.nextLine()); 
                    break;
                case 2: listUnpaidBookings(); break;
                case 3: displayFinancialDashboard(); break;
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
                    
                    long nights = ChronoUnit.DAYS.between(LocalDate.parse(data[1].trim()), LocalDate.parse(data[2].trim()));
                    if (nights <= 0) nights = 1;

                    double roomPrice = calculateTotal(data[3], nights);
                    int swim = Integer.parseInt(data[9].trim());
                    int buffet = Integer.parseInt(data[10].trim());
                    double extras = calculateExtras(swim, buffet);
                    double total = calculateFinalTotal(data[3], nights, swim, buffet, Integer.parseInt(data[7].trim()));

                    String border = UIElement.createBorder("═");
                    System.out.println("\n\t\t╔" + border + "╗");
                    UIElement.printCenteredRow("BILLING STATEMENT & BREAKDOWN");
                    System.out.println("\t\t╠" + border + "╣");
                    UIElement.printRow(" ");
                    UIElement.printRow(String.format("  Booking ID:    %-90s", bookingId));
                    UIElement.printRow(String.format("  Guest Name:    %-90s", data[5].trim()));
                    UIElement.printRow(String.format("  Room Type:     %-90s", data[3].trim() + " (Room " + data[4].trim() + ")"));
                    UIElement.printRow(String.format("  Duration:      %-90s", nights + " Night(s)"));
                    UIElement.printRow(" ");
                    UIElement.printRow("  CHARGES LIST:");
                    UIElement.printRow(String.format("    ► Room Base Price:                           PHP %-50.2f", roomPrice));
                    UIElement.printRow(String.format("    ► Amenities (Swim Passes: %d, Buffet: %d):     PHP %-50.2f", swim, buffet, extras));
                    UIElement.printRow(" ");
                    System.out.println("\t\t╠" + border + "╣");
                    UIElement.printRow(String.format("  TOTAL BALANCE DUE:                             PHP %-50.2f", total));
                    UIElement.printRow(String.format("  REQUIRED DOWNPAYMENT (30%%):                    PHP %-50.2f", calculateDownpayment(total)));
                    System.out.println("\t\t╚" + border + "╝");

                    Scanner sc = new Scanner(System.in);
                    System.out.println("\n\t\t╔" + border + "╗");
                    UIElement.printCenteredRow("SELECT PAYMENT TYPE");
                    System.out.println("\t\t╠" + border + "╣");
                    UIElement.printRow("            [ 1 ] Downpayment (30%)");
                    UIElement.printRow("            [ 2 ] Full Payment (100%)");
                    System.out.println("\t\t╚" + border + "╝");
                    System.out.print("\t\t          ► Option: ");
                    int paymentChoice = sc.nextInt();

                    double amountToApply = (paymentChoice == 1) ? calculateDownpayment(total) : total;
                    String status = (paymentChoice == 1) ? "DOWNPAYMENT" : "FULLY_PAID";

                    System.out.print("\t\t          ► Enter Cash Received: PHP ");
                    double cash = sc.nextDouble();

                    if (cash < amountToApply) {
                        System.out.println("\t\t          [!] Insufficient amount. Required: PHP " + String.format("%.2f", amountToApply));
                        return;
                    }

                    double change = cash - amountToApply;
                    System.out.println("\t\t          ► Change: PHP " + String.format("%.2f", change));
                    
                    data[11] = status; 
                    lines.set(i, String.join("|", data));
                    Files.write(Paths.get(HOTEL_DB_PATH), lines);
                    
                    logTransaction(generateTransactionId(), bookingId, amountToApply);
                    System.out.println("\t\t          [Success] Transaction complete. Status: " + status);
                    return;
                }
            }
            System.out.println("\t\t          [!] ID Not Found.");
        } catch (Exception e) { System.out.println("\t\t          [!] Error: " + e.getMessage()); }
    }

    public void listUnpaidBookings() {
        String border = UIElement.createBorder("═");
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("PENDING BOOKINGS (UNPAID / DOWNPAYMENT)");
        System.out.println("\t\t╠" + border + "╣");
        System.out.printf("\t\t║ %-10s | %-50s | %-50s ║\n", " ID", " Guest Name", " Room Details");
        System.out.println("\t\t╠" + border + "╣");

        try {
            for (String line : Files.readAllLines(Paths.get(HOTEL_DB_PATH))) {
                if (line.contains("UNPAID") || line.contains("DOWNPAYMENT")) {
                    String[] d = line.split("\\|");
                    String rowContent = String.format(" %-9s | %-49s | %-50s ", d[0], d[5], d[3] + " - Room " + d[4]);
                    System.out.println("\t\t║" + rowContent + "║");
                }
            }
        } catch (IOException e) { UIElement.printRow("Error reading database."); }
        System.out.println("\t\t╚" + border + "╝");
    }

    private void logTransaction(String transId, String bookingId, double amount) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FINANCE_LOG_PATH, true)))) {
            out.println(LocalDate.now() + "|" + transId + "|" + bookingId + "|" + amount);
        } catch (IOException e) { System.out.println("\t\t [!] Error logging transaction."); }
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
        UIElement.printRow("            [ 2 ] Weekly Revenue Summary");
        UIElement.printRow("            [ 3 ] Yearly Revenue Summary");
        UIElement.printRow(" ");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Choice: ");
        int viewType = sc.nextInt();
        sc.nextLine(); 
        
        LocalDate today = LocalDate.now();
        double totalRevenue = 0.0;

        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("[ REVENUE AUDIT REPORT ]");
        System.out.println("\t\t╠" + border + "╣");
        String headers = String.format(" %-10s | %-8s | %-15s | %-12s | %-6s | %-25s | %-18s ", 
                         "Date", "Trans ID", "Guest Name", "Room Type", "Room #", "Amenities (S/B)", "Revenue Applied");
        System.out.println("\t\t║" + headers + "║");
        System.out.println("\t\t╠" + border + "╣");

        Map<String, String[]> hotelDbMap = new HashMap<>();
        try {
            List<String> hotelLines = Files.readAllLines(Paths.get(HOTEL_DB_PATH));
            for (String hLine : hotelLines) {
                String[] hData = hLine.split("\\|");
                if (hData.length > 0) hotelDbMap.put(hData[0].trim(), hData);
            }
        } catch (IOException e) { UIElement.printRow("          [!] Error pre-loading hotel registration data."); }

        try {
            List<String> logs = Files.readAllLines(Paths.get(FINANCE_LOG_PATH));
            for (String line : logs) {
                try {
                    String[] data = line.split("\\|");
                    LocalDate logDate = LocalDate.parse(data[0].trim());
                    String transId = data[1].trim();
                    String bookingId = data[2].trim();
                    double amount = Double.parseDouble(data[data.length - 1].trim());

                    boolean show = (viewType == 1 && logDate.equals(today)) ||
                                   (viewType == 2 && logDate.isAfter(today.minusDays(7))) ||
                                   (viewType == 3 && logDate.getYear() == today.getYear());

                    if (show) {
                        String guestName = "N/A", roomType = "N/A", roomNum = "N/A", amenitiesInfo = "None";
                        if (hotelDbMap.containsKey(bookingId)) {
                            String[] hData = hotelDbMap.get(bookingId);
                            roomType = hData[3].trim(); roomNum = hData[4].trim(); guestName = hData[5].trim();
                            amenitiesInfo = "Swim: " + hData[9].trim() + " | Buffet: " + hData[10].trim();
                        }
                        if (guestName.length() > 15) guestName = guestName.substring(0, 12) + "...";

                        String rowContent = String.format(" %-10s | %-8s | %-15s | %-12s | %-6s | %-25s | PHP %-14.2f ", 
                                            data[0].trim(), transId, guestName, roomType, roomNum, amenitiesInfo, amount);
                        System.out.println("\t\t║" + rowContent + "║");
                        totalRevenue += amount;
                    }
                } catch (Exception e) { continue; }
            }
        } catch (Exception e) { UIElement.printRow("          [!] Failed reading transaction logs."); }

        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow(String.format("          TOTAL COMBINED REVENUE COLLECTED : PHP %-50.2f", totalRevenue));
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
        } catch (IOException e) { return "00001"; }
    }

    @Override
    public double calculateTotal(String roomType, long nights) {
        String t = roomType.trim().toLowerCase();
        double p = t.contains("standard") ? STANDARD_PRICE : t.contains("deluxe") ? DELUXE_PRICE : 
                   t.contains("junior") ? JR_SUITE_PRICE : t.contains("suite") ? SUITE_PRICE : PENTHOUSE_PRICE;
        return p * nights;
    }

    @Override
    public double calculateExtras(int swim, int buffet) { return (swim * SWIM_PASS_PRICE) + (buffet * BUFFET_PASS_PRICE); }
    @Override
    public double calculateDownpayment(double total) { return total * 0.30; }

    public double calculateFinalTotal(String rt, long n, int s, int b, int a) { 
        int cap = rt.toLowerCase().contains("standard") ? CAP_STANDARD : 
                  rt.toLowerCase().contains("junior") ? CAP_JR_SUITE :
                  rt.toLowerCase().contains("suite") ? CAP_SUITE :
                  rt.toLowerCase().contains("penthouse") ? CAP_PENTHOUSE : CAP_DELUXE;
        double excess = Math.max(0, a - cap) * EXCESS_PAX_PRICE * n;
        return calculateTotal(rt, n) + calculateExtras(s, b) + excess; 
    }
}