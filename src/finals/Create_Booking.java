package finals;
import java.nio.file.*;
import java.nio.file.Files;
import java.io.IOException;
public class Create_Booking {

	private int adultGuest;
	private int childGuest;
	private String name;
	public Create_Booking(int newAdult, int newChild, String newName) {
		this.adultGuest = newAdult;
		this.childGuest = newChild;
		this.name = newName;
	}
	public void savetoFile()
	{
		String bookingDets = String.format("Adults: %d, Children: %d%n, Name: %s", adultGuest, childGuest, name);
		
		try 
		{
			Files.write(Paths.get("booking.txt"),bookingDets.getBytes(),java.nio.file.StandardOpenOption.CREATE,java.nio.file.StandardOpenOption.APPEND);
			System.out.println("Booking saved successfully!");

		}
		catch (IOException e)
		{
			System.out.println("Error while saving booking" + e.getMessage());
		}
	}
}
