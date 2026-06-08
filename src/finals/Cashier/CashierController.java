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
    private final int WIDTH = 120; // Standardized UI width

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
            printRow("          [ 2 ] View Pending Unpaid Bookings");
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
                    
                    long nights = ChronoUnit.DAYS.between(LocalDate.parse(data[1].trim()), LocalDate.parse(data[2].trim()));
                    if (nights <= 0) nights = 1;

                    int adults = Integer.parseInt(data[7].trim());
                    int swim = Integer.parseInt(data[9].trim());
                    int buffet = Integer.parseInt(data[10].trim());
                    
                    double total = calculateFinalTotal(data[3], nights, swim, buffet, adults);
                    
                    System.out.println("\n\t\t FINAL TOTAL: PHP " + String.format("%.2f", total));
                    System.out.print("\t\t [1] Downpayment (30%) or [2] Full Payment: ");
                    Scanner sc = new Scanner(System.in);
                    double paid = (sc.nextInt() == 1) ? calculateDownpayment(total) : total;

                    data[11] = "PAID"; data[12] = "ACTIVE"; 
                    lines.set(i, String.join("|", data));
                    Files.write(Paths.get(hotelDbPath), lines);
                    
                    logTransaction(bookingId, paid);
                    printReceipt(bookingId, data[3], nights, swim, buffet, paid, adults);
                    return;
                }
            }
            System.out.println("\t\t [!] ID Not Found.");
        } catch (Exception e) { System.out.println("\t\t [!] Error: " + e.getMessage()); }
    }

    public void listUnpaidBookings() {
        String border = "═".repeat(WIDTH);
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("PENDING UNPAID BOOKINGS");
        System.out.println("\t\t╠" + border + "╣");
        System.out.printf("\t\t║ %-10s | %-50s | %-50s ║\n", " ID", " Guest Name", " Room Details");
        System.out.println("\t\t╠" + border + "╣");

        try {
            for (String line : Files.readAllLines(Paths.get(hotelDbPath))) {
                if (line.contains("UNPAID")) {
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
        System.out.println("\t\t║ Room: " + room + " (" + nights + " nights) @ PHP " + String.format("%.2f", calculateTotal(room, 1)) + " ".repeat(WIDTH - 50) + "║");
        System.out.println("\t\t║ Excess Pax: " + Math.max(0, adults - 2) + " pax @ PHP " + EXCESS_PAX_PRICE + " ".repeat(WIDTH - 45) + "║");
        System.out.println("\t\t║ Extras Total: PHP " + String.format("%.2f", calculateExtras(swim, buffet)) + " ".repeat(WIDTH - 30) + "║");
        System.out.println("\t\t╠" + border + "╣");
        printRow("TOTAL PAID: PHP " + String.format("%.2f", total));
        System.out.println("\t\t╚" + border + "╝");
    }

    private void printRow(String content) {
        int spacesNeeded = WIDTH - content.length();
        System.out.println("\t\t║" + content + " ".repeat(Math.max(0, spacesNeeded)) + "║");
    }

    private void logTransaction(String id, double amount) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(financePath, true)))) {
            out.println(LocalDate.now() + "|" + id + "|" + amount);
        } catch (IOException e) {}
    }

    public void displayFinancialDashboard() {} 

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