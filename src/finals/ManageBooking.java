package finals;
import java.util.Scanner;
import finals.DatabaseLogic.Database;

public class ManageBooking {
    public void checkBooking() {
    	Scanner scan = new Scanner(System.in);
    	Database checkDatabase = new Database();
    	System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
		System.out.println("\t\t║                           CHECK EXISTING BOOKING                             ║");
		System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");

		System.out.println("\t\t\t\t\t  1. View All Bookings");
		System.out.println("\t\t\t\t\t  2. Search by Booking ID");
		System.out.println("\t\t\t\t\t  3. Search by Guest Name");
		System.out.print("\t\t\t\tENTER HERE: ");
		int num = scan.nextInt();
		scan.nextLine(); 
		
		String guestInfo = ""; 
		
		if (num == 2) {
			System.out.print("\t\t\t\tENTER BOOKING ID: ");
			guestInfo = scan.nextLine();
		} else if (num == 3) {
			System.out.print("\t\t\t\tENTER GUEST NAME: ");
			guestInfo = scan.nextLine();
		}
		
		System.out.println();

		checkDatabase.readDatabase(num, guestInfo);
    }
}

