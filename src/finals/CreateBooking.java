package finals;

import java.util.ArrayList;
import java.util.Scanner;
import finals.DatabaseLogic.Database;
import java.time.LocalDate;
import java.time.Month;

public class CreateBooking {
	

	private LocalDate currentDate = LocalDate.now();
	private int currentYear = currentDate.getYear();
	private int currentMonthNumber = currentDate.getMonthValue();
	private Month currentMonth = currentDate.getMonth();
	private int currentDay = currentDate.getDayOfMonth();
	
	private int totalAdult;
	private int totalChild;
	private int yearIn;
	private int monthIn;
	private int dayIn;
	private String timeIn;
	private int yearOut;
	private int monthOut;
	private int dayOut;
	private String timeOut;
	private String roomType;
	private ArrayList<String> adultNames = new ArrayList<>();
	private ArrayList<String> childNames = new ArrayList<>();
	private int swimPasses;
	private int buffetPasses;
	
	public void writeDatabase() { 
		Scanner scan = new Scanner(System.in);
		Database writeLine = new Database();

		adultNames.clear();
		childNames.clear();
		
		System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
		System.out.println("\t\t║ -1 Back                        CREATE BOOKING                                ║");
		System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
		
		while(true) {
			try {
				System.out.print("\t\t\t\tHow many adults? : ");
				totalAdult = scan.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid number.");
				scan.nextLine(); 
			}
		}
		if(totalAdult == -1) return;
		
		while(true) {
			try {
				System.out.print("\t\t\t\tHow many children? : ");
				totalChild = scan.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid number.");
				scan.nextLine();
			}
		}
		if(totalChild == -1) return;
        
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		System.out.println("\t\t\t\t\t\tTotal Guest: " + (totalAdult + totalChild));
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		
		System.out.println("\t\t\t\tDate of Check In : ");
		while(true) {
			try {
				System.out.print("\t\t\t\tEnter year (YYYY) : ");
				yearIn = scan.nextInt();
				if(yearIn == -1) return;
				if(yearIn < currentYear) {
					System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid 4-digit year.");
				scan.nextLine();
			}
		}
		
		while(true) {
			try {
				System.out.print("\t\t\t\tEnter month (MM) : ");
				monthIn = scan.nextInt();
				if(monthIn == -1) return;
				if(monthIn < currentMonthNumber && yearIn <= currentYear) {
					System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid month (1-12).");
				scan.nextLine();
			}
		}
		
		while(true) {
			try {
				System.out.print("\t\t\t\tEnter day (DD) : ");
				dayIn = scan.nextInt();
				if(dayIn == -1) return;
				if(dayIn < currentDay && monthIn <= currentMonthNumber && yearIn <= currentYear) {
					System.out.println("\t\t\tBackdating is not permitted. Please enter current or future dates.");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid day.");
				scan.nextLine();
			}
		}
	
		timeIn = String.format("%d-%02d-%02d", yearIn, monthIn, dayIn);
		
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		System.out.println("\t\t\t\t\t\tCheck in: " + timeIn);
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		
		System.out.println("\t\t\t\tDate of Check Out : ");
		while(true) {
			try {
				System.out.print("\t\t\t\tEnter year (YYYY) : ");
				yearOut = scan.nextInt();
				if(yearOut == -1) return;
				if(yearOut < yearIn) {
					System.out.println("\t\t\tInvalid checkout year. Must match or be after check-in year.");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid 4-digit year.");
				scan.nextLine();
			}
		}
			
		while(true) { 
			try {
				System.out.print("\t\t\t\tEnter month (MM) : ");
				monthOut = scan.nextInt();
				if(monthOut == -1) return;
				if(yearOut == yearIn && monthOut < monthIn) {
					System.out.println("\t\t\tInvalid checkout month. Must be equal to or after check-in month.");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid month (1-12).");
				scan.nextLine();
			}
		}
			
		while(true) {
			try {
				System.out.print("\t\t\t\tEnter day (DD) : ");
				dayOut = scan.nextInt();
				if(dayOut == -1) return;
				if(yearOut == yearIn && monthOut == monthIn && dayOut <= dayIn) {
					System.out.println("\t\t\tCheckout day must be after the check-in day.");
					continue;
				}
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a valid day.");
				scan.nextLine();
			}
		}
			
		timeOut = String.format("%d-%02d-%02d", yearOut, monthOut, dayOut);
		
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		System.out.println("\t\t\t\t\t\tCheck out: " + timeOut);
		System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
		scan.nextLine();
		
		System.out.print("\t\t\t\tChoose a Room Type : ");
		roomType = scan.nextLine();
		if(roomType.equals("-1")) return;
		
		for (int i = 1; i <= totalAdult; i++) {
			System.out.print("\t\t\t\tEnter name for Adult " + i + ": ");
			String name = scan.nextLine();
			if(name.equals("-1")) return;
			adultNames.add(name); 
		}
			
		for (int i = 1; i <= totalChild; i++) {
			System.out.print("\t\t\t\tEnter name for Child " + i + ": ");
			String name = scan.nextLine();
			if(name.equals("-1")) return;
			childNames.add(name); 
		}
		
		while(true) {
			try {
				System.out.print("\t\t\t\tHow many Pool Passes? : ");
				swimPasses = scan.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a number.");
				scan.nextLine();
			}
		}
		if(swimPasses == -1) return;
		
		while(true) {
			try {
				System.out.print("\t\t\t\tHow many Buffet Passes? : ");
				buffetPasses = scan.nextInt();
				break;
			} catch (Exception e) {
				System.out.println("\t\t\tInvalid input. Please enter a number.");
				scan.nextLine();
			}
		}
		if(buffetPasses == -1) return;
		
		writeLine.writeLine(timeIn, timeOut, roomType, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
		
	}
}
