package finals.Booking;

public class Menu {
    
    enum eMenu{
        NEW_BOOKING("New Booking"),
        CHECK_BOOKING("Check Booking"),
        CHECK_AVAILABILITY("Check Availability"),
        FOLIO_MANAGEMENT("Folio Management");

        private String option; 
        
        eMenu(String option) {
            this.option = option; 
        }
        
        public String getMenu(){
            return option;
        }
    }

    public void displayMenu() {
        String border = "═".repeat(120);
        String emptyLine = " ".repeat(120);
        
        System.out.println("\n\t\t╔" + border + "╗");
        System.out.println("\t\t║" + emptyLine + "║");
        
        // ASCII Art (80 chars wide) with 20 spaces of padding on both sides to equal 120
        System.out.println("\t\t║                     " + "   ██      ██  ████████  ██        ██████    ███████   ███    ███  ████████   " + "                     ║");
        System.out.println("\t\t║                     " + "   ██      ██  ██        ██       ██    ██  ██     ██  ████  ████  ██         " + "                     ║");
        System.out.println("\t\t║                     " + "   ██  ██  ██  ██████    ██       ██        ██     ██  ██ ████ ██  ██████     " + "                     ║");
        System.out.println("\t\t║                     " + "   ██████████  ██        ██       ██    ██  ██     ██  ██  ██  ██  ██         " + "                     ║");
        System.out.println("\t\t║                     " + "    ██    ██   ████████  ████████  ██████    ███████   ██      ██  ████████   " + "                     ║");
        
        System.out.println("\t\t║" + emptyLine + "║");
        System.out.println("\t\t╠" + border + "╣");
        System.out.println("\t\t║" + emptyLine + "║");
        
        // 42 spaces left + 35 characters text + 43 spaces right = 120 width
        System.out.println("\t\t║                                          HOTEL MANAGEMENT SYSTEM - MAIN MENU                                           ║");
        // 52 spaces left + 16 characters text + 52 spaces right = 120 width
        System.out.println("\t\t║                                                    [ System Ready ]                                                    ║");
        
        System.out.println("\t\t║" + emptyLine + "║");
        System.out.println("\t\t╠" + border + "╣");
        System.out.println("\t\t║" + emptyLine + "║");
        
        // Dynamically print the options while keeping the right wall perfectly aligned
        int j = 1;
        for(Menu.eMenu listMenu: Menu.eMenu.values()) {
            String optionText = "[" + j + "]  " + listMenu.getMenu();
            
            // 48 spaces of padding on the left to center it nicely within the 120 width
            String leftPadding = " ".repeat(48); 
            
            // Calculate how much empty space is left for the right side
            // 120 is the total inside width of the box
            int remainingSpace = 120 - leftPadding.length() - optionText.length();
            String rightPadding = " ".repeat(remainingSpace);
            
            System.out.println("\t\t║" + leftPadding + optionText + rightPadding + "║");
            j++;
        }
        
        System.out.println("\t\t║" + emptyLine + "║");
        System.out.println("\t\t╚" + border + "╝");
    }    
}