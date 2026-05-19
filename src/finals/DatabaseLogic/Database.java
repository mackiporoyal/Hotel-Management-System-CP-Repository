package finals.DatabaseLogic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Database {
	
	private String timeIn;
	private String timeOut;
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
	
	public void writeDatabase(String timeIn, String timeOut, String roomType, ArrayList<String> adultNames, ArrayList<String> childNames, int totalAdult, int totalChild, int swimPasses, int buffetPasses) 
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
	
	//logic for displaying 
	}
}