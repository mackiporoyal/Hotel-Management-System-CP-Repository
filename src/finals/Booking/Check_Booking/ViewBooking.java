package finals.Booking.Check_Booking;

import java.util.Scanner;

import finals.Booking.BookingManager;
import finals.DatabaseLogic.Database;

public class ViewBooking extends BookingManager{
		public void viewBooking() {
			Scanner scan = new Scanner(System.in);
	        Database db = new Database();
			
	        System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
	        System.out.println("\t\t║ -1 Back                          VIEW BOOKING                                ║");
	        System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
	       
	        int viewType = 0;
	        while (true) {
	            try {
	                System.out.println("\t\t\t\t\t  1. View All Booking");
	                System.out.println("\t\t\t\t\t  2. View Booking by ID");
	                System.out.println("\t\t\t\t\t  3. View by Guest Name");
	                System.out.print("\t\t\t\tENTER HERE: ");
	                viewType = scan.nextInt();
	                scan.nextLine(); // Clear scanner buffer
	                
	                // Allow the user to cancel and go back
	                if (viewType == -1) {
	                    return;
	                }
	                
	                // Validate menu choices
	                if (viewType >= 1 && viewType <= 3) {
	                    break;
	                }
	                System.out.println("\t\t\tInvalid choice. Please select 1, 2, or 3 (or -1 to go back).");
	            } catch (Exception e) {
	                System.out.println("\t\t\tInvalid input. Please enter a valid viewTypeber.");
	                scan.nextLine(); // Clear bad non-integer input from stream
	            }
	        }
	        
	        String guestInfo = ""; 
	        
	        if (viewType == 2) {
	            System.out.print("\t\t\t\tENTER BOOKING ID: ");
	            guestInfo = scan.nextLine();
	            if (guestInfo.equals("-1")) return; // Handle back cancellation here too
	        } else if (viewType == 3) {
	            System.out.print("\t\t\t\tENTER GUEST NAME: ");
	            guestInfo = scan.nextLine();
	            if (guestInfo.equals("-1")) return; // Handle back cancellation here too
	        }
	        
	        System.out.println();

	        // Execution safely passed to business logic tier
	        this.manageBooking(viewType, guestInfo);
	       
	        
	        
		}
}
