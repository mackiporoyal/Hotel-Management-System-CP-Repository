package finals;
import finals.DatabaseLogic.Database;

public class Check_For_Booking {
	
	public void runDatabase(int num, String input) {
		Database db = new Database();
		db.runDatabase(num, input); 
	}
}