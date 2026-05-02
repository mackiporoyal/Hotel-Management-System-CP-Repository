package finals;
import java.util.Scanner;
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
			System.out.println("\t\t\t\tHow many adults? : ");
			int adultNum = scan.nextInt();
	
			System.out.println("\t\t\t\tHow many Children? : ");
			int childNum = scan.nextInt();
			
			test.writeDatabase(adultNum, childNum);
		
		
		break;
		case 2: System.out.println("2");
		break;
		case 3:	roomAvailability.displayRoom.DisplayRoom();
		break;
		
		default: System.out.println("Enter + //logic to count how many case/length of enum values// only!");
		}
		scan.close(); 
		
	}
}
