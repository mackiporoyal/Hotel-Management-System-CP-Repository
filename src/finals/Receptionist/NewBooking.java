package finals.Receptionist;

import java.util.ArrayList;
import java.util.List;
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

    public void displayNewBooking() { 
        Scanner scan = new Scanner(System.in);

        adultNames.clear();
        childNames.clear();
        
        System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║ -1 Back                        NEW BOOKING                                   ║");
        System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
        
        // adult total is required to proceed with booking, child total can be 0
        while(true) {
            try {
                System.out.print("\t\t║\t\tHow many adults? : ");
                totalAdult = scan.nextInt();
                if (totalAdult < -1) {
                    System.out.println("\t\t\tNumber of adults cannot be negative. Please enter a valid number.");
                    continue;
                }
                if(totalAdult == 0) {
                    System.out.println("\t\t\tAt least one adult is required for a booking. Please");
                    continue;
                }
                scan.nextLine(); 
                break;
                
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a valid number.");
                scan.nextLine(); 
            }
        }
        if(totalAdult == -1) return;
        
        while(true) {
            try {
                System.out.print("\t\t\t\tHow many children? : ");
                totalChild = scan.nextInt();
                scan.nextLine(); 
                break;
                
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a valid number.");
                scan.nextLine();
            }
        }
        if(totalChild == -1) return;
        
        System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
        System.out.println("\t\t\t\t\t\tTotal Guest: " + (totalAdult + totalChild));
        System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════\n");
   
        // --- 2. ASK FOR CHECK-IN DATES ---
        System.out.println("\t\t\t\tDate of Check In : ");

        while(true) {
            try {
                System.out.print("\t\t\t\tEnter year (YYYY) : ");
                yearIn = scan.nextInt();
                scan.nextLine(); 
                if(yearIn == -1) return;
                if(yearIn < currentYear) {
                    System.out.println("\t\t\tBackdating is not permitted. Please enter current dates.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a valid 4-digit year.");
                scan.nextLine();
            }
        }
        
        while(true) {
            try {
                System.out.print("\t\t\t\tEnter month (MM) : ");
                monthIn = scan.nextInt();
                scan.nextLine(); 
                if(monthIn == -1) return;
                if(monthIn < 1 || monthIn > 12) {
                    System.out.println("\t\t\tInvalid month. Please enter a value between 1 and 12.");
                    continue;
                }
                if(monthIn < currentMonthNumber && yearIn <= currentYear) {
                    System.out.println("\t\t\tBackdating is not permitted.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a valid month (1-12).");
                scan.nextLine();
            }
        }
        
        while(true) {
            try {
                System.out.print("\t\t\t\tEnter day (DD) : ");
                dayIn = scan.nextInt();
                scan.nextLine(); 
                if(dayIn == -1) return;
                
                int maxDaysInMonth = YearMonth.of(yearIn, monthIn).lengthOfMonth();
                if(dayIn < 1 || dayIn > maxDaysInMonth) {
                    System.out.println("\t\t\tInvalid day. This month has " + maxDaysInMonth + " days.");
                    continue;
                }
                
                if(dayIn < currentDay && monthIn <= currentMonthNumber && yearIn <= currentYear) {
                    System.out.println("\t\t\tBackdating is not permitted.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input.");
                scan.nextLine();
            }
        }
    
        timeIn = String.format("%d-%02d-%02d", yearIn, monthIn, dayIn);
        
        // --- 3. ASK FOR CHECK-OUT DATES ---
        System.out.println("\t\t\t\tDate of Check Out : ");
        while(true) {
            try {
                System.out.print("\t\t\t\tEnter year (YYYY) : ");
                yearOut = scan.nextInt();
                scan.nextLine();
                if(yearOut == -1) return;
                if(yearOut < yearIn) {
                    System.out.println("\t\t\tInvalid checkout year. Must match or be after check-in year.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input.");
                scan.nextLine();
            }
        }
            
        while(true) { 
            try {
                System.out.print("\t\t\t\tEnter month (MM) : ");
                monthOut = scan.nextInt();
                scan.nextLine(); 
                if(monthOut == -1) return;
                if(monthOut < 1 || monthOut > 12) {
                    System.out.println("\t\t\tInvalid month.");
                    continue;
                }
                if(yearOut == yearIn && monthOut < monthIn) {
                    System.out.println("\t\t\tInvalid checkout month.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input.");
                scan.nextLine();
            }
        }
        
        while(true) {
            try {
                System.out.print("\t\t\t\tEnter day (DD) : ");
                dayOut = scan.nextInt();
                scan.nextLine();
                if(dayOut == -1) return;
                
                int maxDaysOutMonth = YearMonth.of(yearOut, monthOut).lengthOfMonth();
                if(dayOut < 1 || dayOut > maxDaysOutMonth) {
                    System.out.println("\t\t\tInvalid day. This month has " + maxDaysOutMonth + " days.");
                    continue;
                }
                
                if(yearOut == yearIn && monthOut == monthIn && dayOut <= dayIn) {
                    System.out.println("\t\t\tCheckout day must be after the check-in day.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input.");
                scan.nextLine();
            }
        }
        timeOut = String.format("%d-%02d-%02d", yearOut, monthOut, dayOut);
       
        // --- 4. GUEST NAMES ---
        for (int i = 1; i <= totalAdult; i++) {
            System.out.print("\t\t\t\tEnter name for Adult " + i + ": ");
            String name = scan.nextLine();
            if(name.equals("-1")) return;
            adultNames.add(name); 
        }
            
        for (int i = 1; i <= totalChild; i++) {
            System.out.print("\t\t\t\tEnter name for Child " + i + ": ");
            String name = scan.nextLine();
            if(name.equals("-1")) return;
            childNames.add(name); 
        }
        
        // --- 5. LOGIC FOR ROOM AVAILABILITY ---
        System.out.println("\n\t\t\t═══════════════════════════════════════════════════════════════════");
        System.out.println("\t\t\t\t\t\tROOM SELECTION");
        System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
        System.out.println("\t\t\tOpening Room Availability Viewer...");
        System.out.println("\t\t\t(Press -1 to exit the viewer when you are ready to pick a room)\n");
        
        roomViewer.startMenu();
        
        // --- STRICT ROOM TYPE VALIDATION ---
        while(true) {
            System.out.print("\n\t\t\t\tEnter desired Room Type (Standard, Deluxe, Junior Suite, Suite, Penthouse): ");
            roomType = scan.nextLine().trim();
            if(roomType.equals("-1")) return;
            
            String checkType = roomType.toLowerCase();
            
            if (checkType.equals("standard") || checkType.equals("deluxe") || checkType.equals("junior suite") || 
                checkType.equals("jr suite") || checkType.equals("suite") || checkType.equals("penthouse")) {
                
                roomType = roomType.substring(0, 1).toUpperCase() + roomType.substring(1).toLowerCase();
                if (checkType.equals("junior suite") || checkType.equals("jr suite")) roomType = "Junior Suite";
                break; 
            } else {	
                System.out.println("\t\t\t[!] Invalid entry. Please type exactly: Standard, Deluxe, Junior Suite, Suite, or Penthouse.");
            }
        }
        
        // --- STRICT ROOM NUMBER & OCCUPANCY VALIDATION ---
        while(true) {
            try {
                System.out.print("\t\t\t\tEnter exact Room Number (e.g., 201, 305): ");
                roomNumber = scan.nextInt();
                scan.nextLine(); 
                
                String typeLower = roomType.toLowerCase();
                boolean isValidFloor = false;

                // 1. Check if the number matches the chosen floor
                if (typeLower.contains("standard") && roomNumber >= 201 && roomNumber <= 208) {
                    isValidFloor = true;
                } else if (typeLower.contains("deluxe") && roomNumber >= 301 && roomNumber <= 308) {
                    isValidFloor = true;
                } else if ((typeLower.contains("junior") || typeLower.contains("jr")) && roomNumber >= 401 && roomNumber <= 404) {
                    isValidFloor = true;
                } else if (typeLower.contains("suite") && !typeLower.contains("junior") && !typeLower.contains("jr") && roomNumber >= 501 && roomNumber <= 502) {
                    isValidFloor = true;
                } else if (typeLower.contains("penthouse") && roomNumber == 601) {
                    isValidFloor = true;
                }

                if (!isValidFloor) {
                    System.out.println("\t\t\t[!] Mismatch: Room " + roomNumber + " is not a valid " + roomType + " room.");
                    continue; // Make them try again
                }

                // 2. Check if the room is ALREADY BOOKED [X] in the database
                boolean isOccupied = false;
                List<String[]> records = this.readAllLines(); 
                for (String[] row : records) {
                    if (row.length >= 13 && row[12].trim().equalsIgnoreCase("ACTIVE")) {
                        try {
                            int bookedRoom = Integer.parseInt(row[4].trim());
                            if (bookedRoom == roomNumber) {
                                isOccupied = true;
                                break;
                            }
                        } catch (Exception ignored) {}
                    }
                }

                if (isOccupied) {
                    System.out.println("\t\t\t[!] Sorry! Room " + roomNumber + " is already occupied [X]. Please choose a different room.");
                    continue; // Make them try again
                }

                // If it passes both the Floor Check and the Occupancy Check, break the loop!
                break;

            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a valid room number.");
                scan.nextLine();
            }
        }
        
        // --- 6. AMENITIES ---
        while(true) {
            try {
                System.out.print("\t\t\t\tHow many Pool Passes? : ");
                swimPasses = scan.nextInt();
                scan.nextLine(); 
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a number.");
                scan.nextLine();
            }
        }
        if(swimPasses == -1) return;
        
        while(true) {
            try {
                System.out.print("\t\t\t\tHow many Buffet Passes? : ");
                buffetPasses = scan.nextInt();
                scan.nextLine(); 
                break;
            } catch (Exception e) {
                System.out.println("\t\t\tInvalid input. Please enter a number.");
                scan.nextLine();
            }
        }
        if(buffetPasses == -1) return;
        
        // --- 7. SAVE TO DATABASE ---
        this.createBooking(timeIn, timeOut, roomType, roomNumber, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
    }
}