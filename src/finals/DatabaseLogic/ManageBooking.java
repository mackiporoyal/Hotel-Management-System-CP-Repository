package finals.DatabaseLogic;

import java.util.Scanner;

import finals.Booking.BookingManager;

public class ManageBooking extends BookingManager{
    public void checkBooking() {
        Scanner scan = new Scanner(System.in);
        Database db = new Database();
    

        System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║ -1 Back                         MANAGE BOOKING                               ║");
        System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");

        int num = 0;
        while (true) {
            try {
                System.out.println("\t\t\t\t\t  1. Create New Booking");
                System.out.println("\t\t\t\t\t  2. Edit Booking by ID");
                System.out.println("\t\t\t\t\t  3. Edit by Guest Name");
                System.out.print("\t\t\t\tENTER HERE: ");
                num = scan.nextInt();
                scan.nextLine(); // Clear scanner buffer
                
                // Allow the user to cancel and go back
                if (num == -1) {
                    return;
                }
                
                // Validate menu choices
                if (num >= 1 && num <= 3) {
                    break;
                }
                System.out.println("\t\t\tInvalid choice. Please select 1, 2, or 3 (or -1 to go back).");
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a valid number.");
                scan.nextLine(); // Clear bad non-integer input from stream
            }
        }
        
        String guestInfo = ""; 
        
        if (num == 2) {
            System.out.print("\t\t\t\tENTER BOOKING ID: ");
            guestInfo = scan.nextLine();
            if (guestInfo.equals("-1")) return; // Handle back cancellation here too
        } else if (num == 3) {
            System.out.print("\t\t\t\tENTER GUEST NAME: ");
            guestInfo = scan.nextLine();
            if (guestInfo.equals("-1")) return; // Handle back cancellation here too
        }
        
        System.out.println();

        // Execution safely passed to business logic tier
        this.manageBooking(num, guestInfo);
    }
}