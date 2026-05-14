package finals;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {		

		Menu menu = new Menu();
		Scanner scan = new Scanner(System.in);
		Create_Booking test = new Create_Booking();
		RoomAvailability roomAvailability = new RoomAvailability();
		
		System.out.println("\t\t\t\t===================================================");
		System.out.println("\t\t\t\t     WELCOME TO HOTEL MANAGEMENT CENTRAL SYSTEM     ");
		System.out.println("\t\t\t\t===================================================");
		menu.displayMenu();
		System.out.print("\t\t\t\tENTER HERE: ");
		int chooseMenu = scan.nextInt();
		scan.nextLine();
		
		switch(chooseMenu) {
		case 1:
			System.out.println("\t\t\t\t________________Create Booking_____________________");
			System.out.print("\t\t\t\tHow many adults? : ");
			int totalAdult = scan.nextInt();
	
			System.out.print("\t\t\t\tHow many children? : ");
			int totalChild = scan.nextInt();
			
			System.out.print("\t\t\t\tDate of Check In : ");
			int timeIn = scan.nextInt();
			
			System.out.print("\t\t\t\tDate of Check Out : ");
			int timeOut = scan.nextInt();
			
			scan.nextLine();
			
			System.out.print("\t\t\t\tChoose a Room Type : ");
			String roomType = scan.nextLine();
			
			ArrayList<String> adultNames = new ArrayList<>();
			for (int i = 1; i <= totalAdult; i++) {
			    System.out.print("\t\t\t\tEnter name for Adult " + i + ": ");
			    String name = scan.nextLine();
			    adultNames.add(name); 
			}
				
			ArrayList<String> childNames = new ArrayList<>();
			for (int i = 1; i <= totalChild; i++) {
			    System.out.print("\t\t\t\tEnter name for Child " + i + ": ");
			    String name = scan.nextLine();
			    childNames.add(name); 
			}
			
			System.out.print("\t\t\t\tHow many Pool Passes? : ");
			int swimPasses = scan.nextInt();
			
			System.out.print("\t\t\t\tHow many Buffet Passes? : ");
			int buffetPasses = scan.nextInt();
			
			
			test.writeDatabase(timeIn, timeOut, roomType, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
		
		
		break;
		case 2: System.out.println("2");
		break;
		case 3:	roomAvailability.displayRoom.DisplayRoom();
		break;
		//
		default: System.out.println("Enter + //logic to count how many case/length of enum values// only!");
		}
		scan.close(); 
		
	}
}
