package finals;
import java.util.Scanner;
import java.io.*;
import java.nio.*;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {		

		Menu menu = new Menu();
		Scanner scan = new Scanner(System.in);
		Create_Booking test = new Create_Booking();
		RoomAvailability roomAvailability = new RoomAvailability();
		UpdateStayBilling Update = new UpdateStayBilling();
		
		
		
		
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
			
			System.out.print("\t\t\t\tEnter adult guests' names : ");
			String adultNames = scan.nextLine();
				
			System.out.print("\t\t\t\tEnter child guests' names : ");
			String childNames = scan.nextLine();
			
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
		case 4:
			
			System.out.println("\t\t\t\t______________Update Stay and Billing______________");
			System.out.println("Choose booking number: ");
			String booknum = scan.nextLine();

			
			System.out.println("Enter a number of the divider you want to edit: (1-10) ");
			int arrayindex = scan.nextInt() -1;
			
			scan.nextLine();
			System.out.println("enter changes: ");
			String option = scan.nextLine();
			
			Update.UpdateStayBill(booknum, arrayindex, option);
		break;	
		default: System.out.println("Enter + //logic to count how many case/length of enum values// only!");
		}
		scan.close(); 
		
	}
}
