package finals.DatabaseLogic;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database {
	
	private int adultGuest;
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
		try 
		{
			List<String> allLines = Files.readAllLines(absolutePath);
			String lastLine = allLines.get(allLines.size() - 1);
			String[] bookingNumber = lastLine.split("\\|");
			int num = Integer.parseInt(bookingNumber[0]);
			num++;
            
            FileWriter writer = new FileWriter(storageRPath.toString(), true);
            String toDatabase = num + "|" + this.timeIn  + "|" + this.timeOut + "|" + this.roomType + "|" + this.adultNames + "|" + this.childNames + "|" + this.totalAdult + "|" + this.totalChild + "|" + this.swimPasses + "|" + this.buffetPasses + "|" + this.status + "\n";
            
            writer.write(toDatabase);
            writer.flush();
            writer.close();
            
            System.out.println("\t\t\t\t===================================================");
    			System.out.println("\t\t\t\t             BOOKING SUCCESSFULY CREATED            ");
    			System.out.println("\t\t\t\t===================================================");	
    	}
		
		catch (IOException e) 
		{
			System.err.println("Try");
		}
	
	//logic for displaying
	}
}