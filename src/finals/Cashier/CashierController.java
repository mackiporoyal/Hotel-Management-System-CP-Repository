package finals.Cashier;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class CashierController implements RoomPricing {
    private final String financePath = System.getProperty("user.dir") + File.separator + "src" + 
                                      File.separator + "finals" + File.separator + "DatabaseLogic" + 
                                      File.separator + "FinanceLog.txt";


    String dbFolder = System.getProperty("user.dir") + File.separator + "src" + 
                      File.separator + "finals" + File.separator + "DatabaseLogic" + File.separator;

    String hotelDbPath = dbFolder + "HotelDatabase.txt";
    String financeLogPath = dbFolder + "FinanceLog.txt";
    	
    public CashierController() {
        File logFile = new File(financePath);
        try {
            if (!logFile.exists()) {
                logFile.createNewFile(); // Creates the file automatically if missing
            }
        } catch (IOException e) {
            System.out.println("Critical: Could not initialize FinanceLog.txt");
        }
    }
    public void displayFinancialDashboard() {
        Scanner sc = new Scanner(System.in);
        String border = "═".repeat(120);

        System.out.println("\n\t\t╔" + border + "╗");
        System.out.println("\t\t║" + " ".repeat(120) + "║");
        System.out.println("\t\t║      [ 1 ] Weekly Report" + " ".repeat(95) + "║");
        System.out.println("\t\t║      [ 2 ] Monthly Report" + " ".repeat(94) + "║");
        System.out.println("\t\t║      [ 3 ] Annual Report" + " ".repeat(95) + "║");
        System.out.println("\t\t║" + " ".repeat(120) + "║");
        System.out.println("\t\t╚" + border + "╝");
        
        System.out.print("\t\t          ► Choose Report Type: ");
        int choice = sc.nextInt();
        
        generateReport(choice);
    }

    private void generateReport(int type) {
        double total = 0;
        LocalDate now = LocalDate.now();
        String border = "═".repeat(120);

        System.out.println("\n\t\t╔" + border + "╗");
        System.out.println("\t\t║" + " ".repeat(120) + "║");

        try (BufferedReader br = new BufferedReader(new FileReader(financePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                LocalDate logDate = LocalDate.parse(data[0]);
                double amount = Double.parseDouble(data[2]);

                boolean match = false;
                if (type == 1 && logDate.isAfter(now.minusDays(7))) match = true; // Weekly
                else if (type == 2 && logDate.getMonth() == now.getMonth()) match = true; // Monthly
                else if (type == 3 && logDate.getYear() == now.getYear()) match = true; // Annual

                if (match) {
                    String row = String.format(" Date: %s | ID: %s | Amount: PHP %.2f", data[0], data[1], amount);
                    System.out.println("\t\t║" + row + " ".repeat(Math.max(0, 120 - row.length())) + "║");
                    total += amount;
                }
            }
        } catch (IOException e) { System.out.println("\t\t║ No records found." + " ".repeat(102) + "║"); }

        System.out.println("\t\t╠" + border + "╣");
        String footer = " TOTAL REVENUE: PHP " + String.format("%.2f", total);
        System.out.println("\t\t║" + footer + " ".repeat(120 - footer.length()) + "║");
        System.out.println("\t\t╚" + border + "╝");
    }

    // --- Pricing Interface Implementations remain here ---
    public double calculateTotal(String roomType, long nights) { /* ... */ return 0.0; }
    public double calculateDownpayment(double total) { return total * 0.30; }
}