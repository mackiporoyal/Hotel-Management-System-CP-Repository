package finals;

public class RoomAvailability {
    private char[][] roomsStandardDeluxe = {
        {'A','A','A','A','A','A','A','A'},
        {'■','■','■','■','■','■','■','■'},
        {'■','■','■','■','■','■','■','■'},
        {'A','A','A','A','A','A','A','A'},
    };

    public void printHeader() {
        System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                             ROOM AVAILABILITY                                ║");
        System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
    }

    public void displayRoom() {
        int roomNumber = 201;

        // Loop through each row
        for (int z = 0; z < this.roomsStandardDeluxe.length; z++) {
            
            // --- LINE 1: Print the Room Numbers ---
            System.out.print("\t\t");
            for (int x = 0; x < this.roomsStandardDeluxe[z].length; x++) {
                char status = roomsStandardDeluxe[z][x];
                
                if (status == '■') {
                    System.out.print("      "); // 6 spaces instead of a number
                } else {
                    // %-6d means "print the number and pad it to be 6 characters wide"
                    System.out.printf("%-6d", (roomNumber + x)); 
                }
            }
            System.out.println(); // Move to the next line

            // --- LINE 2: Print the Room Status Boxes ---
            System.out.print("\t\t");
            for (int x = 0; x < this.roomsStandardDeluxe[z].length; x++) {
                // Print the bracket, the status character, and enough spaces to match the 6-character width above
                System.out.print("[" + roomsStandardDeluxe[z][x] + "]   ");
            }
            
            System.out.println("\n"); // Double enter to separate this row from the next row
        }
    }

    public enum Floors {
    	//FIRST FLOOR: LOBBY, CAFETERIA, SWIMMING POOL
    	//2ND: STANDARD: 16 ROOMS
    	//3RD: DELUXE: 16 ROOMS
    	//4TH: JR. SUITE: 8 ROOMS
    	//5TH: SUITE: 4 ROOMS
    	//6TH: PENTHOUSE: 2 ROOMS
 
    	
        FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH
    }
}
