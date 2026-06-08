package finals.Booking.RoomAvailability;

import finals.DatabaseLogic.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.List;

public class RoomAvailability {
    private String[][] roomsStandard = {
        {"S","S","S","S","S","S","S","S"},
        {"■","■","■","■","■","■","■","■"},
        {"■","■","■","■","■","■","■","■"},
        {"S","S","S","S","S","S","S","S"},
    };
    
    private String[][] roomsDeluxe = {
        {"S","S","S","S","S","S","S","S"},
        {"■","■","■","■","■","■","■","■"},
        {"■","■","■","■","■","■","■","■"},
        {"S","S","S","S","S","S","S","S"},
    };
    
    private String[][] roomsJrSuite = {
        {"S","S","S","S"},
        {"■","■","■","■"},
        {"■","■","■","■"},
        {"S","S","S","S"},
    };
    
    private String[][] roomsSuite = {
        {"S","S"},
        {"■","■"},
        {"■","■"},
        {"S","S"},
    };
    
    private String[][] roomsPentHouse = {
        {"S"},
        {"■"},
        {"■"},
        {"S"},
    };

    // Dynamic database folder base path compilation
    private final String dbFolder = System.getProperty("user.dir") + File.separator + "src" + 
                                   File.separator + "finals" + File.separator + "DatabaseLogic" + File.separator;
    private final String hotelDbPath = dbFolder + "HotelDatabase.txt";

    public RoomAvailability() {
        syncWithDatabase();
    }

    public void syncWithDatabase() {
        Database db = new Database();
        List<String[]> records = db.readRoomDatabase(); 

        for (String[] record : records) {
            if (record.length >= 3) { 
                try {
                    int roomNumber = Integer.parseInt(record[0].trim());
                    String roomType = record[1].trim().toLowerCase();
                    String status = record[2].trim().toUpperCase(); 
                    
                    if (roomType.contains("standard") && roomNumber >= 201 && roomNumber <= 216) {
                        db.confirmBookingAndLockRoom(roomsStandard, 201, roomNumber, status);
                    } 
                    else if (roomType.contains("deluxe") && roomNumber >= 301 && roomNumber <= 316) {
                        db.confirmBookingAndLockRoom(roomsDeluxe, 301, roomNumber, status);
                    } 
                    else if ((roomType.contains("junior") || roomType.contains("jr")) && roomNumber >= 401 && roomNumber <= 408) {
                        db.confirmBookingAndLockRoom(roomsJrSuite, 401, roomNumber, status);
                    } 
                    else if (roomType.contains("suite") && !roomType.contains("junior") && !roomType.contains("jr") && roomNumber >= 501 && roomNumber <= 504) {
                        db.confirmBookingAndLockRoom(roomsSuite, 501, roomNumber, status);
                    } 
                    else if (roomType.contains("penthouse") && roomNumber >= 601 && roomNumber <= 602) {
                        db.confirmBookingAndLockRoom(roomsPentHouse, 601, roomNumber, status);
                    }
                } catch (NumberFormatException e) {}
            }
        }
    }

    public int getOccupiedCount(LocalDate targetDate) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(hotelDbPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains("booking")) {
                    String[] record = line.split(",");
                    if (record.length >= 4) {
                        try {
                            LocalDate checkIn = LocalDate.parse(record[0].trim());
                            LocalDate checkOut = LocalDate.parse(record[1].trim());

                            if (!targetDate.isBefore(checkIn) && targetDate.isBefore(checkOut)) {
                                count++;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        } catch (Exception e) {}

        return count;
    }

    public void syncWithHotelDatabase(String targetDateStr) {
        LocalDate targetDate = LocalDate.parse(targetDateStr);

        try (BufferedReader br = new BufferedReader(new FileReader(hotelDbPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = line.split("\\|"); 
                
                if (record.length >= 13) { 
                    try {
                        LocalDate checkIn = LocalDate.parse(record[1].trim());
                        LocalDate checkOut = LocalDate.parse(record[2].trim());

                        if (!targetDate.isBefore(checkIn) && targetDate.isBefore(checkOut)) {
                            int roomNumber = Integer.parseInt(record[4].trim());

                            if (roomNumber >= 201 && roomNumber <= 602) {
                                String paymentStatus = record[11].trim().toUpperCase(); 
                                String newStatus = "OD"; 

                                if (paymentStatus.equals("FULLY_PAID")) {
                                    newStatus = "OC"; 
                                } else if (paymentStatus.equals("DOWNPAYMENT")) {
                                    newStatus = "OR"; 
                                } else if (paymentStatus.equals("UNPAID")) {
                                    newStatus = "OD";
                                }

                                if (roomNumber >= 201 && roomNumber <= 216) updateLocalRoomArray(roomsStandard, 201, roomNumber, newStatus);
                                else if (roomNumber >= 301 && roomNumber <= 316) updateLocalRoomArray(roomsDeluxe, 301, roomNumber, newStatus);
                                else if (roomNumber >= 401 && roomNumber <= 408) updateLocalRoomArray(roomsJrSuite, 401, roomNumber, newStatus);
                                else if (roomNumber >= 501 && roomNumber <= 504) updateLocalRoomArray(roomsSuite, 501, roomNumber, newStatus);
                                else if (roomNumber >= 601 && roomNumber <= 602) updateLocalRoomArray(roomsPentHouse, 601, roomNumber, newStatus);
                            }
                        }
                    } catch (Exception e) {}
                }
            }
        } catch (Exception e) {}
    }

    private void updateLocalRoomArray(String[][] floorArray, int baseRoomNum, int targetRoomNum, String newStatus) {
        int index = targetRoomNum - baseRoomNum;
        int rowLength = floorArray[0].length;
        
        if (index < rowLength) {
            floorArray[0][index] = newStatus;
        } else {
            floorArray[3][index - rowLength] = newStatus;
        }
    }

    public String getExactRoomType(int roomNumber) {
        if (roomNumber >= 201 && roomNumber <= 216) return "Standard";
        if (roomNumber >= 301 && roomNumber <= 316) return "Deluxe";
        if (roomNumber >= 401 && roomNumber <= 408) return "Junior Suite";
        if (roomNumber >= 501 && roomNumber <= 504) return "Suite";
        if (roomNumber >= 601 && roomNumber <= 602) return "Penthouse";
        return "Unknown";
    }

    public String getDetailedStatus(int roomNumber) {
        String code = "VR"; 
        try {
            if (roomNumber >= 201 && roomNumber <= 216) {
                code = (roomNumber <= 208) ? roomsStandard[0][roomNumber - 201] : roomsStandard[3][roomNumber - 209];
            } else if (roomNumber >= 301 && roomNumber <= 316) {
                code = (roomNumber <= 308) ? roomsDeluxe[0][roomNumber - 301] : roomsDeluxe[3][roomNumber - 309];
            } else if (roomNumber >= 401 && roomNumber <= 408) {
                code = (roomNumber <= 404) ? roomsJrSuite[0][roomNumber - 401] : roomsJrSuite[3][roomNumber - 405];
            } else if (roomNumber >= 501 && roomNumber <= 504) {
                code = (roomNumber <= 502) ? roomsSuite[0][roomNumber - 501] : roomsSuite[3][roomNumber - 503];
            } else if (roomNumber >= 601 && roomNumber <= 602) {
                code = (roomNumber == 601) ? roomsPentHouse[0][0] : roomsPentHouse[3][0];
            }
        } catch (Exception e) {}

        if (code.equals("S") || code.equals("VR")) return "Vacant / Ready";
        if (code.equals("VD")) return "Vacant / Dirty";
        if (code.equals("OC")) return "Occupied / Clean";
        if (code.equals("OD")) return "Occupied / Dirty";
        if (code.equals("OR")) return "Occupied / Reserved"; 
        if (code.equals("OOS")) return "Out of Service";
        if (code.equals("OOO")) return "Out of Order";
        
        return "Unknown (" + code + ")";
    }

    public String getGuestName(int roomNumber) {
        Database db = new Database();
        List<String[]> records = db.readRoomDatabase(); 
        for (String[] record : records) {
            if (record.length >= 4) { 
                try {
                    int dbRoomNum = Integer.parseInt(record[0].trim());
                    if (dbRoomNum == roomNumber) {
                        return record[3].trim(); 
                    }
                } catch (NumberFormatException e) {}
            }
        }
        return "N/A (No Guest)";
    }

    public String[][] getRoomsStandard() { return roomsStandard; }
    public String[][] getRoomsDeluxe() { return roomsDeluxe; }
    public String[][] getRoomsJrSuite() { return roomsJrSuite; }
    public String[][] getRoomsSuite() { return roomsSuite; }
    public String[][] getRoomsPentHouse() { return roomsPentHouse; }

    public enum Floors { FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH }
}