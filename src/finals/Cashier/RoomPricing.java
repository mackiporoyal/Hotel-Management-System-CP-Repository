package finals.Cashier;

public interface RoomPricing {
	//room type
    double STANDARD_PRICE = 2500.00;
    double DELUXE_PRICE = 4500.00;
    double JR_SUITE_PRICE = 7000.00;
    double SUITE_PRICE = 12000.00;
    double PENTHOUSE_PRICE = 25000.00;
    //amenities
    double SWIM_PASS_PRICE = 500.00;
    double BUFFET_PASS_PRICE = 850.00;
    double EXCESS_PAX_PRICE = 250.00;
    
    double calculateTotal(String roomType, long nights);
    double calculateExtras(int swimPasses, int buffetPasses); // Added this
    double calculateDownpayment(double total);
    
    int CAP_STANDARD = 2;
    int CAP_DELUXE = 4;
    int CAP_JR_SUITE = 4;
    int CAP_SUITE = 4;
    int CAP_PENTHOUSE = 8;
}