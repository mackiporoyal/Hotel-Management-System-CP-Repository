package finals.core.config;

import java.io.File;

public interface ProgramConstants {
    // --- TERMINAL DIMENSIONS ---
    int TERMINAL_WIDTH = 120;

    // --- SYSTEM FILE TRACKING PATHS ---
    String DATABASE_FOLDER = System.getProperty("user.dir") + File.separator + "src" + File.separator + 
                             "finals" + File.separator + "database" + File.separator + "textdb" + File.separator;
                             
    String HOTEL_DB_PATH = DATABASE_FOLDER + "HotelDatabase.txt";
    String FINANCE_LOG_PATH = DATABASE_FOLDER + "FinanceLog.txt";
    String ROOM_DB_PATH = DATABASE_FOLDER + "RoomDatabase.txt";

    // --- BASE ROOM PRICE CONSTANTS ---
    double STANDARD_PRICE = 2500.00;
    double DELUXE_PRICE = 4500.00;
    double JR_SUITE_PRICE = 7000.00;
    double SUITE_PRICE = 12000.00;
    double PENTHOUSE_PRICE = 25000.00;

    // --- AMENITIES PASS PRICING ---
    double SWIM_PASS_PRICE = 500.00;
    double BUFFET_PASS_PRICE = 850.00;
    double EXCESS_PAX_PRICE = 250.00;

    // --- MAXIMUM OCCUPANCY CAPACITIES ---
    int CAP_STANDARD = 2;
    int CAP_DELUXE = 4;
    int CAP_JR_SUITE = 4;
    int CAP_SUITE = 4;
    int CAP_PENTHOUSE = 8;
}