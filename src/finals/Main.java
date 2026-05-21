package finals;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {		

		Menu menu = new Menu();
		Scanner scan = new Scanner(System.in);
		CreateBooking create = new CreateBooking();
		RoomAvailability room = new RoomAvailability();
		UpdateStayBilling Update = new UpdateStayBilling();
		
		menu.displayMenu();
		System.out.print("\t\t\t\tENTER HERE: ");
		int chooseMenu = scan.nextInt();
		scan.nextLine();
		
		switch(chooseMenu) {
		case 1:
		    create.writeDatabase(); // This now automatically runs the entire question sequence!
		break;
		case 2: 
			System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
			System.out.println("\t\t║                           CHECK EXISTING BOOKING                             ║");
			System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");

			System.out.println("\t\t\t\t\t  1. View All Bookings");
			System.out.println("\t\t\t\t\t  2. Search by Booking Number");
			System.out.println("\t\t\t\t\t  3. Search by Guest Name");
			System.out.print("\t\t\t\tENTER HERE: ");
			int num = scan.nextInt();
			scan.nextLine(); 
			
			String input = ""; 
			
			if (num == 2) {
				System.out.print("\t\t\t\tENTER BOOKING ID: ");
				input = scan.nextLine();
			} else if (num == 3) {
				System.out.print("\t\t\t\tENTER GUEST NAME: ");
				input = scan.nextLine();
			}
			
			System.out.println();

			check.runDatabase(num, input);
			
		break;
		case 3:
		    room.printHeader();
		    room.displayRoom();
		    break;
		case 4:
			System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
			System.out.println("\t\t║                           UPDATE STAY AND BILLING                            ║");
			System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
			System.out.print("\t\t\t\tEnter Booking # : ");
			String booknum = scan.nextLine().trim();
			Update.booking(booknum); 
			Update.display();
			
			System.out.println("\n\n\t\t\t\tChoose what to change (1-10): ");
			int index = scan.nextInt() -1;
			
			scan.nextLine();
			System.out.println("Enter changes: ");
			String changes = scan.nextLine();
			            
			Update.UpdateStayBill(index,changes);
			
		break;	
		default: 
			System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
			System.out.println("\t\t║                                INVALID OPTION                                ║");
			System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
			System.out.println("Enter + //logic to count how many case/length of enum values// only!");
		}
		scan.close(); 
		
	} 
}