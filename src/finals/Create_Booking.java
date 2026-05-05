package finals;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
public class Create_Booking {

	private int adultGuest;
	private int childGuest;
	Path storageRPath = Paths.get("HotelDatabase.txt");
	Path absolutePath = storageRPath.toAbsolutePath();
	public void writeDatabase(int adultGuest, int childGuest) {
		this.adultGuest = adultGuest;
		this.childGuest = childGuest;
	try {
		List<String> allLines = Files.readAllLines(absolutePath);
        String lastLine = allLines.get(allLines.size() - 1);
        System.out.println(lastLine);
  
		FileWriter writer = new FileWriter(storageRPath.toString(), true);
		String toDatabase = "00001" + "|" + this.adultGuest + "|" + this.childGuest + "\n";
		writer.write(toDatabase);
		writer.flush();
		writer.close();
	}
	
	catch (IOException e) {
		System.err.println("Try");
	}
}
}