package start;

import java.util.Scanner;

import finals.Booking.Menu;
import finals.Booking.RoomAvailability;
import finals.Booking.View_Booking.ManageBooking;
import finals.DatabaseLogic.Database;
import finals.Receptionist.NewBooking;
import finals.Cashier.Cashier;

public class Main { // FIX 1: Removed "extends Database"
	
	public static void main(String[] args) {		
		
		Scanner scan = new Scanner(System.in);
		Database db = new Database();	

		RoomAvailability masterRoomData = new RoomAvailability(); 
		Menu menu = new Menu();
		
		ManageBooking search = new ManageBooking();
		Cashier cash = new Cashier(db); 
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
					break;
					
				case 2: // Manage Booking
					search.checkBooking();
					break;
					
				case 3: 
					masterRoomData.getRoomAvailability(); 
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