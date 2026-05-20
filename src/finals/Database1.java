package finals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime; // Imported for checking current time
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class Database1 {
	
	private int adultGuest;
	private int timeIn;
	private int timeOut;
	private String roomType;
	private String adultNames;
	private String childNames;
	private int totalAdult;
	private int totalChild;
	private int swimPasses;
	private int buffetPasses;
	private String status; // Added status field
	
	Path storageRPath = Paths.get("HotelDatabase.txt");
	Path absolutePath = storageRPath.toAbsolutePath();
	
	public void writeDatabase(int timeIn, int timeOut, String roomType, ArrayList<String> adultNames, ArrayList<String> childNames, int totalAdult, int totalChild, int swimPasses, int buffetPasses) 
	{
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.roomType = roomType;
		this.adultNames = String.join(", ", adultNames);
		this.childNames = String.join(", ", childNames);
		this.totalAdult = totalAdult;
		this.totalChild = totalChild;
		this.swimPasses = swimPasses;
		this.buffetPasses = buffetPasses;
		this.status = "ACTIVE"; // New bookings start as ACTIVE
		
		try 
		{
			// Handle file initialization if it's empty
			List<String> allLines = Files.readAllLines(absolutePath);
			int num = 1;
			if (!allLines.isEmpty()) {
				String lastLine = allLines.get(allLines.size() - 1);
				String[] bookingNumber = lastLine.split("\\|", -1);
				num = Integer.parseInt(bookingNumber[0]) + 1;
			}
			
			FileWriter writer = new FileWriter(storageRPath.toString(), true);
			// Appended |ACTIVE at the end of the data string
			String toDatabase = num + "|" + this.timeIn  + "|" + this.timeOut + "|" + this.roomType + "|" + this.adultNames + "|" + this.childNames + "|" + this.totalAdult + "|" + this.totalChild + "|" + this.swimPasses + "|" + this.buffetPasses + "|" + this.status + "\n";
			
			writer.write(toDatabase);
			writer.flush();
			writer.close();
			
			System.out.println("\t\t\t\t===================================================");
			System.out.println("\t\t\t\t             BOOKING SUCCESSFULLY CREATED            ");
			System.out.println("\t\t\t\t===================================================");    
		}
		catch (IOException e) 
		{
			System.err.println("Error writing to database: " + e.getMessage());
		}
	}	
	/*
	 * Reads the entire database, checks if the checkout time has passed based 
	 * on the system's local time, and updates the status column to FINISHED if necessary.
	 */
	public void refreshDatabase() {
		try {
			List<String> allLines = Files.readAllLines(absolutePath);
			List<String> updatedLines = new ArrayList<>();
			
			// Get current system time hour (assuming timeIn/timeOut are stored as military hours, e.g., 14 for 2 PM)
			int currentHour = LocalTime.now().getHour(); 
			boolean fileChanged = false;

			for (String line : allLines) {
				if (line.trim().isEmpty()) continue;
				
				String[] tokens = line.split("\\|", -1);
		
				if (tokens.length >= 11) {
					int bookingTimeOut = Integer.parseInt(tokens[2]); 
					String currentStatus = tokens[10];                
					
					if (currentStatus.equalsIgnoreCase("ACTIVE") && currentHour >= bookingTimeOut) {
						tokens[10] = "FINISHED";
						fileChanged = true;
					}
				}
				
				// Rebuild the line
				updatedLines.add(String.join("|", tokens));
			}
	
			if (fileChanged) {
				Files.write(absolutePath, updatedLines);
				System.out.println("[Database Refreshed: Expired bookings set to FINISHED]");
			}
			
		} catch (IOException e) {
			System.err.println("Error refreshing database: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.err.println("Error parsing time data during refresh: " + e.getMessage());
		}
	}
}