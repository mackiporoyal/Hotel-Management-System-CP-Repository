package finals.features.room;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;
import finals.core.ui.UIElement;

public class RoomController {
    
    private RoomAvailability data;
    private RoomDisplay display;

    public RoomController() {
        this.data = new RoomAvailability();
        this.display = new RoomDisplay(this.data);
    }

    private int getValidIntInput(Scanner scanner, String prompt, int min, int max) {
        while(true) {
            System.out.print(prompt);
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input == -1) return -1;
                if (input >= min && input <= max) return input;
                System.out.println("\t\t            [!] Invalid input. Must be between " + min + " and " + max + ".");
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid format. Please enter a number.");
            }
        }
    }

    private void displayCalendar(int year, int month) {
        String border = UIElement.createBorder("═");
        YearMonth ym = YearMonth.of(year, month);
        String monthName = ym.getMonth().toString() + " " + year;

        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow(monthName);
        System.out.println("\t\t╠" + border + "╣");
        
        // Use an explicit, fixed margin spacer string to lock the calendar block to the center
        String calendarLeftMargin = " ".repeat(39); 
        
        // Print the headers row using the explicit left margin structure
        String headerText = "SUN   MON   TUE   WED   THU   FRI   SAT";
        UIElement.printRow(calendarLeftMargin + headerText);
        UIElement.printRow(" ");

        int startDay = ym.atDay(1).getDayOfWeek().getValue() % 7;
        int daysInMonth = ym.lengthOfMonth();

        StringBuilder weekRowBuilder = new StringBuilder();
        
        // Append explicit empty cell blocks for the initial week day offset
        for (int i = 0; i < startDay; i++) {
            weekRowBuilder.append("      ");
        }

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = ym.atDay(day);
            int bookedRooms = data.getOccupiedCount(currentDate);
            
            // Format days cleanly into uniform 6-character column bounds
            String dayCellText = (bookedRooms >= 46) ? " X    " : String.format("%-2d    ", day);
            weekRowBuilder.append(dayCellText);
            
            // Trigger row delivery if it's Saturday or the definitive final day of the month
            if ((startDay + day) % 7 == 0 || day == daysInMonth) {
                // Construct the full string line, combining the hard margin prefix with the week data
                String completeLineText = calendarLeftMargin + weekRowBuilder.toString();
                UIElement.printRow(completeLineText);
                
                // Clear out the buffer string for the next week line
                weekRowBuilder = new StringBuilder();
            }
        }
        System.out.println("\t\t╚" + border + "╝");
    }

    public boolean startMenu(boolean isBookingMode) { return startMenu(isBookingMode, null); }

    public boolean startMenu(boolean isBookingMode, String targetDate) {
        Scanner scanner = new Scanner(System.in);
        String border = UIElement.createBorder("═");

        if (targetDate == null || targetDate.isEmpty()) {
            while (true) {
                System.out.println("\n\t\t╔" + border + "╗");
                UIElement.printCenteredRow(" [ DATE SELECTOR ] ");
                UIElement.printRow(" ");
                System.out.println("\t\t╚" + border + "╝");
                
                int year = getValidIntInput(scanner, "\t\t ► Enter Year (YYYY) : ", 2024, 2100);
                if (year == -1) return false;
                int month = getValidIntInput(scanner, "\t\t ► Enter Month (1-12): ", 1, 12);
                if (month == -1) return false;

                displayCalendar(year, month);

                int maxDays = YearMonth.of(year, month).lengthOfMonth();
                int day = getValidIntInput(scanner, "\t\t ► Select Day (1-" + maxDays + ") : ", 1, maxDays);
                if (day == -1) return false;

                targetDate = String.format("%04d-%02d-%02d", year, month, day);
                break; 
            }
        }

        while (true) {
            data.syncWithDatabase();
            data.syncWithHotelDatabase(targetDate);
            
            System.out.println("\n\t\t╔" + border + "╗");
            UIElement.printRow("ROOM AVAILABILITY");
            System.out.println("\t\t╠" + border + "╣");
            UIElement.printRow(" [ FLOOR SELECTION : " + targetDate + " ] ");
            System.out.println("\t\t╠" + border + "╣");
            UIElement.printRow("          [ 1 ] Change Date");
            UIElement.printRow("          [ 2 ] 2nd Floor (Standard)");
            UIElement.printRow("          [ 3 ] 3rd Floor (Deluxe)");
            UIElement.printRow("          [ 4 ] 4th Floor (Junior Suite)");
            UIElement.printRow("          [ 5 ] 5th Floor (Suite)");
            UIElement.printRow("          [ 6 ] 6th Floor (Penthouse)");
            UIElement.printRow("          [-1 ] Return to Main Menu");
            System.out.println("\t\t╚" + border + "╝");
            
            int choice = getValidIntInput(scanner, "\t\t          ► Enter choice: ", -1, 6);

            if (choice == -1) return false; 
            else if (choice == 1) {
                if (isBookingMode && targetDate != null && targetDate.length() > 0) {
                    System.out.println("\t\t [!] You cannot change the date in New Booking mode.");
                    continue;
                }
                break; 
            }

            int navChoice = 0; 
            switch (choice) {
                case 2: navChoice = display.displayByFloor(RoomAvailability.Floors.SECOND, isBookingMode); break;
                case 3: navChoice = display.displayByFloor(RoomAvailability.Floors.THIRD, isBookingMode); break;
                case 4: navChoice = display.displayByFloor(RoomAvailability.Floors.FOURTH, isBookingMode); break;
                case 5: navChoice = display.displayByFloor(RoomAvailability.Floors.FIFTH, isBookingMode); break;
                case 6: navChoice = display.displayByFloor(RoomAvailability.Floors.SIXTH, isBookingMode); break;
            }

            if (isBookingMode) {
                if (navChoice == 1) return true; 
                else if (navChoice == -1) return false; 
            } else {
                if (navChoice == -1) return false; 
                else if (navChoice >= 201) viewRoomDetails(navChoice);
            }
        }
        return false; 
    }

    private void viewRoomDetails(int roomNumber) {
        Scanner scanner = new Scanner(System.in);
        String border = UIElement.createBorder("═");
        String currentStatus = data.getDetailedStatus(roomNumber);
        String currentType = data.getExactRoomType(roomNumber);
        String currentGuest = data.getGuestName(roomNumber);

        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printCenteredRow("      [ ROOM DETAILS : ROOM " + roomNumber + " ]");
        UIElement.printRow(" ");
        UIElement.printRow(String.format("          Room Status       : %-80s", currentStatus));
        UIElement.printRow(String.format("          Room Type         : %-80s", currentType));
        UIElement.printRow(String.format("          Adult Guest 1     : %-80s", currentGuest));
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Press ENTER to go back...");
        scanner.nextLine();
    }
}