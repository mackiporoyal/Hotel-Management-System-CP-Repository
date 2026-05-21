package finals;

public class RoomAvailability {
    private char[][] rooms = {
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
        rooms[0][2] = 'X'; // Example modification
        for (int z = 0; z < 4; z++) {
            System.out.print("\t\t");
            for (int x = 0; x < 8; x++) {
                System.out.print("\t    [" + rooms[z][x] + "]");
            }
            System.out.println();
        }
    }

    public enum Floors {
        FIRST, SECOND, THIRD, FOURTH
    }
}
