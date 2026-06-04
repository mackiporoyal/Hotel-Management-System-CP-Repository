package finals.Booking.RoomAvailability;
import finals.Booking.RoomAvailability.RoomDisplay;
import java.util.Scanner;

public class RoomController {
    
    // The controller holds both the data and the display tools
    private RoomAvailability data;
    private RoomDisplay display;

    public RoomController() {
        // Initialize the data, then pass it to the display class
        this.data = new RoomAvailability();
        this.display = new RoomDisplay(this.data);
    }

    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        data.syncWithDatabase();
        while (running) {
            display.printHeader();
            System.out.println("\t\tSelect a floor to view availability:");
            System.out.println("\t\t[2] 2nd Floor (Standard Rooms)");
            System.out.println("\t\t[3] 3rd Floor (Deluxe Rooms)");
            System.out.println("\t\t[4] 4th Floor (Junior Suites)");
            System.out.println("\t\t[5] 5th Floor (Suites)");
            System.out.println("\t\t[6] 6th Floor (Penthouse)");
            System.out.println("\t\t[-1] to return to main menu ");
            System.out.print("\t\tEnter choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 2:
                    display.displayByFloor(RoomAvailability.Floors.SECOND);
                    break;
                case 3:
                    display.displayByFloor(RoomAvailability.Floors.THIRD);
                    break;
                case 4:
                    display.displayByFloor(RoomAvailability.Floors.FOURTH);
                    break;
                case 5:
                    display.displayByFloor(RoomAvailability.Floors.FIFTH);
                    break;
                case 6:
                    display.displayByFloor(RoomAvailability.Floors.SIXTH);
                    break;
                case -1:
                    running = false;
                    break;
                default:
                    System.out.println("\t\tInvalid floor choice. Please choose 2-6, or -1.");
                    break;
            }
        }
        
        System.out.println("\t\tReturning to main menu...");
    }
}