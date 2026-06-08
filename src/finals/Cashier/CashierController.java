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
    private final int WIDTH = 120; 

    public CashierController() {
        File logFile = new File(financePath);
        try { if (!logFile.exists()) logFile.createNewFile(); } catch (IOException e) {}
    }

    public void displayCashierMenu() {
        Scanner sc = new Scanner(System.in);
        String border = "═".repeat(WIDTH);

        while (true) {
            System.out.println("\n\t\t╔" + border + "╗");
            printRow("CASHIER DASHBOARD");
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
                case 2: listUnpaidBookings(); break;
                case 3: displayFinancialDashboard(); break;
            }
        }
    }

    public void processPayment(String bookingId) {
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
                    
                    // Show breakdown
                    System.out.println("\n\t\t--- BILL DETAILS ---");
                    System.out.println("\t\t Room (" + data[3] + "): PHP " + String.format("%.2f", roomPrice));
                    System.out.println("\t\t Extras (Swim/Buffet): PHP " + String.format("%.2f", extras));
                    double total = calculateFinalTotal(data[3], nights, swim, buffet, Integer.parseInt(data[7].trim()));
                    System.out.println("\t\t TOTAL TO PAY: PHP " + String.format("%.2f", total));

                    // --- 2. ASK FOR CASH ---
                    Scanner sc = new Scanner(System.in);
                    System.out.print("\t\t Enter Cash Received: PHP ");
                    double cash = sc.nextDouble();

                    if (cash < (total * 0.30)) {
                        System.out.println("\t\t [!] Insufficient amount for downpayment.");
                        return;
                    }

                    // --- 3. MATH & STATUS ---
                    double applyAmount = (cash >= total) ? total : (total * 0.30); // Uses partial payment if cash is only 30%
                    double change = cash - applyAmount;
                    String status = (cash >= total) ? "FULLY_PAID" : "DOWNPAYMENT";

                    System.out.println("\t\t Change: PHP " + String.format("%.2f", change));
                    
                    // --- 4. UPDATE DATABASE ---
                    data[11] = status; 
                    lines.set(i, String.join("|", data));
                    Files.write(Paths.get(hotelDbPath), lines);
                    
                    logTransaction(generateTransactionId(), bookingId, applyAmount);
                    System.out.println("\t\t [!] Transaction complete. Status: " + status);
                    return;
                }
            }
        } catch (Exception e) { System.out.println("\t\t [!] Error: " + e.getMessage()); }
    }

    public void listUnpaidBookings() {
        String border = "═".repeat(WIDTH);
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("PENDING BOOKINGS (UNPAID / DOWNPAYMENT)");
        System.out.println("\t\t╠" + border + "╣");
        System.out.printf("\t\t║ %-10s | %-50s | %-50s ║\n", " ID", " Guest Name", " Room Details");
        System.out.println("\t\t╠" + border + "╣");

        try {
            for (String line : Files.readAllLines(Paths.get(hotelDbPath))) {
                // Show bookings that are not fully paid
                if (line.contains("UNPAID") || line.contains("DOWNPAYMENT")) {
                    String[] d = line.split("\\|");
                    String rowContent = String.format(" %-9s | %-49s | %-50s ", d[0], d[5], d[3] + " - Room " + d[4]);
                    System.out.println("\t\t║" + rowContent + "║");
                }
            }
        } catch (IOException e) { printRow("Error reading database."); }
        System.out.println("\t\t╚" + border + "╝");
    }
    	
    private void printReceipt(String id, String room, long nights, int swim, int buffet, double total, int adults) {
        String border = "═".repeat(WIDTH);
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("OFFICIAL RECEIPT");
        System.out.println("\t\t╠" + border + "╣");
        System.out.println("\t\t║ Booking ID: " + id + " ".repeat(WIDTH - 13 - id.length()) + "║");
        System.out.println("\t\t║ Room: " + room + " (" + nights + " nights) " + " ".repeat(WIDTH - 40) + "║");
        System.out.println("\t\t║ Extras Total: PHP " + String.format("%.2f", calculateExtras(swim, buffet)) + " ".repeat(WIDTH - 30) + "║");
        System.out.println("\t\t╠" + border + "╣");
        printRow("TOTAL PAID: PHP " + String.format("%.2f", total));
        System.out.println("\t\t╚" + border + "╝");
    }

    private void printRow(String content) {
        int spacesNeeded = WIDTH - content.length();
        System.out.println("\t\t║" + content + " ".repeat(Math.max(0, spacesNeeded)) + "║");
    }

 // Updated method to accept three parameters
    private void logTransaction(String transId, String bookingId, double amount) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(financePath, true)))) {
            // Log format: Date | TransID | BookingID | Amount
            out.println(LocalDate.now() + "|" + transId + "|" + bookingId + "|" + amount);
        } catch (IOException e) { 
            System.out.println("\t\t [!] Error logging transaction.");
        }
    }

    public void displayFinancialDashboard() {
        String border = "═".repeat(WIDTH);
        Scanner sc = new Scanner(System.in);

        System.out.println("\n\t\t╔" + border + "╗");
        printRow("SELECT REVENUE VIEW");
        printRow(" [1] Daily | [2] Weekly | [3] Yearly");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Choice: ");
        int viewType = sc.nextInt();
        
        LocalDate today = LocalDate.now();
        double totalRevenue = 0.0;

        System.out.println("\n\t\t╔" + border + "╗");
        printRow("REVENUE REPORT");
        System.out.println("\t\t╠" + border + "╣");
        
        try {
            List<String> logs = Files.readAllLines(Paths.get(financePath));
            for (String line : logs) {
                try {
                    String[] data = line.split("\\|");
                    // Check if the line has at least 3 parts (Date, ID, Amount)
                    // Adjust index based on your log format
                    LocalDate logDate = LocalDate.parse(data[0].trim());
                    
                    // Find the amount: if the line has 4 parts, it's index 3. 
                    // If it has 5, it might be index 4. Let's grab the last part.
                    double amount = Double.parseDouble(data[data.length - 1].trim());
                    String transId = data[1].trim();

                    boolean show = false;
                    if (viewType == 1) show = logDate.equals(today);
                    else if (viewType == 2) show = logDate.isAfter(today.minusDays(7));
                    else if (viewType == 3) show = logDate.getYear() == today.getYear();

                    if (show) {
                        printRow(String.format(" %-15s | %-15s | PHP %-10.2f", data[0], transId, amount));
                        totalRevenue += amount;
                    }
                } catch (Exception e) {
                    // This line is malformed, skip it and keep going
                    continue; 
                }
            }
        } catch (Exception e) { printRow("Error reading logs."); }

        System.out.println("\t\t╠" + border + "╣");
        printRow("TOTAL REVENUE: PHP " + String.format("%.2f", totalRevenue));
        System.out.println("\t\t╚" + border + "╝");
        
        System.out.print("\t\t          ► Press ENTER to return...");
        new Scanner(System.in).nextLine();
    }
    
 // --- ID GENERATOR ---
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