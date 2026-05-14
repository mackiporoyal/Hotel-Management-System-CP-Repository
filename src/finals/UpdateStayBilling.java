package finals;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.*;

public class UpdateStayBilling {
	public void UpdateStayBill(String bookingnum, int indexarray, String choice)
	{
		String [] forecord; // Found Record, thats what it means
		String pathfile = "HotelDatabase.txt";
		BufferedReader buffre = null;
		
		try {
		
		buffre = new BufferedReader(new FileReader(pathfile));
		String l;
		while ((l = buffre.readLine())!= null)
		{
			String[] split = l.split("\\|");
			if (split.length > 0 && split[0].trim().equals(bookingnum.trim()));
			{
				forecord = split;
				break;
			}
		}
		buffre.close();
	}
		catch(Exception e)
		{
			System.err.println(e);
		}
		
		BufferedWriter buffwrite = null;
//		forecord = applyChange(forecord, indexarray, choice);
		
		
		
		
		
		
		
	}
public static void main(String[]args)
{
	String srcfile = "HotelDatabase.txt";
	BufferedReader read = null;
	String ln;
	
	try
	{
		read = new BufferedReader(new FileReader(srcfile));
		while((ln = read.readLine())!= null) // So basically with this while loop, it will continue reading the next line
			                                 // and if there is no next line then it will break out of the while loop and then continue on with the rest of the program
		{
			
			
			String[] row = ln.split("\\|(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // this will ensure it will seperate all that is in the database to different things. so if we want to take the booknum, then it will only print it out
			
			for (String index : row)
			{
				//the percent sign is for a formal specifier or a placeholder, and the number is the amount of spaces of room, to justify them, we put in " - "
				System.out.printf("%-5s, ", index);
			}
			System.out.println();
		}
		read.close();
	}
	catch (Exception e)
	{
		System.out.println(e);
	}
		
}
public enum head
{
	BOOKNUM(0), DATEIN(1), DATEOUT(2), ROOMTYPE(3), ADULTNAME(4), CHILDNAME(5),TOTALADULT(6), TOTALCHILD(7), SWIMPASS(8), BUFFETPASS(9);
	
	private final int header;
	head(int head){
		this.header = head;
	}
	public int getheader() {
		return this.header;
	}
	
}
}