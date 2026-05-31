package finals;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
	}
	public void updatedChanges()
	{

	 //Changes logic
	
	try {
		
		List<String[]> allRows = Files.lines(Paths.get("HotelDatabase.txt")).map(line -> line.split("\\|")).collect(Collectors.toList());
		String[] Target = null;
		for (String[] row : allRows)
	{
		if(row[0].trim().equals(booknum))
		{
			Target = row;
		}
	}
		
		Target[indexarr] = changes;
		try (FileWriter write = new FileWriter("HotelDatabase.txt", false);PrintWriter pow = new PrintWriter(write))
		{
			for (String row[] : allRows)
			{
				if(row[0].trim().equals(booknum))
				{
					pow.println(String.join("|", Target));
				}
				else
				{
					pow.println(String.join("|", row));
				}
				
			}
			
		}
		
		int[] maxWid = new int[Target.length];
        for (String[] rowe : allRows) {
            for (int i = 0; i < rowe.length; i++) {
                if (rowe[i].length() > maxWid[i]) {
                    maxWid[i] = rowe[i].length();
                }
            }
        }
        

			
		
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
            
        //Updated display
        System.out.println("\n\t\t\t\t________________Update Successful!________________");
        for (int r = 0; r < rowsToDisplay; r++) {
            String[] row = allRows.get(r);
            for (int c = 0; c < row.length; c++) {
                
                System.out.printf("%-" + (maxWidths[c] + 2) + "s", row[c]);
            }
            System.out.println();
        }

		
        for(int c = 0; c < Target.length; c++)
        {
        	System.out.printf("%-" + (maxWid[c] + 2) + "s", Target[c]);
        }
        System.out.println();
	}
	catch(IOException e)
	{
		System.err.println(e);
	}

	
} 
}