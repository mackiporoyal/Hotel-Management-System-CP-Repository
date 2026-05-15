package finals;

import java.io.*;

public class UpdateStayBilling {
	private String Booknum;
	private int indexarr;
	private String changes;
	
	public void UpdateStayBill(String bookingnum, int indexarray, String choice)
	{
		this.Booknum = bookingnum;
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
			String filename = "HotelDatabase.txt";
			BufferedReader buffre;
			String li = "";
			String [] bn;
			try 
			{
				buffre = new BufferedReader(new FileReader(filename));
				while ((li = buffre.readLine()) != null)
				{
						String [] row = li.split("\\|(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
						if (row[0].trim().equals(Booknum))
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
			for (head h : head.values())
			{
				System.out.print("\t    " +h);
			}
			System.out.println();
			
			
			
			
			
			
			
			
	}

	
}
