package finals;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database {
	private int adultGuest;
	private int childGuest;
	Path storageRPath = Paths.get("HotelDatabase.txt");
	Path absolutePath = storageRPath.toAbsolutePath();
	
		public void DatabaseNewLine(int adultGuest, int childGuest) {
			Database.this.adultGuest = adultGuest;
			Database.this.childGuest = childGuest;
			
			try  {
				List<String> allLines = Files.readAllLines(absolutePath);
				String lastLine = allLines.get(allLines.size() - 1);
			    String[] bookingNumber = lastLine.split("\\|", - 1);
			    int num = Integer.parseInt(bookingNumber[0]);		
			    bookingNumber[0] = Integer.toString(++num);
			        
				FileWriter writer = new FileWriter(storageRPath.toString(), true);
				String toDatabase = bookingNumber[0] + "|" + Database.this.adultGuest + "|" + Database.this.childGuest + "\n";
				writer.write(toDatabase);
				writer.flush();
				writer.close();
			}
				
			catch (IOException e) {
				System.err.println("Try");
			}
		}
	
	
	
	public void DatabaseReadAllLine() {
		String storageRPath = "HotelDatabase.txt";
		try (BufferedReader br = new BufferedReader(new FileReader(storageRPath))){
		List<String> allLines = Files.readAllLines(absolutePath);
		System.out.println(allLines.toString());
		}catch(IOException e) {
			
		}
	}
}
