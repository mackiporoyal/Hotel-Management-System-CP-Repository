package finals.Receptionist;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.YearMonth;

import finals.Booking.BookingManager;
import finals.Booking.RoomAvailability.RoomController; 

public class NewBooking extends BookingManager {
    RoomController roomViewer = new RoomController();
    
    private LocalDate currentDate = LocalDate.now();
    private int currentYear = currentDate.getYear();
    private int currentMonthNumber = currentDate.getMonthValue();
    private int currentDay = currentDate.getDayOfMonth();
    
    private int totalAdult;
    private int totalChild;
    private int yearIn;
    private int monthIn;
    private int dayIn;
    private String timeIn;
    private int yearOut;
    private int monthOut;
    private int dayOut;
    private String timeOut;
    
    private String roomType;
    private int roomNumber; 
    private ArrayList<String> adultNames = new ArrayList<>();
    private ArrayList<String> childNames = new ArrayList<>();
    private int swimPasses;
    private int buffetPasses;

    private void printRow(String text) {
        int spaces = 120 - text.length();
        System.out.println("\t\t║" + text + " ".repeat(Math.max(0, spaces)) + "║");
    }

    public void displayNewBooking() { 
        Scanner scan = new Scanner(System.in);
        String border = "═".repeat(120);

        adultNames.clear();
        childNames.clear();
        
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("");
        
        String title = "HOTEL MANAGEMENT SYSTEM - NEW BOOKING";
        int padL = (120 - title.length()) / 2;
        printRow(" ".repeat(padL) + title);
        
        String subTitle = "[ Type '-1' at any prompt to cancel ]";
        int subPadL = (120 - subTitle.length()) / 2;
        printRow(" ".repeat(subPadL) + subTitle);
        
        printRow("");
        System.out.println("\t\t╠" + border + "╣");
        
        // --- 1. GUEST COUNT ---
        printRow("      [ 1. GUEST COUNT ]");
        System.out.println("\t\t╚" + border + "╝");
        
        while(true) {
            try {
                System.out.print("\t\t          ► Number of Adults       : ");
                totalAdult = scan.nextInt();
                
                if (totalAdult < -1) {
                    System.out.println("\t\t            [!] Invalid. Number cannot be negative.");
                    continue;
                }
                if(totalAdult == 0) {
                    System.out.println("\t\t            [!] Invalid. At least one adult is required.");
                    continue;
                }
                scan.nextLine(); 
                break;
                
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input. Please enter a number.");
                scan.nextLine(); 
            }
        }
        if(totalAdult == -1) return;
        
        while(true) {
            try {
                System.out.print("\t\t          ► Number of Children     : ");
                totalChild = scan.nextInt();
                scan.nextLine(); 
                break;
                
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input. Please enter a number.");
                scan.nextLine();
            }
        }
        if(totalChild == -1) return;
        
        System.out.println("\n\t\t          * Total Guests Booked    : " + (totalAdult + totalChild));
   
        // --- 2. CHECK-IN DATES ---
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("      [ 2. CHECK-IN DATE ]");
        System.out.println("\t\t╚" + border + "╝");

        while(true) {
            try {
                System.out.print("\t\t          ► Enter Year  (YYYY)     : ");
                yearIn = scan.nextInt();
                scan.nextLine(); 
                
                if(yearIn == -1) return;
                if(yearIn < currentYear) {
                    System.out.println("\t\t            [!] Backdating is not permitted.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input. Enter a 4-digit year.");
                scan.nextLine();
            }
        }
        
        while(true) {
            try {
                System.out.print("\t\t          ► Enter Month (MM)       : ");
                monthIn = scan.nextInt();
                scan.nextLine(); 
                
                if(monthIn == -1) return;
                if(monthIn < 1 || monthIn > 12) {
                    System.out.println("\t\t            [!] Invalid month (1-12 only).");
                    continue;
                }
                if(monthIn < currentMonthNumber && yearIn <= currentYear) {
                    System.out.println("\t\t            [!] Backdating is not permitted.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input. Enter a valid month.");
                scan.nextLine();
            }
        }
        
        while(true) {
            try {
                System.out.print("\t\t          ► Enter Day   (DD)       : ");
                dayIn = scan.nextInt();
                scan.nextLine(); 
                
                if(dayIn == -1) return;
                
                int maxDaysInMonth = YearMonth.of(yearIn, monthIn).lengthOfMonth();
                if(dayIn < 1 || dayIn > maxDaysInMonth) {
                    System.out.println("\t\t            [!] Invalid. This month has " + maxDaysInMonth + " days.");
                    continue;
                }
                
                if(dayIn < currentDay && monthIn <= currentMonthNumber && yearIn <= currentYear) {
                    System.out.println("\t\t            [!] Backdating is not permitted.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input.");
                scan.nextLine();
            }
        }
    
        timeIn = String.format("%d-%02d-%02d", yearIn, monthIn, dayIn);
        
        // --- 3. CHECK-OUT DATES ---
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("      [ 3. CHECK-OUT DATE ]");
        System.out.println("\t\t╚" + border + "╝");

        while(true) {
            try {
                System.out.print("\t\t          ► Enter Year  (YYYY)     : ");
                yearOut = scan.nextInt();
                scan.nextLine();
                
                if(yearOut == -1) return;
                if(yearOut < yearIn) {
                    System.out.println("\t\t            [!] Checkout year must be after check-in year.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input.");
                scan.nextLine();
            }
        }
            
        while(true) { 
            try {
                System.out.print("\t\t          ► Enter Month (MM)       : ");
                monthOut = scan.nextInt();
                scan.nextLine(); 
                
                if(monthOut == -1) return;
                if(monthOut < 1 || monthOut > 12) {
                    System.out.println("\t\t            [!] Invalid month.");
                    continue;
                }
                if(yearOut == yearIn && monthOut < monthIn) {
                    System.out.println("\t\t            [!] Checkout month cannot be before check-in.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input.");
                scan.nextLine();
            }
        }
        
        while(true) {
            try {
                System.out.print("\t\t          ► Enter Day   (DD)       : ");
                dayOut = scan.nextInt();
                scan.nextLine();
                
                if(dayOut == -1) return;
                
                int maxDaysOutMonth = YearMonth.of(yearOut, monthOut).lengthOfMonth();
                if(dayOut < 1 || dayOut > maxDaysOutMonth) {
                    System.out.println("\t\t            [!] Invalid. This month has " + maxDaysOutMonth + " days.");
                    continue;
                }
                
                if(yearOut == yearIn && monthOut == monthIn && dayOut <= dayIn) {
                    System.out.println("\t\t            [!] Checkout day must be after check-in day.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input.");
                scan.nextLine();
            }
        }
        timeOut = String.format("%d-%02d-%02d", yearOut, monthOut, dayOut);
       
        // --- 4. GUEST NAMES ---
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("      [ 4. GUEST REGISTRATION ]");
        System.out.println("\t\t╚" + border + "╝");

        for (int i = 1; i <= totalAdult; i++) {
            System.out.print("\t\t          ► Name for Adult " + i + "       : ");
            String name = scan.nextLine();
            if(name.equals("-1")) return;
            adultNames.add(name); 
        }
            
        for (int i = 1; i <= totalChild; i++) {
            System.out.print("\t\t          ► Name for Child " + i + "       : ");
            String name = scan.nextLine();
            if(name.equals("-1")) return;
            childNames.add(name); 
        }
        
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("      [ 5. ROOM SELECTION ]");
        printRow("");
        printRow("          * Opening Room Availability Viewer...");
        printRow("          * Review the floor plan, then press -1 to exit and select.");
        System.out.println("\t\t╚" + border + "╝");
        
        // --- 5. LOGIC FOR ROOM AVAILABILITY ---
        boolean proceedWithBooking = roomViewer.startMenu(true, timeIn); 

        if (!proceedWithBooking) {
            return;
        }
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("      [ ROOM SELECTION CONTINUED ]");
        System.out.println("\t\t╚" + border + "╝");

        while(true) {
            System.out.print("\t\t          ► Desired Room Type      : ");
            roomType = scan.nextLine().trim();
            
            if(roomType.equals("-1")) return;
            
            String checkType = roomType.toLowerCase();
            
            if (checkType.equals("standard") || checkType.equals("deluxe") || checkType.equals("junior suite") || 
                checkType.equals("jr suite") || checkType.equals("suite") || checkType.equals("penthouse")) {
                
                roomType = roomType.substring(0, 1).toUpperCase() + roomType.substring(1).toLowerCase();
                if (checkType.equals("junior suite") || checkType.equals("jr suite")) roomType = "Junior Suite";
                break; 
            } else {
                System.out.println("\t\t            [!] Must be exactly: Standard, Deluxe, Junior Suite, Suite, Penthouse.");
            }
        }

        while(true) {
            try {
                System.out.print("\t\t          ► Exact Room Number      : ");
                roomNumber = scan.nextInt();
                scan.nextLine(); 
                
                String typeLower = roomType.toLowerCase();
                boolean isValidFloor = false;

                if (typeLower.contains("standard") && roomNumber >= 201 && roomNumber <= 216) {
                    isValidFloor = true;
                } else if (typeLower.contains("deluxe") && roomNumber >= 301 && roomNumber <= 316) {
                    isValidFloor = true;
                } else if ((typeLower.contains("junior") || typeLower.contains("jr")) && roomNumber >= 401 && roomNumber <= 408) {
                    isValidFloor = true;
                } else if (typeLower.contains("suite") && !typeLower.contains("junior") && !typeLower.contains("jr") && roomNumber >= 501 && roomNumber <= 504) {
                    isValidFloor = true;
                } else if (typeLower.contains("penthouse") && roomNumber >= 601 && roomNumber <= 602) {
                    isValidFloor = true;
                }

                if (!isValidFloor) {
                    System.out.println("\t\t            [!] Mismatch: Room " + roomNumber + " is not a valid " + roomType + " room.");
                    continue; 
                }
                break;

            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input. Please enter a valid room number.");
                scan.nextLine();
            }
        }
        
        // --- 6. AMENITIES ---
        System.out.println("\n\t\t╔" + border + "╗");
        printRow("      [ 6. ADD-ON AMENITIES ]");
        System.out.println("\t\t╚" + border + "╝");

        while(true) {
            try {
                System.out.print("\t\t          ► Number of Pool Passes  : ");
                swimPasses = scan.nextInt();
                scan.nextLine(); 
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input. Please enter a number.");
                scan.nextLine();
            }
        }
        if(swimPasses == -1) return;
        
        while(true) {
            try {
                System.out.print("\t\t          ► Number of Buffet Passes: ");
                buffetPasses = scan.nextInt();
                scan.nextLine(); 
                break;
            } catch (Exception e) {
                System.out.println("\t\t            [!] Invalid input. Please enter a number.");
                scan.nextLine();
            }
        }
        if(buffetPasses == -1) return;
        
        // --- 7. SAVE TO DATABASE ---
        this.createBooking(timeIn, timeOut, roomType, roomNumber, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
    }
}