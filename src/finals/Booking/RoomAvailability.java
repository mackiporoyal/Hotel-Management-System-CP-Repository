package finals.Booking;

import java.util.Scanner;
import finals.Booking.RoomAvailabilityLogic.RoomAvailabilityLogic;

// FIX 1: Removed "extends RoomAvailabilityLogic"
public class RoomAvailability {
    
    // 'A' = Available, '■' = Hidden placeholder, 'X' = Booked
    private char[][] roomsStandardDeluxe = {
        {'A','A','A','A','A','A','A','A'},
        {'■','■','■','■','■','■','■','■'},
        {'■','■','■','■','■','■','■','■'},
        {'A','A','A','A','A','A','A','A'},
    };
    
    private char[][] roomsJrSuite = {
        {'A','A','A','A'},
        {'■','■','■','■'},
        {'■','■','■','■'},
        {'A','A','A','A'},
    };
    
    private char[][] roomsSuite = {
        {'A','A'},
        {'■','■'},
        {'■','■'},
        {'A','A'},
    };
    
    private char[][] roomsPentHouse = {
        {'A'},
        {'■'},
        {'■'},
        {'A'},
    };

    public char[][] getRoomsStandardDeluxe() { return roomsStandardDeluxe; }
    public char[][] getRoomsJrSuite() { return roomsJrSuite; }
    public char[][] getRoomsSuite() { return roomsSuite; }
    public char[][] getRoomsPentHouse() { return roomsPentHouse; }

    public enum Floors {
        FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH
    }

    // ========================================================
    // MENU LOOP
    // ========================================================
    public void getRoomAvailability() {
        Scanner scanner = new Scanner(System.in);
        
        // FIX 2: Create the worker right here! 
        // We pass 'this' into the parentheses, which tells the Logic class: 
        // "Here is the data! Use the arrays inside this very file!"
        RoomAvailabilityLogic logic = new RoomAvailabilityLogic(this);

        boolean running = true;

        while (running) {
            // FIX 3: Use 'logic.' instead of 'this.' to call the drawing methods
            logic.printHeader();
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
                    logic.displayByFloor(RoomAvailability.Floors.SECOND);
                    break;
                case 3:
                    logic.displayByFloor(RoomAvailability.Floors.THIRD);
                    break;
                case 4:
                    logic.displayByFloor(RoomAvailability.Floors.FOURTH);
                    break;
                case 5:
                    logic.displayByFloor(RoomAvailability.Floors.FIFTH);
                    break;
                case 6:
                    logic.displayByFloor(RoomAvailability.Floors.SIXTH);
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