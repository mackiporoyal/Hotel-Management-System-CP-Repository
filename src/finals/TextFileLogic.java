package finals;

import java.nio.file.*;
import java.util.Scanner;
import java.io.IOException;
public class TextFileLogic {

	Path storagePath = Paths.get("HotelDatabase.txt");
	Path fullPath = storagePath.toAbsolutePath();
	public void d() {
		Scanner scan = new Scanner(System.in);

		System.out.println("How many adults? : ");
		int adultnum = scan.nextInt();

		System.out.println("How many Children? : ");
		int childnum = scan.nextInt();
		
		
		// room availability here
		
		System.out.println("Enter Guess Name: ");
		String name = scan.nextLine();
		
		
		try
		{
			Create_Booking bookDets = new Create_Booking(adultnum, childnum, name);
			String bookdata = String.format("%d|%d%n|%s", adultnum, childnum, name);
			Files.write(storagePath, bookdata.getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);
			
			System.out.println("Booking Saved!");
			
			
		}
		catch (IOException e)
		{
			System.err.println("Error Found while saving: " + e.getMessage());
		}
	}
}
