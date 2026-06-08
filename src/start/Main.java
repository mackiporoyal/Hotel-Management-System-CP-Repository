package start;

import java.util.Scanner;

import finals.Booking.Menu;
import finals.Booking.RoomAvailability.RoomAvailability;
import finals.Booking.RoomAvailability.RoomController;
import finals.DatabaseLogic.Database;
import finals.DatabaseLogic.ManageBooking;
import finals.DatabaseLogic.AccountManager; // Import the new Auth manager
import finals.Receptionist.NewBooking;
import finals.Cashier.CashierController;

public class Main {
	
	public static void main(String[] args) {		
		
		Scanner scan = new Scanner(System.in);
		Database db = new Database();	
		AccountManager auth = new AccountManager(); // Auth Instance
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
				scan.nextLine(); 
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
					
				case 3: // Room Availability
					room.startMenu(false);
					break;

				case 4: // Cashier Access (Login Required)
					System.out.print("\t\t          ► Username: ");
					String user = scan.nextLine();
					System.out.print("\t\t          ► Password: ");
					String pass = scan.nextLine();

					// Cashier Login Security
				
					if (auth.authenticate(user, pass)) {
						System.out.println("\t\t          Login Successful!");
						cash.displayCashierMenu(); // Access granted
					} else {
						System.out.println("\t\t          [!] Invalid Credentials.");
					}
					break;

				default: 
					System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
					System.out.println("\t\t║                                INVALID OPTION                                ║");
					System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
					System.out.println("\t\t\t\tPlease enter a valid option.");
			}
		}
	} 
}