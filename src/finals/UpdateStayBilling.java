package finals;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import finals.DatabaseLogic.Database;

import java.io.*;

public class UpdateStayBilling {
	private String booknum;
	private int indexarr;
	private String changes;
	Database update = new Database();
	
	public void booking(String book)
	{
		this.booknum = book;
	}
	public void UpdateStayBill(int indexarray, String choice)
	{
		this.indexarr = indexarray;
		this.changes = choice;
	}
	static enum head
	{
		BOOKNUM,
		DATEIN,
		DATEOUT, 
		ROOMTYPE, 
		ADULTNAME, 
		CHILDNAME,
		TOTALADULT, 
		TOTALCHILD, 
		SWIMPASS, 
		BUFFETPASS;
		

		}
	public void display ()
	{
		update.displayDatabase();

		try {
			BufferedWriter write = new BufferedWriter(new FileWriter("HotelDatabase.txt"));			
		}
	catch (Exception e)
		{
		
		}
	
		

} 
}