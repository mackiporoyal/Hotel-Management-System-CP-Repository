package finals;
import java.nio.file.*;

public class Create_Booking {

	private int adultGuest;
	private int childGuest;
	private String name;
	public Create_Booking(int newAdult, int newChild, String newName) {
		this.adultGuest = newAdult;
		this.childGuest = newChild;
		this.name = newName;
	}
}
