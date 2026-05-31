package finals.Receptionist;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.YearMonth;

import finals.Booking.Logic.BookingManager;
import finals.Booking.RoomAvailabilityLogic.RoomAvailabilityLogic;

public class NewBooking extends BookingManager {
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
    
    // Save the specific room number chosen by the guest
    private int selectedRoomNumber; 
    
    private ArrayList<String> adultNames = new ArrayList<>();
    private ArrayList<String> childNames = new ArrayList<>();
    private int swimPasses;
    private int buffetPasses;

    public void writeDatabase() { 
        Scanner scan = new Scanner(System.in);

        adultNames.clear();
        childNames.clear();
        
        System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("\t\t║ -1 Back                        NEW BOOKING                                ║");
        System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
        
        // --- 1. ASK FOR GUESTS ---
        while(true) {
            try {
                System.out.print("\t\t\t\tHow many adults? : ");
                totalAdult = scan.nextInt();
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
        System.out.println("\t\t\t═══════════════════════════════════════════════════════════════════");
        
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
        
        // --- 4. INTERACTIVE ROOM SELECTION ---
     
        
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
        
        // --- 6. SAVE TO DATABASE ---
        // createBooking(String timeIn, String timeOut, String roomType, List<String> adultNames, List<String> childNames, int totalAdult, int totalChild, int swimPasses, int buffetPasses)
        this.createBooking(timeIn, timeOut, roomType, adultNames, childNames, totalAdult, totalChild, swimPasses, buffetPasses);
        }

}