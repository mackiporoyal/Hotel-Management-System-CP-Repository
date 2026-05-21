package finals;

import java.util.ArrayList;
import java.util.Scanner;
import finals.DatabaseLogic.Database;

public class CreateBooking {
	
	public void writeDatabase() {
		Scanner scan = new Scanner(System.in);
		Database writeLine = new Database();

		System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
		System.out.println("\t\t║ -1 Back                        CREATE BOOKING                                ║");
		System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
		
		System.out.print("\t\t\t\tHow many adults? : ");
		int totalAdult = scan.nextInt();
		
		System.out.print("\t\t\t\tHow many children? : ");
		int totalChild = scan.nextInt();
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		System.out.println("\t\t\t\t\t\tTotal Guest: " + (totalAdult + totalChild));
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");

		System.out.println("\t\t\t\tDate of Check In : ");
		System.out.print("\t\t\t\tEnter year (YYYY) : ");
		int yearIn = scan.nextInt();
		System.out.print("\t\t\t\tEnter month (MM) : ");
		int monthIn = scan.nextInt();
		System.out.print("\t\t\t\tEnter day (DD) : ");
		int dayIn = scan.nextInt();	
		
		// Formats month and day with leading zeros if they are single digits (e.g., 5 becomes 05)
		String timeIn = String.format("%d-%02d-%02d", yearIn, monthIn, dayIn);
		
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		System.out.println("\t\t\t\t\t\tCheck in: " + timeIn);
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		
		System.out.println("\t\t\t\tDate of Check Out : ");
		System.out.print("\t\t\t\tEnter year (YYYY) : ");
		int yearOut = scan.nextInt();
		System.out.print("\t\t\t\tEnter month (MM) : ");
		int monthOut = scan.nextInt();
		System.out.print("\t\t\t\tEnter day (DD) : ");
		int dayOut = scan.nextInt();
		
		String timeOut = String.format("%d-%02d-%02d", yearOut, monthOut, dayOut);
		
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		System.out.println("\t\t\t\t\t\tCheck out: " + timeOut);
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		scan.nextLine(); // Clear scanner buffer
		
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
		
		writeLine.writeDatabase(timeIn, timeOut, roomType, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
		writeLine.refreshDatabase(); 
	}
}