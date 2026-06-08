package finals.Booking.RoomAvailability;

import java.util.Scanner;

public class RoomDisplay {
    
    private finals.Booking.RoomAvailability.RoomAvailability data;

    public enum StatusLegend {
        VR("[VR]", "Vacant/Ready", "ready for occupation of next guest/s"),
        VD("[VD]", "Vacant/Dirty", "needs cleaning services"),
        OC("[OC]", "Occupied/Clean", "no need for cleaning services (until guests request for one)"),
        OD("[OD]", "Occupied/Dirty", "guest requested for cleaning services"),
        OOS("[OOS]", "Out of Service", "needs minor fixing, kahit may sira pwede occupy ng guests if overbooked"),
        OOO("[OOO]", "Out of Order", "needs major fixing, unbookable");

        private String code;
        private String name;
        private String desc;

        StatusLegend(String code, String name, String desc) {
            this.code = code;
            this.name = name;
            this.desc = desc;
        }

        public String getFullText() {
            String codePad = code + " ".repeat(6 - code.length());
            String namePad = name + " ".repeat(16 - name.length());
            return codePad + namePad + "- " + desc;
        }
    }

    public RoomDisplay(finals.Booking.RoomAvailability.RoomAvailability data) {
        this.data = data;
    }

    public void printHeader() {
        String border = "═".repeat(120);
        System.out.println("\n\t\t╔" + border + "╗");
        System.out.println("\t\t║" + " ".repeat(120) + "║");
        
        String title = "ROOM AVAILABILITY";
        int leftPad = (120 - title.length()) / 2;
        int rightPad = 120 - title.length() - leftPad;
        
        System.out.println("\t\t║" + " ".repeat(leftPad) + title + " ".repeat(rightPad) + "║");
        System.out.println("\t\t║" + " ".repeat(120) + "║");
        System.out.println("\t\t╚" + border + "╝");
    }

    public int displayByFloor(finals.Booking.RoomAvailability.RoomAvailability.Floors floor, boolean isBookingMode) {
        switch (floor) {
            case SECOND:
                displayRoomLayout(data.getRoomsStandard(), 201, "STANDARD ROOMS (2ND FLOOR)", isBookingMode);
                break;
            case THIRD:
                displayRoomLayout(data.getRoomsDeluxe(), 301, "DELUXE ROOMS (3RD FLOOR)", isBookingMode);
                break;
            case FOURTH:
                displayRoomLayout(data.getRoomsJrSuite(), 401, "JUNIOR SUITE ROOMS (4TH FLOOR)", isBookingMode);
                break;
            case FIFTH:
                displayRoomLayout(data.getRoomsSuite(), 501, "SUITE ROOMS (5TH FLOOR)", isBookingMode);
                break;
            case SIXTH:
                displayRoomLayout(data.getRoomsPentHouse(), 601, "PENTHOUSE ROOMS (6TH FLOOR)", isBookingMode);
                break;
            default:
                System.out.println("\t\t  [!] No rooms available on this floor (Lobby/Amenities).");
                break;
        }
        
        return promptUserNavigation(isBookingMode);
    }

    private int promptUserNavigation(boolean isBookingMode) {
        Scanner scan = new Scanner(System.in);
        String border = "═".repeat(120);
        String empty = " ".repeat(120);
        
        System.out.println("\n\t\t╔" + border + "╗");
        System.out.println("\t\t║" + empty + "║");
        
        String title = "      [ NAVIGATION OPTIONS ]";
        System.out.println("\t\t║" + title + " ".repeat(120 - title.length()) + "║");
        System.out.println("\t\t║" + empty + "║");
        
        // --- CLEAN NAVIGATION FLOW ---
        if (isBookingMode) {
            System.out.println("\t\t║          [ 1 ] I am ready to pick my room (Exit Viewer)" + " ".repeat(120 - "          [ 1 ] I am ready to pick my room (Exit Viewer)".length()) + "║");
        } else {
            System.out.println("\t\t║          [Room#] Enter exact Room Number to view details (e.g., 201, 305)" + " ".repeat(120 - "          [Room#] Enter exact Room Number to view details (e.g., 201, 305)".length()) + "║");
        }
        
        System.out.println("\t\t║          [ 0 ] Go back and view another floor" + " ".repeat(120 - "          [ 0 ] Go back and view another floor".length()) + "║");
        System.out.println("\t\t║          [-1 ] Cancel / Return to Main Menu" + " ".repeat(120 - "          [-1 ] Cancel / Return to Main Menu".length()) + "║");
        
        System.out.println("\t\t║" + empty + "║");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Enter your choice: ");
        
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (Exception e) {
            return 0; 
        }
    }

    private void displayRoomLayout(String[][] roomLayout, int startRoomNum, String roomTypeName, boolean isBookingMode) {
        if (roomLayout == null || roomLayout.length == 0) {
            System.out.println("\t\t  [!] No layout to display.");
            return;
        }

        int currentRoomNumber = startRoomNum;
        final int INNER_WIDTH = 120; 

        System.out.println("\t\t╔" + "═".repeat(INNER_WIDTH) + "╗");
        printCenteredLine(" " + roomTypeName.toUpperCase() + " ROOM AVAILABILITY ", INNER_WIDTH);
        System.out.println("\t\t╠" + "═".repeat(INNER_WIDTH) + "╣");

        int lastVisibleRow = -1;
        for (int i = 0; i < roomLayout.length; i++) {
            if (roomLayout[i] != null && roomLayout[i].length > 0 && !roomLayout[i][0].equals("■")) {
                lastVisibleRow = i;
            }
        }

        for (int row = 0; row < roomLayout.length; row++) {
            String[] colsArr = roomLayout[row]; 
            if (colsArr == null || colsArr.length == 0 || colsArr[0].equals("■")) continue;

            int cols = colsArr.length;
            int base = 119 / cols;
            int rem = 119 % cols;
            int[] widths = new int[cols];
            for (int c = 0; c < cols; c++) widths[c] = base + (c < rem ? 1 : 0);

            StringBuilder roomNumbersRow = new StringBuilder();
            StringBuilder statusRow = new StringBuilder();
            StringBuilder hallwayDivider = new StringBuilder();

            for (int col = 0; col < cols; col++) {
                int w = widths[col];
                String roomName = "Room " + (currentRoomNumber + col);
                
                String rawStatus = colsArr[col];
                String finalStatus = rawStatus; 

                if (isBookingMode) {
                    if (rawStatus.equals("S") || rawStatus.equals("VR") || rawStatus.equals("VD") || rawStatus.equals("OOS")) {
                        finalStatus = "A";
                    } else {
                        finalStatus = "X"; 
                    }
                }
                
                String roomStatus = "[" + finalStatus + "]"; 

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
                String hallwaySign = "H A L L W A Y                        H A L L W A Y                        H A L L W A Y";
                printCenteredLine(hallwaySign, INNER_WIDTH);
                printCenteredLine(hallwayDivider.toString(), INNER_WIDTH);
            }

            currentRoomNumber += cols;
        }

        if (!isBookingMode) {
            System.out.println("\t\t╠" + "═".repeat(INNER_WIDTH) + "╣");
            printCenteredLine(" [ ROOM STATUS LEGEND ] ", INNER_WIDTH);
            System.out.println("\t\t╠" + "═".repeat(INNER_WIDTH) + "╣");
            System.out.println("\t\t║" + " ".repeat(INNER_WIDTH) + "║");
            
            for (StatusLegend legend : StatusLegend.values()) {
                String text = "      " + legend.getFullText(); 
                System.out.println("\t\t║" + text + " ".repeat(120 - text.length()) + "║");
            }
            
            System.out.println("\t\t║" + " ".repeat(INNER_WIDTH) + "║");
        }
        
        System.out.println("\t\t╚" + "═".repeat(INNER_WIDTH) + "╝");
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
        
        System.out.println("\t\t║" + " ".repeat(leftSpaces) + text + " ".repeat(rightSpaces) + "║");
    }
}