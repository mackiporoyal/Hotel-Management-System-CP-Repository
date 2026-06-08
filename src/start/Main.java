package start;

import java.util.Scanner;

import finals.Booking.Menu;
import finals.Booking.Check_Booking.ManageBooking;
import finals.Booking.RoomAvailability.RoomAvailability;
import finals.Booking.RoomAvailability.RoomController;
import finals.DatabaseLogic.Database;
import finals.Receptionist.NewBooking;
import finals.Cashier.CashierController;

public class Main { // FIX 1: Removed "extends Database"
	
	public static void main(String[] args) {		
		
		Scanner scan = new Scanner(System.in);
		Database db = new Database();	
		NewBooking newBooking = new NewBooking();
		RoomAvailability roomAvailability = new RoomAvailability(); 
		RoomController room = new RoomController();
		Menu menu = new Menu();
		
		ManageBooking search = new ManageBooking();
		CashierController cash = new CashierController(); 
		
		
		while(true) {
			menu.displayMenu();
			System.out.print("\t\t\t\tENTER HERE: ");
			int chooseMenu = 0;
			
			try {
				chooseMenu = scan.nextInt();
				scan.nextLine(); // Clear scanner buffer
			} catch(Exception e) {
				scan.nextLine();
			}
			
			switch(chooseMenu) {
				case 1: // Create Booking
					newBooking.displayNewBooking();
					break;
					
				case 2: // Manage Booking
					search.checkBooking();
					break;
					
				case 3: 
					room.startMenu(false);
					break;
					
				default: 
					System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
					System.out.println("\t\t║                                INVALID OPTION                                ║");
					System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
					// FIX 4: Just display a clean error message since we know there are only 3 options right now
					System.out.println("\t\t\t\tPlease enter a valid option (1-3).");
			}
		}
	} 
}