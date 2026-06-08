package finals.Booking.RoomAvailability;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;

public class RoomController {
    
    private finals.Booking.RoomAvailability.RoomAvailability data;
    private RoomDisplay display;

    public RoomController() {
        this.data = new finals.Booking.RoomAvailability.RoomAvailability();
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

    private void printCenteredLine(String text, int totalWidth) {
        int leftSpaces = (totalWidth - text.length()) / 2;
        int rightSpaces = totalWidth - text.length() - leftSpaces;
        System.out.println("\t\t║" + " ".repeat(Math.max(0, leftSpaces)) + text + " ".repeat(Math.max(0, rightSpaces)) + "║");
    }

    private void displayCalendar(int year, int month) {
        String border = "═".repeat(120);
        YearMonth ym = YearMonth.of(year, month);
        String monthName = ym.getMonth().toString() + " " + year;

        System.out.println("\n\t\t╔" + border + "╗");
        printCenteredLine(monthName, 120);
        System.out.println("\t\t╠" + border + "╣");
        
        String header = " SUN   MON   TUE   WED   THU   FRI   SAT  ";
        printCenteredLine(header, 120);
        System.out.println("\t\t║" + " ".repeat(120) + "║");

        int startDay = ym.atDay(1).getDayOfWeek().getValue() % 7;
        int daysInMonth = ym.lengthOfMonth();

        StringBuilder row = new StringBuilder();
        for (int i = 0; i < startDay; i++) row.append("      ");

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = ym.atDay(day);
            int bookedRooms = data.getOccupiedCount(currentDate);
            String dayStr = (bookedRooms >= 46) ? "  X   " : String.format("%3d   ", day);
            row.append(dayStr);
            if ((startDay + day) % 7 == 0 || day == daysInMonth) {
                while(row.length() < 42) row.append(" ");
                printCenteredLine(row.toString(), 120);
                row = new StringBuilder();
            }
        }
        System.out.println("\t\t╚" + border + "╝");
    }

    private void printRow(String content) {
        int contentWidth = content.length();
        int spacesNeeded = Math.max(0, 120 - contentWidth);
        System.out.println("\t\t║" + content + " ".repeat(spacesNeeded) + "║");
    }

    // --- OVERLOADED METHODS ---
    public boolean startMenu(boolean isBookingMode) {
        return startMenu(isBookingMode, null);
    }

    public boolean startMenu(boolean isBookingMode, String targetDate) {
        Scanner scanner = new Scanner(System.in);
        String border = "═".repeat(120);

        if (targetDate == null || targetDate.isEmpty()) {
            String empty = " ".repeat(120);
            while (true) {
                System.out.println("\n\t\t╔" + border + "╗");
                printCenteredLine(" [ DATE SELECTOR ] ", 120);
                System.out.println("\t\t║" + empty + "║");
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
            
            System.out.println("\n\t\t╔" + "═".repeat(120) + "╗");
            printRow("ROOM AVAILABILITY");
            System.out.println("\t\t╠" + "═".repeat(120) + "╣");
            printRow(" [ FLOOR SELECTION : " + targetDate + " ] ");
            System.out.println("\t\t╠" + "═".repeat(120) + "╣");
            printRow("          [ 1 ] Change Date");
            printRow("          [ 2 ] 2nd Floor (Standard)");
            printRow("          [ 3 ] 3rd Floor (Deluxe)");
            printRow("          [ 4 ] 4th Floor (Junior Suite)");
            printRow("          [ 5 ] 5th Floor (Suite)");
            printRow("          [ 6 ] 6th Floor (Penthouse)");
            printRow("          [-1 ] Return to Main Menu");
            System.out.println("\t\t╚" + "═".repeat(120) + "╝");
            
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
                case 2: navChoice = display.displayByFloor(finals.Booking.RoomAvailability.RoomAvailability.Floors.SECOND, isBookingMode); break;
                case 3: navChoice = display.displayByFloor(finals.Booking.RoomAvailability.RoomAvailability.Floors.THIRD, isBookingMode); break;
                case 4: navChoice = display.displayByFloor(finals.Booking.RoomAvailability.RoomAvailability.Floors.FOURTH, isBookingMode); break;
                case 5: navChoice = display.displayByFloor(finals.Booking.RoomAvailability.RoomAvailability.Floors.FIFTH, isBookingMode); break;
                case 6: navChoice = display.displayByFloor(finals.Booking.RoomAvailability.RoomAvailability.Floors.SIXTH, isBookingMode); break;
            }

            if (isBookingMode) {
                if (navChoice == 1) return true; 
                else if (navChoice == -1) return false; 
            } else {
                if (navChoice == -1) return false; 
                else if (navChoice >= 201) viewRoomDetails(navChoice);
            }
        }
        return false; // Safety return added here to fix the compilation error
    }

    private void viewRoomDetails(int roomNumber) {
        Scanner scanner = new Scanner(System.in);
        String border = "═".repeat(120);
        String empty = " ".repeat(120);
        String currentStatus = data.getDetailedStatus(roomNumber);
        String currentType = data.getExactRoomType(roomNumber);
        String currentGuest = data.getGuestName(roomNumber);

        System.out.println("\n\t\t╔" + border + "╗");
        printCenteredLine("      [ ROOM DETAILS : ROOM " + roomNumber + " ]", 120);
        System.out.println("\t\t║" + empty + "║");
        System.out.println("\t\t║          Room Status       : " + currentStatus + " ".repeat(80) + "║");
        System.out.println("\t\t║          Room Type         : " + currentType + " ".repeat(80) + "║");
        System.out.println("\t\t║          Adult Guest 1     : " + currentGuest + " ".repeat(80) + "║");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Press ENTER to go back...");
        scanner.nextLine();
    }
}