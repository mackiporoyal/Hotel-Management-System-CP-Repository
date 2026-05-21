package finals;

import java.util.ArrayList;
import java.util.Scanner;
import finals.DatabaseLogic.Database;
import java.time.LocalDate;
import java.time.Month;
public class CreateBooking {
	LocalDate currentDate = LocalDate.now();
	int currentYear = currentDate.getYear();
	int currentMonthNumber = currentDate.getMonthValue();
	Month currentMonth = currentDate.getMonth();
	int currentDay = currentDate.getDayOfMonth();
	
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
		
		int yearIn;
		int monthIn;
		int dayIn;
		
		System.out.println("\t\t\t\tDate of Check In : ");
		while(true) {
		System.out.print("\t\t\t\tEnter year (YYYY) : ");
		yearIn = scan.nextInt();
		if(yearIn<currentYear) {
			System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
			continue;
		}
		break;
		}
		
		while(true) {
		System.out.print("\t\t\t\tEnter month (MM) : ");
		monthIn = scan.nextInt();
		if(monthIn<currentMonthNumber) {
			System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
			continue;
		}
		break;
		}
		
		while(true) {
		System.out.print("\t\t\t\tEnter day (DD) : ");
		dayIn = scan.nextInt();
		if(dayIn<currentDay) {
			System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
			continue;
		}
		break;
		}
	
		// Formats month and day with leading zeros if they are single digits (e.g., 5 becomes 05)
		String timeIn = String.format("%d-%02d-%02d", yearIn, monthIn, dayIn);
		
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		System.out.println("\t\t\t\t\t\tCheck in: " + timeIn);
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		
		int yearOut;
		int monthOut;
		int dayOut;
		
		System.out.println("\t\t\t\tDate of Check Out : ");
		while(true) {
		System.out.print("\t\t\t\tEnter year (YYYY) : ");
		yearOut = scan.nextInt();
		if(yearOut<currentYear) {
			System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
			continue;
		}
		break;
		}
			
		while(true) { 
		System.out.print("\t\t\t\tEnter month (MM) : ");
		monthOut = scan.nextInt();
		if(monthOut<currentMonthNumber) {
			System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
			continue;
		}
		break;
		}
			
		while(true) {
		System.out.print("\t\t\t\tEnter day (DD) : ");
		dayOut = scan.nextInt();
		if(dayOut<currentDay) {
			System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
			continue;
		}
		break;
		}
			
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
		
		writeLine.writeLine(timeIn, timeOut, roomType, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
	}
}