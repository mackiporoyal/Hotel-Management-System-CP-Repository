package finals.Cashier;

import java.util.Scanner;

import finals.Cashier.Logic.CashierFunctions;
import finals.DatabaseLogic.Database;

public class Cashier {
    private Database db;
    private Scanner scanner;

    public Cashier(Database db) {
        this.db = db;
        this.scanner = new Scanner(System.in);
    }

    // Main process for handling cash transactions
    public void processCashPayment(String bookingId) {
        // 1. Find the booking from the database (Fixed method name: added 's')
        db.readAllLines(); 
        String[] targetBooking = null;

        for (String[] line : db.getLines()) {
            if (line.length > 0 && line[0].trim().equals(bookingId.trim())) {
                targetBooking = line;
                break;
            }
        }

        if (targetBooking == null) {
            System.out.println("\t\t\t\t[Error] Booking ID not found!");
            return;
        }

        // Check if it's already paid/inactive
        if (targetBooking.length <= 10 || !targetBooking[10].trim().equalsIgnoreCase("ACTIVE")) {
            String currentStatus = targetBooking.length > 10 ? targetBooking[10] : "UNKNOWN";
            System.out.println("\t\t\t\t[Notice] This booking is already " + currentStatus + ".");
            return;
        }

        // 2. Extract details to compute cost
        String roomType = targetBooking[3];
        int swimPasses = Integer.parseInt(targetBooking[8].trim());
        int buffetPasses = Integer.parseInt(targetBooking[9].trim());

        // 3. Calculate Total Bill
        double totalBill = calculateTotalCost(roomType, swimPasses, buffetPasses);
        
        System.out.println("\t\t\t\t===================================================");
        System.out.println("\t\t\t\t               CASHIER - PAYMENT                    ");
        System.out.println("\t\t\t\t===================================================");
        System.out.printf("\t\t\t\t Booking ID:   %s\n", bookingId);
        System.out.printf("\t\t\t\t Room Type:    %s\n", roomType);
        System.out.printf("\t\t\t\t Total Amount: $%.2f\n", totalBill);
        System.out.println("\t\t\t\t---------------------------------------------------");

        // 4. Accept Cash Payment
        double cashReceived = 0;
        while (cashReceived < totalBill) {
            System.out.print("\t\t\t\t Enter Cash Amount: $");
            try {
                cashReceived = Double.parseDouble(scanner.nextLine());
                if (cashReceived < -1) { // checking for any hidden escape routes
                     return;
                }
                if (cashReceived < totalBill) {
                    System.out.printf("\t\t\t\t [Error] Insufficient Cash! Still need $%.2f\n", (totalBill - cashReceived));
                }
            } catch (NumberFormatException e) {
                System.out.println("\t\t\t\t [Error] Invalid input. Please enter a valid number.");
            }
        }

        // 5. Calculate Change
        double change = cashReceived - totalBill;
        System.out.println("\t\t\t\t---------------------------------------------------");
        System.out.printf("\t\t\t\t Cash Received: $%.2f\n", cashReceived);
        System.out.printf("\t\t\t\t Change to Give: $%.2f\n", change);
        System.out.println("\t\t\t\t===================================================");

        // 6. Complete Transaction & Update DB Status to "PAID"
        // Fixed: Route this through CashierLogic since it controls database file overwrites
        CashierFunctions logic = new CashierFunctions();
        logic.updateBookingStatus(bookingId, "PAID");
        
        System.out.println("\t\t\t\t       TRANSACTION COMPLETED & DATABASE UPDATED     ");
        System.out.println("\t\t\t\t===================================================");
    }

    // Helper method to set your pricing rules
    private double calculateTotalCost(String roomType, int swim, int buffet) {
        double roomPrice = 0;
        
        switch (roomType.toUpperCase().trim()) {
            case "DELUXE": roomPrice = 150.00; break;
            case "SUITE":  roomPrice = 250.00; break;
            default:       roomPrice = 100.00; break; 
        }

        double swimCost = swim * 15.00;    
        double buffetCost = buffet * 25.00; 

        return roomPrice + swimCost + buffetCost;
    }
}