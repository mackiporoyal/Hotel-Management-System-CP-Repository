package finals.DatabaseLogic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime; // Imported for checking current time
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database {
	
	private int adultGuest;
	private String timeIn;
	private String timeOut;
	private String roomType;
	private String adultNames;
	private String childNames;
	private int totalAdult;
	private int totalChild;
	private int swimPasses;
	private int buffetPasses;
	private String status; // Added status field
	
	Path storageRPath = Paths.get("HotelDatabase.txt");
	Path absolutePath = storageRPath.toAbsolutePath();

	List<String[]> lines = new ArrayList<>();
	String[] lastLine;
	
	private void readAllLine(boolean allLine) {
		
		try (BufferedReader br = new BufferedReader(new FileReader(absolutePath.toFile()))) {
			String currentLine;
			String[] line = null;
			
			while ((currentLine = br.readLine()) != null) {
				line = currentLine.split("\\|",-1);
				if(allLine == true) {
					lines.add(line);	
				}else if(allLine == false){ 
			        lastLine = line;	
				}	
			}
		}
		catch(Exception e) {}
	}
	
	
	private void displayRoom() {
		
	}
	
	private void calendarToRoom(int status){
		
		
	}
	
	public void roomToCalendar(){
		
	}
	
	private static void printCalendar(int year, int month, List<Integer> bookedDays) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        
        // getDayOfWeek().getValue() returns 1 (Monday) to 7 (Sunday). 
        // We do % 7 to make Sunday = 0, Monday = 1, etc., for our grid.
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7; 
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        System.out.println("\t\t\t\tSun   Mon   Tue   Wed   Thu   Fri   Sat");
        System.out.print("\t\t\t\t");

        // Print empty spaces for days before the 1st of the month
        for (int i = 0; i < startDayOfWeek; i++) {
            System.out.print("      ");
        }

        // Print the days
        for (int day = 1; day <= daysInMonth; day++) {
            if (bookedDays.contains(day)) {
                System.out.print(" XX   "); // Mark as booked
            } else {
                System.out.printf("%3d   ", day); // Print normal day
            }

            // If we reach Saturday (index 6), start a new line
            if ((day + startDayOfWeek) % 7 == 0) {
                System.out.println();
                System.out.print("\t\t\t\t");
            }
        }
        System.out.println();
    }

	
	public void writeLine(String timeIn, String timeOut, String roomType, ArrayList<String> adultNames, ArrayList<String> childNames, int totalAdult, int totalChild, int swimPasses, int buffetPasses) 
	{
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.roomType = roomType;
		this.adultNames = String.join(", ", adultNames);
		this.childNames = String.join(", ", childNames);
		this.totalAdult = totalAdult;
		this.totalChild = totalChild;
		this.swimPasses = swimPasses;
		this.buffetPasses = buffetPasses;
		this.status = "ACTIVE";
		
		int num = 10000;
		this.readAllLine(false);
		
		
		if (Files.exists(absolutePath) && !lines.isEmpty()) {
			
		    String[] lastLine = lines.get(lines.size() - 1);
		    
		    String bookingNumberStr = lastLine[0];

		    num = Integer.parseInt(bookingNumberStr);
		    num++;
		}
		String toDatabase = num + "|" + this.timeIn  + "|" + this.timeOut + "|" + this.roomType + "|" + this.adultNames + "|" + this.childNames + "|" + this.totalAdult + "|" + this.totalChild + "|" + this.swimPasses + "|" + this.buffetPasses + "|" + this.status + "\n";
			
			try (BufferedWriter writer1 = new BufferedWriter(new FileWriter(storageRPath.toString(), true))) {
				writer1.write(toDatabase);
				
				// Rebuild the line
				updatedLines.add(String.join("|", tokens));
			}
		
		catch (Exception e) 
		{
			System.err.println("Database write operation failed!");
			e.printStackTrace();
		}
			
	}	
	
	
	
	public void readDatabase(int num, String input) {
		boolean exists = false;
		try (BufferedReader br = new BufferedReader(new FileReader(absolutePath.toFile()))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					String[] column = line.split("\\|", -1);
					
					if (column.length >= 10) {
						if (num == 1) {
							displayCheckBooking(column);
							exists = true;
						} else if (num == 2 || num == 3) {
							int col = (num == 2) ? 0 : 4; 
							
							if (column[col].toLowerCase().contains(input.toLowerCase())) {
								displayCheckBooking(column);
								exists = true;
							}
						}
					} 
				}
			}
			
			if (!exists && num != 1) {
				System.out.println("\t\t\t\t\t No matches found for: " + input);
			}
			
		} catch (IOException e) {
			System.out.println("\t\t\t\t\t Error reading database: " + e.getMessage());
		}
	}
	
	

	private void displayCheckBooking(String[] column) {
		System.out.println("\t\t\t\t--------------------------------------------------");
		System.out.println("\t\t\t\t Booking ID: " + column[0]);
		System.out.println("\t\t\t\t Check-in:  " + column[1] + "  |  Check-out: " + column[2]);
		System.out.println("\t\t\t\t Room Type: " + column[3]);
		System.out.println("\t\t\t\t Adults:    " + column[4]);
		if (!column[5].isEmpty() && !column[5].equalsIgnoreCase("none")) {
			System.out.println("\t\t\t\t Children:  " + column[5]);
		}
		System.out.println("\t\t\t\t Passes:    Pool (" + column[8] + ") | Buffet (" + column[9] + ")");
		System.out.println("\t\t\t\t--------------------------------------------------\n");
	}

	
	public void displayDatabase() {
		String rows;
		try (BufferedReader read = new BufferedReader(new FileReader(absolutePath.toFile()))) {
			this.readAllLine(true);
		    while ((rows = read.readLine()) != null) {
		        String[] columns = rows.split("\\|");					
		        lines.add(columns);
		        
		        int numColumns = lines.get(0).length;
				int[] colWidths = new int[numColumns];
				for(String[] lines: lines) {
					for(int i = 0; i<lines.length; i++) {
						if (i < 	numColumns && lines.length > colWidths[i]) {
							colWidths[i] = lines[i].length();
						}
					}
				}
		        
		        System.out.println(columns[3]);	
		    }
		    
		} catch (IOException e) {
		    e.printStackTrace(); // This prints the error if the file doesn't exist
		}
	}
}