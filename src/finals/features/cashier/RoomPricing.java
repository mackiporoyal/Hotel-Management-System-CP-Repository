package finals.features.cashier;

import finals.core.config.ProgramConstants;

public interface RoomPricing extends ProgramConstants {
    double calculateTotal(String roomType, long nights);
    double calculateExtras(int swimPasses, int buffetPasses);
    double calculateDownpayment(double total);
}