package finals.DatabaseLogic;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database {
	
	private int timeIn;
	private int timeOut;
	private String roomType;
	private String adultNames;
	private String childNames;
	private int totalAdult;
	private int totalChild;
	private int swimPasses;
	private int buffetPasses;
	private String status;
	
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
		this.status = "ACTIVE";
		
		int num = 1000; 
		
		try 
		{
			if (Files.exists(absolutePath) && Files.size(absolutePath) > 0) {
				List<String> allLines = Files.readAllLines(absolutePath);
				if (!allLines.isEmpty()) {
					String lastLine = allLines.get(allLines.size() - 1);
					String[] bookingNumber = lastLine.split("\\|");
					num = Integer.parseInt(bookingNumber[0].trim()) + 1;
				}
			} else {
				Files.createFile(absolutePath);
			}
			
			String toDatabase = num + "|" + this.timeIn  + "|" + this.timeOut + "|" + this.roomType + "|" + this.adultNames + "|" + this.childNames + "|" + this.totalAdult + "|" + this.totalChild + "|" + this.swimPasses + "|" + this.buffetPasses + "|" + this.status + "\n";
			
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageRPath.toString(), true))) {
				writer.write(toDatabase);
				
				System.out.println("\t\t\t\t===================================================");
				System.out.println("\t\t\t\t             BOOKING SUCCESSFULLY CREATED          ");
				System.out.println("\t\t\t\t===================================================");	
			}
		} 
		catch (Exception e) 
		{
			System.err.println("Database write operation failed!");
			e.printStackTrace();
		}
	}
	
	public void runDatabase(int num, String input) {
		boolean exists = false;

		try (BufferedReader br = new BufferedReader(new FileReader(absolutePath.toFile()))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					String[] row = line.split("\\|", -1);
					
					if (row.length >= 10) {
						if (num == 1) {
							displayBooking(row);
							exists = true;
						} else if (num == 2 || num == 3) {
							int col = (num == 2) ? 0 : 4; 
							
							if (row[col].toLowerCase().contains(input.toLowerCase())) {
								displayBooking(row);
								exists = true;
							}
						}
					} 
				}
			}
			
			if (!exists && num != 1) {
				System.out.println("\t\t\t\t\t No matches found for: " + input);
			}
			
		} catch (IOException e) {
			System.out.println("\t\t\t\t\t Error reading database: " + e.getMessage());
		}
	}

	private void displayBooking(String[] row) {
		System.out.println("\t\t\t\t--------------------------------------------------");
		System.out.println("\t\t\t\t Booking ID: " + row[0]);
		System.out.println("\t\t\t\t Check-in:  " + row[1] + "  |  Check-out: " + row[2]);
		System.out.println("\t\t\t\t Room Type: " + row[3]);
		System.out.println("\t\t\t\t Guests:    " + row[4]);
		if (!row[5].isEmpty() && !row[5].equalsIgnoreCase("none")) {
			System.out.println("\t\t\t\t Children:  " + row[5]);
		}
		System.out.println("\t\t\t\t Passes:    Pool (" + row[8] + ") | Buffet (" + row[9] + ")");
		System.out.println("\t\t\t\t--------------------------------------------------\n");
	}
}
