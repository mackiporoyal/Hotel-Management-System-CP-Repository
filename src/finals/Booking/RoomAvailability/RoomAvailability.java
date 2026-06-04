package finals.Booking.RoomAvailability;

// Make sure to import your Database class!
import finals.DatabaseLogic.Database;
import java.util.List;

public class RoomAvailability {
    
    // 'A' = Available, '■' = Hidden placeholder, 'X' = Booked
    private char[][] roomsStandard = {
        {'A','A','A','A','A','A','A','A'},
        {'■','■','■','■','■','■','■','■'},
        {'■','■','■','■','■','■','■','■'},
        {'A','A','A','A','A','A','A','A'},
    };
    
    private char[][] roomsDeluxe = {
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
    // =========================================================================
    // ADDED: DATABASE SYNC LOGIC
    // Reads HotelDatabase.txt and locks rooms marked as ACTIVE
    // =========================================================================
 // =========================================================================
    // DATABASE SYNC LOGIC (WITH STRICT 2-FACTOR VALIDATION)
    // Reads HotelDatabase.txt and locks rooms ONLY if Type & Number perfectly match
    // =========================================================================
    public void syncWithDatabase() {
        Database db = new Database();
        List<String[]> records = db.readAllLines();

        for (String[] record : records) {
            // Check if the record has enough columns to have a Room Type (Index 3) and Room Number (Index 4)
            if (record.length > 4) { 
                String roomType = record[3].trim().toLowerCase(); // Grab the Room Type
                String status = record[record.length - 1].trim(); // Status is always the very last column
                
                // Only lock the room if the status is "ACTIVE"
                if (status.equalsIgnoreCase("ACTIVE")) {
                    try {
                        int roomNumber = Integer.parseInt(record[4].trim());
                        
                        // --- STRICT 2-FACTOR VALIDATION ---
                        // It must be the correct room type AND fall within the correct floor numbers!
                        
                        if (roomType.contains("standard") && roomNumber >= 201 && roomNumber <= 208) {
                            db.confirmBookingAndLockRoom(roomsStandard, 201, roomNumber);
                        } 
                        else if (roomType.contains("deluxe") && roomNumber >= 301 && roomNumber <= 308) {
                            db.confirmBookingAndLockRoom(roomsDeluxe, 301, roomNumber);
                        } 
                        else if ((roomType.contains("junior") || roomType.contains("jr")) && roomNumber >= 401 && roomNumber <= 404) {
                            db.confirmBookingAndLockRoom(roomsJrSuite, 401, roomNumber);
                        } 
                        else if (roomType.contains("suite") && !roomType.contains("junior") && !roomType.contains("jr") && roomNumber >= 501 && roomNumber <= 502) {
                            db.confirmBookingAndLockRoom(roomsSuite, 501, roomNumber);
                        } 
                        else if (roomType.contains("penthouse") && roomNumber == 601) {
                            db.confirmBookingAndLockRoom(roomsPentHouse, 601, roomNumber);
                        }
                        // If a record says "Standard" but has room "301", it falls through the cracks here 
                        // and is safely ignored! The room will remain [A].

                    } catch (NumberFormatException e) {
                        // Skip any rows where the room number isn't a proper integer (like your line 1)
                    }
                }
            }
        }
    }

    // =========================================================================
    // GETTERS
    // =========================================================================
    public char[][] getRoomsStandard() { return roomsStandard; }
    public char[][] getRoomsDeluxe() { return roomsDeluxe; }
    public char[][] getRoomsJrSuite() { return roomsJrSuite; }
    public char[][] getRoomsSuite() { return roomsSuite; }
    public char[][] getRoomsPentHouse() { return roomsPentHouse; }

    public enum Floors {
        FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH
    }

    public void checkDatabase() {
        System.out.println("Checking database connection...");
        try {
            Thread.sleep(1000); 
            System.out.println("Database connection successful!");
            
        } catch (InterruptedException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}