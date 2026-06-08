package finals.Cashier;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CashierController implements RoomPricing {
    
    private final String dbFolder = System.getProperty("user.dir") + File.separator + "src" + 
                                   File.separator + "finals" + File.separator + "DatabaseLogic" + File.separator;
    private final String hotelDbPath = dbFolder + "HotelDatabase.txt";
    private final String financePath = dbFolder + "FinanceLog.txt";
    
    // Made static to support static UI rendering methods
    private static final int WIDTH = 120; 

    public CashierController() {
        File logFile = new File(financePath);
        try { if (!logFile.exists()) logFile.createNewFile(); } catch (IOException e) {}
    }

    public void displayCashierMenu() {
        Scanner sc = new Scanner(System.in);
        String border = "═".repeat(WIDTH);

        while (true) {
            System.out.println("\n\t\t╔" + border + "╗");
            printCenteredRow("CASHIER DASHBOARD");
            System.out.println("\t\t╠" + border + "╣");
            printRow(" ");
            printRow("          [ 1 ] Process Payment");
            printRow("          [ 2 ] View Pending Bookings (Unpaid/Downpayment)");
            printRow("          [ 3 ] Financial Dashboard");
            printRow("          [-1 ] Exit");
            printRow(" ");
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
                case 2: 
                    listUnpaidBookings(); 
                    break;
                case 3: 
                    displayFinancialDashboard(); 
                    break;
            }
        }
    }

    public void processPayment(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            System.out.println("\t\t          [!] Invalid ID. Entry cannot be empty.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(hotelDbPath));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(bookingId)) {
                    String[] data = lines.get(i).split("\\|");
                    
                    // --- 1. CALCULATE BILL DETAILS ---
                    long nights = ChronoUnit.DAYS.between(LocalDate.parse(data[1].trim()), LocalDate.parse(data[2].trim()));
                    if (nights <= 0) nights = 1;

                    double roomPrice = calculateTotal(data[3], nights);
                    int swim = Integer.parseInt(data[9].trim());
                    int buffet = Integer.parseInt(data[10].trim());
                    double extras = calculateExtras(swim, buffet);
                    double total = calculateFinalTotal(data[3], nights, swim, buffet, Integer.parseInt(data[7].trim()));

                    // --- BOXED LOGICAL ALIGNMENT ---
                    String border = "═".repeat(WIDTH);
                    System.out.println("\n\t\t╔" + border + "╗");
                    printCenteredRow("BILLING STATEMENT & BREAKDOWN");
                    System.out.println("\t\t╠" + border + "╣");
                    printRow(" ");
                    printRow(String.format("  Booking ID:    %-90s", bookingId));
                    printRow(String.format("  Guest Name:    %-90s", data[5].trim()));
                    printRow(String.format("  Room Type:     %-90s", data[3].trim() + " (Room " + data[4].trim() + ")"));
                    printRow(String.format("  Duration:      %-90s", nights + " Night(s)"));
                    printRow(" ");
                    printRow("  CHARGES LIST:");
                    printRow(String.format("    ► Room Base Price:                           PHP %-50.2f", roomPrice));
                    printRow(String.format("    ► Amenities (Swim Passes: %d, Buffet: %d):     PHP %-50.2f", swim, buffet, extras));
                    printRow(" ");
                    System.out.println("\t\t╠" + border + "╣");
                    printRow(String.format("  TOTAL BALANCE DUE:                             PHP %-50.2f", total));
                    printRow(String.format("  REQUIRED DOWNPAYMENT (30%%):                    PHP %-50.2f", calculateDownpayment(total)));
                    System.out.println("\t\t╚" + border + "╝");

                    // --- 2. RESTORED PAYMENT OPTION SELECTION ---
                    Scanner sc = new Scanner(System.in);
                    System.out.println("\n\t\t╔" + border + "╗");
                    printCenteredRow("SELECT PAYMENT TYPE");
                    System.out.println("\t\t╠" + border + "╣");
                    printRow("            [ 1 ] Downpayment (30%)");
                    printRow("            [ 2 ] Full Payment (100%)");
                    System.out.println("\t\t╚" + border + "╝");
                    System.out.print("\t\t          ► Option: ");
                    int paymentChoice = sc.nextInt();

                    double amountToApply = 0;
                    String status = "";

                    if (paymentChoice == 1) {
                        amountToApply = calculateDownpayment(total);
                        status = "DOWNPAYMENT";
                    } else {
                        amountToApply = total;
                        status = "FULLY_PAID";
                    }

                    // --- 3. ASK FOR CASH ---
                    System.out.print("\t\t          ► Enter Cash Received: PHP ");
                    double cash = sc.nextDouble();

                    if (cash < amountToApply) {
                        System.out.println("\t\t          [!] Insufficient amount. Required: PHP " + String.format("%.2f", amountToApply));
                        return;
                    }

                    // Calculate change based on the choice path selected
                    double change = cash - amountToApply;
                    System.out.println("\t\t          ► Change: PHP " + String.format("%.2f", change));
                    
                    // --- 4. UPDATE DATABASE ---
                    data[11] = status; 
                    lines.set(i, String.join("|", data));
                    Files.write(Paths.get(hotelDbPath), lines);
                    
                    logTransaction(generateTransactionId(), bookingId, amountToApply);
                    System.out.println("\t\t          [Success] Transaction complete. Status: " + status);
                    return;
                }
            }
            System.out.println("\t\t          [!] ID Not Found.");
        } catch (Exception e) { System.out.println("\t\t          [!] Error: " + e.getMessage()); }
    }

    public void listUnpaidBookings() {
        String border = "═".repeat(WIDTH);
        System.out.println("\n\t\t╔" + border + "╗");
        printCenteredRow("PENDING BOOKINGS (UNPAID / DOWNPAYMENT)");
        System.out.println("\t\t╠" + border + "╣");
        System.out.printf("\t\t║ %-10s | %-50s | %-50s ║\n", " ID", " Guest Name", " Room Details");
        System.out.println("\t\t╠" + border + "╣");

        try {
            for (String line : Files.readAllLines(Paths.get(hotelDbPath))) {
                if (line.contains("UNPAID") || line.contains("DOWNPAYMENT")) {
                    String[] d = line.split("\\|");
                    String rowContent = String.format(" %-9s | %-49s | %-50s ", d[0], d[5], d[3] + " - Room " + d[4]);
                    System.out.println("\t\t║" + rowContent + "║");
                }
            }
        } catch (IOException e) { printRow("Error reading database."); }
        System.out.println("\t\t╚" + border + "╝");
    }

    // Made static to bridge scope gaps
    private static void printRow(String content) {
        int spacesNeeded = WIDTH - content.length();
        System.out.println("\t\t║" + content + " ".repeat(Math.max(0, spacesNeeded)) + "║");
    }

    // Made static to bridge scope gaps
    private static void printCenteredRow(String content) {
        String trimmed = content.trim();
        int totalSpaces = WIDTH - trimmed.length();
        int leftSpaces = totalSpaces / 2;
        int rightSpaces = totalSpaces - leftSpaces;
        System.out.println("\t\t║" + " ".repeat(Math.max(0, leftSpaces)) + trimmed + " ".repeat(Math.max(0, rightSpaces)) + "║");
    }

    private void logTransaction(String transId, String bookingId, double amount) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(financePath, true)))) {
            out.println(LocalDate.now() + "|" + transId + "|" + bookingId + "|" + amount);
        } catch (IOException e) { 
            System.out.println("\t\t [!] Error logging transaction.");
        }
    }

    // Keeping it static as per requested fix configuration
    public static void displayFinancialDashboard() {
        String border = "═".repeat(WIDTH);
        Scanner sc = new Scanner(System.in);
        
        // Dynamic path calculations shifted safely inside static memory allocation context
        String baseDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "finals" + File.separator + "DatabaseLogic" + File.separator;
        String hDbPath = baseDir + "HotelDatabase.txt";
        String fLogPath = baseDir + "FinanceLog.txt";

        System.out.println("\n\t\t╔" + border + "╗");
        printCenteredRow("HOTEL MANAGEMENT SYSTEM - FINANCIAL DASHBOARD");
        printCenteredRow("[ Select a dynamic filter view for revenue data ]");
        System.out.println("\t\t╠" + border + "╣");
        printRow(" ");
        printRow("            [ 1 ] Daily Revenue Summary");
        printRow("            [ 2 ] Weekly Revenue Summary");
        printRow("            [ 3 ] Yearly Revenue Summary");
        printRow(" ");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Choice: ");
        int viewType = sc.nextInt();
        sc.nextLine(); 
        
        LocalDate today = LocalDate.now();
        double totalRevenue = 0.0;

        System.out.println("\n\t\t╔" + border + "╗");
        printCenteredRow("[ REVENUE AUDIT REPORT ]");
        System.out.println("\t\t╠" + border + "╣");
        
        // Meticulously calculated column widths to total exactly 118 characters inside the box
        String headers = String.format(" %-10s | %-8s | %-15s | %-12s | %-6s | %-25s | %-18s ", 
                         "Date", "Trans ID", "Guest Name", "Room Type", "Room #", "Amenities (S/B)", "Revenue Applied");
        System.out.println("\t\t║" + headers + "║");
        System.out.println("\t\t╠" + border + "╣");

        Map<String, String[]> hotelDbMap = new HashMap<>();
        try {
            List<String> hotelLines = Files.readAllLines(Paths.get(hDbPath));
            for (String hLine : hotelLines) {
                String[] hData = hLine.split("\\|");
                if (hData.length > 0) {
                    hotelDbMap.put(hData[0].trim(), hData);
                }
            }
        } catch (IOException e) {
            printRow("          [!] Error pre-loading hotel registration data.");
        }

        try {
            List<String> logs = Files.readAllLines(Paths.get(fLogPath));
            for (String line : logs) {
                try {
                    String[] data = line.split("\\|");
                    LocalDate logDate = LocalDate.parse(data[0].trim());
                    String transId = data[1].trim();
                    String bookingId = data[2].trim();
                    double amount = Double.parseDouble(data[data.length - 1].trim());

                    boolean show = false;
                    if (viewType == 1) show = logDate.equals(today);
                    else if (viewType == 2) show = logDate.isAfter(today.minusDays(7));
                    else if (viewType == 3) show = logDate.getYear() == today.getYear();

                    if (show) {
                        String guestName = "N/A";
                        String roomType = "N/A";
                        String roomNum = "N/A";
                        String amenitiesInfo = "None";

                        if (hotelDbMap.containsKey(bookingId)) {
                            String[] hData = hotelDbMap.get(bookingId);
                            roomType = hData[3].trim();
                            roomNum = hData[4].trim();
                            guestName = hData[5].trim();
                            int swimPasses = Integer.parseInt(hData[9].trim());
                            int buffetPasses = Integer.parseInt(hData[10].trim());
                            amenitiesInfo = "Swim: " + swimPasses + " | Buffet: " + buffetPasses;
                        }

                        if (guestName.length() > 15) guestName = guestName.substring(0, 12) + "...";

                        String rowContent = String.format(" %-10s | %-8s | %-15s | %-12s | %-6s | %-25s | PHP %-14.2f ", 
                                            data[0].trim(), transId, guestName, roomType, roomNum, amenitiesInfo, amount);
                        System.out.println("\t\t║" + rowContent + "║");
                        totalRevenue += amount;
                    }
                } catch (Exception e) {
                    continue; 
                }
            }
        } catch (Exception e) { 
            printRow("          [!] Failed reading transaction logs."); 
        }

        System.out.println("\t\t╠" + border + "╣");
        printRow(String.format("          TOTAL COMBINED REVENUE COLLECTED : PHP %-50.2f", totalRevenue));
        System.out.println("\t\t╚" + border + "╝");
        
        System.out.print("\t\t          ► Press ENTER to return to Dashboard...");
        sc.nextLine();
    }
    
    private String generateTransactionId() {
        try {
            File logFile = new File(financePath);
            if (!logFile.exists()) return "00001";
            
            List<String> logs = Files.readAllLines(Paths.get(financePath));
            return String.format("%05d", logs.size() + 1);
        } catch (IOException e) { 
            return "00001"; 
        }
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
                  rt.toLowerCase().contains("penthouse") ? CAP_PENTHOUSE : CAP_DELUXE;
        double excess = Math.max(0, a - cap) * EXCESS_PAX_PRICE * n;
        return calculateTotal(rt, n) + calculateExtras(s, b) + excess; 
    }
}