package finals;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UpdateStayBilling {
	private String booknum;
	private int indexarr;
	private String changes;
	
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
		Path storageRPath = Paths.get("HotelDatabase.txt");
		Path absolutePath = storageRPath.toAbsolutePath();
			BufferedReader buffre;
			String li;
			String [] bn = null;
			try 
			{
				buffre = new BufferedReader(new FileReader("HotelDatabase.txt"));
				while ((li = buffre.readLine()) != null)
				{
						String [] row = li.split("\\|");
						if (row[0].trim().equals(booknum))
						{
							bn = row; 
							break;
						}
				}
				buffre.close();
			}
			catch (Exception e)
			{
				System.out.println("Error found while reading the textfile: " + e);
			}
			
			//
			for (head h : head.values())
			{
				System.out.print("     | " +h + " |  ");
			}
			System.out.println("\n");
			System.out.printf("%12s %20s %17s %15s %12s %20s %-18s %-18s %-18s %-18s", bn);	
			
			System.out.println("\n");
			for (head h : head.values())
			{
				System.out.print("     | " +h + " |  ");
			}	
	if(indexarr >= 0 && indexarr < head.values().length)
	{
		head hd = head.values()[indexarr];
	}
	String liney = ("\\|");
	String [] col = liney.split("\\|");

		
}
	
}

