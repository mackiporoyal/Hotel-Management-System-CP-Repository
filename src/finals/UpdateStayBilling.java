package finals;
import java.util.Scanner;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class UpdateStayBilling {
	public static void main(String[]args)
	{
	Scanner s = new Scanner(System.in);
	
	
	// where to enter the booking number so that it can find it within the files
	System.out.println("\t\t\t\t_________________Update Stay and Billing_________________");
	System.out.print("\t\t\t\t Choose booking number: ");
	String bookNum = s.nextLine().trim();
	
	String [] data = null;
	try
	{
		BufferedReader buffread = new BufferedReader(new FileReader("HotelDatabase.txt"));
		String line;
		while((line = buffread.readLine()) != null) {
			String [] part = line.split("\\|", -1);
			if (part.length > 0 && part[0].trim().equals(bookNum))
			{
				data  = part;
				break;
			}
		}
		buffread.close();
	}
	catch (Exception e)
	{
		System.out.println("Error while reading file");
		return;
	}
	if (data == null)
	{
		System.out.println("booking number not found.");
		return;
	}
	// the list
	System.out.println("\nBooking #   | Date In   | Date Out   | Room Type   | Adult Names   | Child Name   | Total Adult   | Total Child  | Swim Pass   | Buffet Pass    ");
	System.out.println("____________________________________________________________________________________________________________________________________________\n");
	 System.out.printf("%-9s | %-7s | %-8s | %-9s | %-11s | %-11s | %-11s | %-11s | %-9s | %s\n", data);
	 System.out.println("\n____________________________________________________________________________________________________________________________________________");
	
	 // the menu
     System.out.println("\n\t\t\t\tChoose what to change:");
     System.out.println("\t\t1. Booking Number");
     System.out.println("\t\t2. Date In");
     System.out.println("\t\t3. Date Out");
     System.out.println("\t\t4. Room Type");
     System.out.println("\t\t5. Adult Names");
     System.out.println("\t\t6. Child Names");
     System.out.println("\t\t7. Total Adult");
     System.out.println("\t\t8. Total Child");
     System.out.println("\t\t9. Swim Passes");
     System.out.println("\t\t10. Buffet Passes");
     System.out.print("\t\tEnter selected number 1 - 10: ");
     int num = Integer.parseInt(s.nextLine()) - 1;
     
     String [] headers = {"Booking number", "Date In", "Date Out", "Room Type", "Adult Names", "Child Names", "Total Adult", "Total Child", "Swim Passes", "Buffet Passes"};
     System.out.println("Enter new value for " + headers[num] + ": ");
     String newValue = s.nextLine().trim();
     
     updateBook(bookNum, newValue, num);
     
     System.out.println("\n\n\t\t\t\t ________ Successful Update ________");
     // system for reading the update and updating files 
     
     try {
     BufferedReader buffread = new BufferedReader(new FileReader("HotelDatabase.txt"));
     String line;
     while ((line = buffread.readLine()) != null)
     {
    	 String[] parts = line.split("\\|", -1);
    	 if (parts.length > 0 && parts[0].trim().equals(bookNum))
    	 {
    		 System.out.printf("%-9s | %-7s | %-8s | %-9s | %-11s | %-11s | %-11s | %-11s | %-9s | %s\\n", parts);
    		 break;
    	 }
     }
     buffread.close();
	}
	catch (Exception e)
	{
		System.out.println("Error found while showing updated data");
	}
     s.close();
     
     
     
     
     //below is for updating the booking
	}
	
public static void updateBook (String bookNum, String newValue, int columnInd)
{
	try
	{
		
	
	List<String> lines =new ArrayList<>();
	BufferedReader br = new BufferedReader(new FileReader("HotelDatabase.txt"));
	String line;
	while((line = br.readLine()) != null) {
		lines.add(line);
	}
	br.close();
	for(int i = 0; i <lines.size(); i++)
	{
		String CuLine = lines.get(i);
		String[] parts = CuLine.split("\\|", -1);
		
		if (parts.length > 0 && parts[0].trim().equals(bookNum))
		{
			if (columnInd >= 0 && columnInd < parts.length)
			{
				parts[columnInd] = newValue;
				lines.set(i, String.join("|", parts));
			}
			break;
		}
	}
		
		BufferedWriter buffwrite = new BufferedWriter(new FileWriter("HotelDatabase.txt"));
		for(String l : lines)
		{
			buffwrite.write(l);
			buffwrite.newLine();
		}
		buffwrite.close();
	}   
	catch (Exception e)
	{
        System.out.println("Error updating the file: " + e.getMessage());
	}

}
}