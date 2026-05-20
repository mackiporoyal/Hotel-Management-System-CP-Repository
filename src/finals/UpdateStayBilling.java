package finals;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;

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
			
			//Header
			
			try {
				List<String[]> allRows = Files.lines(Paths.get("HotelDatabase.txt")).map(line -> line.split("\\|")).collect(Collectors.toList());
				
		        if (allRows.isEmpty()) {
		            System.out.println("The database is empty.");
		            return;
		        }
		
		        int numColumns = allRows.get(0).length;
		        int[] maxWidths = new int[numColumns];
		        
		        for (String[] row : allRows) {
		            for (int i = 0; i < row.length; i++) {
		                if (row[i].length() > maxWidths[i]) {
		                    maxWidths[i] = row[i].length();
		   
				      }
		            }
		          }
		        int rowsToDisplay = Math.min(1, allRows.size());
		        
		        for (int r = 0; r < rowsToDisplay; r++) {
		            String[] row = allRows.get(r);
		            for (int c = 0; c < row.length; c++) {
		                
		                System.out.printf("%-" + (maxWidths[c] + 2) + "s", row[c]);
		            }
		            System.out.println();
		            
		            
		            //Selected booknum
		            if (bn != null)
		            {
		                for (int c = 0; c < bn.length; c++) 
		                {		                	
		                    System.out.printf("%-" + (maxWidths[c] + 2) + "s", bn[c]);
		                }
		                System.out.println();
		            }
		            else 
		            {
		                System.out.println("Booking number not found.");
		            }
		            
		           }
				}
				catch (Exception e)
				{
					System.out.println("Error reading the file: " + e.getMessage());
				}

	if(indexarr >= 0 && indexarr < head.values().length)
	{
		head hd = head.values()[indexarr];
	}
	
	
	 //Changes logic
	try {
	List<String[]> allRows = Files.lines(Paths.get("HotelDatabase.txt")).map(line -> line.split("\\|")).collect(Collectors.toList());
	
	try(FileWriter writer = new FileWriter("HotelDatabase.txt", false); PrintWriter pow = new PrintWriter(writer))
	{
       
		
		
		
			bn[indexarr] = changes;
			for (String[] row : allRows) {
				if (row[0].trim().equals(booknum))
				{
					String changeconfirmed = String.join("|", bn);
					pow.println(changeconfirmed);
				}
				else
				{
					String nochanges = String.join("|", row);
					pow.println(nochanges);
				}

			}
	}
	
	catch(IOException e)
	{
		System.err.println(e);
	}
	
	// Updated display logic
	int numColumns = allRows.get(0).length;
    int[] maxWidths = new int[numColumns];
    
    for (String[] row : allRows) {
        for (int i = 0; i < row.length; i++) {
            if (row[i].length() > maxWidths[i]) {
                maxWidths[i] = row[i].length();
	
            }
        }
    }
	if (bn != null)
    {
        for (int c = 0; c < bn.length; c++) 
        {		                	
            System.out.printf("%-" + (maxWidths[c] + 2) + "s", bn[c]);
        }
        System.out.println();
    }
	}
	catch (IOException e)
	{
		System.err.println(e);
	}

} 
}