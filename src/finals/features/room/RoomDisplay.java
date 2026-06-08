package finals.features.room;

import java.util.Scanner;
import finals.core.ui.UIElement;

public class RoomDisplay {
    
    private RoomAvailability data;

    public enum StatusLegend {
        VR("[VR] Vacant/Ready"),
        VD("[VD] Vacant/Dirty"),
        OC("[OC] Occupied/Clean"),
        OD("[OD] Occupied/Dirty"),
        OOS("[OOS] Out of Service"),
        OOO("[OOO] Out of Order");

        private String label;
        StatusLegend(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    public RoomDisplay(RoomAvailability data) {
        this.data = data;
    }

    public void printHeader() {
        String border = UIElement.createBorder("═");
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printRow(" ");
        UIElement.printCenteredRow("ROOM AVAILABILITY");
        UIElement.printRow(" ");
        System.out.println("\t\t╚" + border + "╝");
    }

    public int displayByFloor(RoomAvailability.Floors floor, boolean isBookingMode) {
        switch (floor) {
            case SECOND: displayRoomLayout(data.getRoomsStandard(), 201, "STANDARD ROOMS (2ND FLOOR)", isBookingMode); break;
            case THIRD: displayRoomLayout(data.getRoomsDeluxe(), 301, "DELUXE ROOMS (3RD FLOOR)", isBookingMode); break;
            case FOURTH: displayRoomLayout(data.getRoomsJrSuite(), 401, "JUNIOR SUITE ROOMS (4TH FLOOR)", isBookingMode); break;
            case FIFTH: displayRoomLayout(data.getRoomsSuite(), 501, "SUITE ROOMS (5TH FLOOR)", isBookingMode); break;
            case SIXTH: displayRoomLayout(data.getRoomsPentHouse(), 601, "PENTHOUSE ROOMS (6TH FLOOR)", isBookingMode); break;
            default: System.out.println("\t\t  [!] No rooms available on this floor (Lobby/Amenities)."); break;
        }
        return promptUserNavigation(isBookingMode);
    }

    private int promptUserNavigation(boolean isBookingMode) {
        Scanner scan = new Scanner(System.in);
        String border = UIElement.createBorder("═");
        
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printRow(" ");
        UIElement.printRow("      [ NAVIGATION OPTIONS ]");
        UIElement.printRow(" ");
        
        if (isBookingMode) {
            UIElement.printRow("          [ 1 ] I am ready to pick my room (Exit Viewer)");
        } else {
            UIElement.printRow("          [Room#] Enter exact Room Number to view details (e.g., 201, 305)");
        }
        UIElement.printRow("          [ 0 ] Go back and view another floor");
        UIElement.printRow("          [-1 ] Cancel / Return to Main Menu");
        UIElement.printRow(" ");
        System.out.println("\t\t╚" + border + "╝");
        System.out.print("\t\t          ► Enter your choice: ");
        try { return Integer.parseInt(scan.nextLine().trim()); } catch (Exception e) { return 0; }
    }

    private void displayRoomLayout(String[][] roomLayout, int startRoomNum, String roomTypeName, boolean isBookingMode) {
        if (roomLayout == null || roomLayout.length == 0) return;

        int currentRoomNumber = startRoomNum;
        String border = UIElement.createBorder("═");

        System.out.println("\t\t╔" + border + "╗");
        UIElement.printCenteredRow(" " + roomTypeName.toUpperCase() + " ROOM AVAILABILITY ");
        System.out.println("\t\t╠" + border + "╣");

        int lastVisibleRow = -1;
        for (int i = 0; i < roomLayout.length; i++) {
            if (roomLayout[i] != null && roomLayout[i].length > 0 && !roomLayout[i][0].equals("■")) lastVisibleRow = i;
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
                    if (rawStatus.equals("S") || rawStatus.equals("VR") || rawStatus.equals("VD") || 
                        rawStatus.equals("OOS") || rawStatus.equalsIgnoreCase("INACTIVE")) {
                        finalStatus = "A";
                    } else {
                        finalStatus = "X"; 
                    }
                }
                
                roomNumbersRow.append(formatCell(roomName, w));
                statusRow.append(formatCell("[" + finalStatus + "]", w));
                hallwayDivider.append("┼").append("═".repeat(Math.max(0, w - 1)));
            }

            roomNumbersRow.append("│"); statusRow.append("│"); hallwayDivider.append("┼");

            UIElement.printCenteredRow(roomNumbersRow.toString());
            UIElement.printCenteredRow(statusRow.toString());

            if (row < lastVisibleRow) {
                UIElement.printCenteredRow(hallwayDivider.toString());
                UIElement.printCenteredRow("H A L L W A Y                        H A L L W A Y                        H A L L W A Y");
                UIElement.printCenteredRow(hallwayDivider.toString());
            }
            currentRoomNumber += cols;
        }

        // FIXED: RE-ENGINEERED 3-ABOVE 3-BELOW LEGEND ROW OUTPUT ENGINE
        if (!isBookingMode) {
            System.out.println("\t\t╠" + border + "╣");
            UIElement.printCenteredRow(" [ ROOM STATUS LEGEND ] ");
            System.out.println("\t\t╠" + border + "╣");
            
            StatusLegend[] values = StatusLegend.values();
            
            // Row 1: VR, VD, OC
            String row1String = String.format("                   %-30s %-30s %-30s", 
                                              values[0].getLabel(), values[1].getLabel(), values[2].getLabel());
            UIElement.printRow(row1String);
            
            // Row 2: OD, OOS, OOO
            String row2String = String.format("                   %-30s %-30s %-30s", 
                                              values[3].getLabel(), values[4].getLabel(), values[5].getLabel());
            UIElement.printRow(row2String);
        }
        System.out.println("\t\t╚" + border + "╝");
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
}