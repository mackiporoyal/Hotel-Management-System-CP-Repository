package finals.Booking.RoomAvailability;

public class RoomDisplay {
    
    private RoomAvailability data;

    public RoomDisplay(RoomAvailability data) {
        this.data = data;
    }

    public void printHeader() {
        System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║                              ROOM AVAILABILITY                               ║");
        System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
    }

    public void displayByFloor(RoomAvailability.Floors floor) {
        switch (floor) {
            case SECOND:
                // Uses getRoomsStandard()
                displayRoomLayout(data.getRoomsStandard(), 201, "STANDARD ROOMS (2ND FLOOR)");
                break;
            case THIRD:
                // Uses getRoomsDeluxe()
                displayRoomLayout(data.getRoomsDeluxe(), 301, "DELUXE ROOMS (3RD FLOOR)");
                break;
            case FOURTH:
                displayRoomLayout(data.getRoomsJrSuite(), 401, "JUNIOR SUITE ROOMS (4TH FLOOR)");
                break;
            case FIFTH:
                displayRoomLayout(data.getRoomsSuite(), 501, "SUITE ROOMS (5TH FLOOR)");
                break;
            case SIXTH:
                displayRoomLayout(data.getRoomsPentHouse(), 601, "PENTHOUSE ROOMS (6TH FLOOR)");
                break;
            default:
                System.out.println("\t\t[!] No rooms available on this floor (Lobby/Amenities).");
                break;
        }
    }



		 private void displayRoomLayout(char[][] roomLayout, int startRoomNum, String roomTypeName) {
		     if (roomLayout == null || roomLayout.length == 0) {
		         System.out.println("\t[!] No layout to display.");
		         return;
		     }
		
		     int currentRoomNumber = startRoomNum;
		     final int INNER_WIDTH = 89;
		
		     System.out.println("\t╔" + "═".repeat(INNER_WIDTH) + "╗");
		     printCenteredLine(" " + roomTypeName.toUpperCase() + " ROOM AVAILABILITY ", INNER_WIDTH);
		     System.out.println("\t╠" + "═".repeat(INNER_WIDTH) + "╣");
		
		     int lastVisibleRow = -1;
		     for (int i = 0; i < roomLayout.length; i++) {
		         if (roomLayout[i] != null && roomLayout[i].length > 0 && roomLayout[i][0] != '■') {
		             lastVisibleRow = i;
		         }
		     }
		
		     for (int row = 0; row < roomLayout.length; row++) {
		         char[] colsArr = roomLayout[row];
		         if (colsArr == null || colsArr.length == 0 || colsArr[0] == '■') continue;
		
		         int cols = colsArr.length;
		         int base = 88 / cols;
		         int rem = 88 % cols;
		         int[] widths = new int[cols];
		         for (int c = 0; c < cols; c++) widths[c] = base + (c < rem ? 1 : 0);
		
		         StringBuilder roomNumbersRow = new StringBuilder();
		         StringBuilder statusRow = new StringBuilder();
		         StringBuilder hallwayDivider = new StringBuilder();
		
		         for (int col = 0; col < cols; col++) {
		             int w = widths[col];
		             String roomName = "Room " + (currentRoomNumber + col);
		             String roomStatus = "[" + colsArr[col] + "]";
		
		             roomNumbersRow.append(formatCell(roomName, w));
		             statusRow.append(formatCell(roomStatus, w));
		             hallwayDivider.append("┼").append("═".repeat(Math.max(0, w - 1)));
		         }
		
		         roomNumbersRow.append("│");
		         statusRow.append("│");
		         hallwayDivider.append("┼");
		
		         printCenteredLine(roomNumbersRow.toString(), INNER_WIDTH);
		         printCenteredLine(statusRow.toString(), INNER_WIDTH);
		
		         if (row < lastVisibleRow) {
		             printCenteredLine(hallwayDivider.toString(), INNER_WIDTH);
		             String hallwaySign = "H A L L W A Y              H A L L W A Y              H A L L W A Y";
		             printCenteredLine(hallwaySign, INNER_WIDTH);
		             printCenteredLine(hallwayDivider.toString(), INNER_WIDTH);
		         }
		
		         currentRoomNumber += cols;
		     }
		
		     System.out.println("\t╚" + "═".repeat(INNER_WIDTH) + "╝");
		 }
		
		 private String formatCell(String text, int totalCellWidth) {
		     if (text == null) text = "";
		     int usable = Math.max(1, totalCellWidth - 1);
		     if (text.length() > usable) text = text.substring(0, usable);
		     int spaceToFill = usable - text.length();
		     int leftPadding = spaceToFill / 2;
		     int rightPadding = spaceToFill - leftPadding;
		     return "│" + " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
		 }
		
		 private void printCenteredLine(String text, int totalWidth) {
		     if (text == null) text = "";
		     if (text.length() > totalWidth) text = text.substring(0, totalWidth);
		     int emptySpace = totalWidth - text.length();
		     int leftSpaces = emptySpace / 2;
		     int rightSpaces = emptySpace - leftSpaces;
		     System.out.println("\t║" + " ".repeat(leftSpaces) + text + " ".repeat(rightSpaces) + "║");
		 }

}