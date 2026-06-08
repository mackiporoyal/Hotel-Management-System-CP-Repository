package finals.app;

import java.util.Scanner;
import finals.database.AccountManager; // Added import for authentication
import finals.features.booking.BookingController;
import finals.features.booking.NewBooking;
import finals.features.cashier.CashierController;
import finals.features.room.RoomController;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu menuEngine = new Menu();
        
        // Instantiate decoupled operational controllers and security managers
        NewBooking newBooking = new NewBooking();
        BookingController bookingController = new BookingController();
        AccountManager accountManager = new AccountManager();
        RoomController roomController = new RoomController();        
        
        while (true) {
            // 1. Render the structured ASCII art main menu box panel
            menuEngine.displayMenu();
            
            // 2. Prompt the operator for choice options
            System.out.print("\t\t          ► Enter option choice: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("-1") || input.toLowerCase().equals("exit")) {
                System.out.println("\n\t\t          [Notice] Shutting down Hotel Central Management System. Goodbye!");
                break;
            }
            
            switch (input) {
                case "1":
                    // Option 1: New Booking flow sequence (locks user selection parameters)
                    newBooking.setNewBooking();
                    break;
                    
                case "2":
                    // Redirects execution safely to the new layered management dashboard
                    bookingController.launchManagementDashboard();
                    break;
                    
                case "3":
                    // Option 3: General Room Availability Viewer & Operations
                    roomController.startMenu(false);
                    break;
                    
                case "4":
                    // Option 4: Folio Management gated behind Account Security
					accountManager.displayCashierAuth();
					break;

                    
                default:
                    System.out.println("\t\t            [!] Invalid option. Please enter a valid choice (1-4).");
                    break;
            }
        }
        
        scanner.close();
    }
}