package finals;

import java.nio.file.*;
import java.util.Scanner;
import java.io.IOException;
public class TextFileLogic {

	static Path storagePath = Paths.get("HotelDatabase.txt");
	Path fullPath = storagePath.toAbsolutePath();
	public static class setInfo{
		public void setGuestInfo() {
			Scanner scan = new Scanner(System.in);
	
			System.out.println("\t\t\t\t_________________Booking________________");
			
			System.out.println("\t\t\t\tHow many adults? : ");
			int adultnum = scan.nextInt();
	
			System.out.println("\t\t\t\tHow many Children? : ");
			int childnum = scan.nextInt();
			
			
			try
			{
				Create_Booking bookDets = new Create_Booking(adultnum, childnum);
				String bookdata = String.format("%d|%d\n", adultnum, childnum);
				Files.write(storagePath, bookdata.getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);
				
				System.out.println("\t\t\t\tBooking Saved!");
				
				
			}
			catch (IOException e)
			{
				System.err.println("Error Found while saving: " + e.getMessage());
			}
//			Main Return = new Main();
//			System.out.println();
//			Return.main(null);
			// loop if you want it to instantly return
		}
	}
}