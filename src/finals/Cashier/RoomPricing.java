package finals.Cashier;

public interface RoomPricing {
    // Placeholder values for now
    double STANDARD_PRICE = 2500.00;
    double DELUXE_PRICE = 4500.00;
    double JR_SUITE_PRICE = 7000.00;
    double SUITE_PRICE = 12000.00;
    double PENTHOUSE_PRICE = 25000.00;

    double calculateTotal(String roomType, long nights);
    double calculateDownpayment(double total);
}