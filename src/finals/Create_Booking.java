package finals;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
public class Create_Booking {

	private int timeIn;
	private int timeOut;
	private String roomType;
	private String adultNames;
	private String childNames;
	private int totalAdult;
	private int totalChild;
	private int swimPasses;
	private int buffetPasses;
	
	Path storageRPath = Paths.get("HotelDatabase.txt");
	Path absolutePath = storageRPath.toAbsolutePath();
	public void writeDatabase(int timeIn, int timeOut, String roomType, String adultNames, String childNames, int totalAdult, int totalChild, int swimPasses, int buffetPasses) {
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.roomType = roomType;
		this.adultNames = adultNames;
		this.childNames = childNames;
		this.totalAdult = totalAdult;
		this.totalChild = totalChild;
		this.swimPasses = swimPasses;
		this.buffetPasses = buffetPasses;
		
	try {
		List<String> allLines = Files.readAllLines(absolutePath);
        String lastLine = allLines.get(allLines.size() - 1);
        String[] bookingNumber = lastLine.split("\\|", - 1);
        int num = Integer.parseInt(bookingNumber[0]);		
        bookingNumber[0] = Integer.toString(++num);
        
		FileWriter writer = new FileWriter(storageRPath.toString(), true);
		String toDatabase = bookingNumber[0] + "|" + this.timeIn  + "|" + this.timeOut + "|" + this.roomType + "|" + this.adultNames + "|" + this.childNames + "|" + this.totalAdult + "|" + this.totalChild + "|" + this.swimPasses + "|" + this.buffetPasses + "\n";
		writer.write(toDatabase);
		writer.flush();
		writer.close();
	}
	
	catch (IOException e) {
		System.err.println("Try");
	}
}
}