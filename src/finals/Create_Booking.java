package finals;
import java.util.ArrayList;
import finals.DatabaseLogic.Database;

public class Create_Booking {
	
	public void writeDatabase(int timeIn, int timeOut, String roomType, ArrayList<String> adultNames, ArrayList<String> childNames, int totalAdult, int totalChild, int swimPasses, int buffetPasses) {
			Database writeLine = new Database();
			writeLine.writeDatabase(timeIn, timeOut, roomType, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
	}
}