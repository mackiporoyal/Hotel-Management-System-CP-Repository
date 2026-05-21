package finals;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {		

		Menu menu = new Menu();
		Scanner scan = new Scanner(System.in);
		CreateBooking create = new CreateBooking();
		RoomAvailability room = new RoomAvailability();
		UpdateStayBilling update = new UpdateStayBilling();
		CheckForBooking search = new CheckForBooking();
		
		while(true) {
		menu.displayMenu();
		System.out.print("\t\t\t\tENTER HERE: ");
		int chooseMenu = 0;
		try {
		chooseMenu = scan.nextInt();
		scan.nextLine();
		}catch(Exception e) {
			scan.nextLine();
			continue;
		}
		
		switch(chooseMenu) {
		case 1:
		    create.writeDatabase(); 
		break;
		case 2: 
			search.checkBooking();
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
			update.booking(booknum); 
			update.display();
			
			System.out.println("\n\n\t\t\t\tChoose what to change (1-10): ");
			int index = scan.nextInt() -1;
			
			scan.nextLine();
			System.out.println("Enter changes: ");
			String changes = scan.nextLine();
			            
			update.UpdateStayBill(index,changes);
			
		break;	
		default: 
			System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
			System.out.println("\t\t║                                INVALID OPTION                                ║");
			System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
			System.out.println("Enter + //logic to count how many case/length of enum values// only!");
		}
		
		
		}
		
	} 
}