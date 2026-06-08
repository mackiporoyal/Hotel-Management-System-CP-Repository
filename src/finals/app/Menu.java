package finals.app;

import finals.core.ui.UIElement;

public class Menu {
    
    enum eMenu {
        NEW_BOOKING("New Booking"),
        CHECK_BOOKING("Check Booking"),
        CHECK_AVAILABILITY("Check Availability"),
        FOLIO_MANAGEMENT("Folio Management");

        private String option; 
        eMenu(String option) { this.option = option; }
        public String getMenu() { return option; }
    }

    public void displayMenu() {
        String border = UIElement.createBorder("═");
        
        System.out.println("\n\t\t╔" + border + "╗");
        UIElement.printRow(" ");
        System.out.println("\t\t║                     " + "   ██      ██  ████████  ██        ██████    ███████   ███    ███  ████████   " + "                     ║");
        System.out.println("\t\t║                     " + "   ██      ██  ██        ██       ██    ██  ██     ██  ████  ████  ██         " + "                     ║");
        System.out.println("\t\t║                     " + "   ██  ██  ██  ██████    ██       ██        ██     ██  ██ ████ ██  ██████     " + "                     ║");
        System.out.println("\t\t║                     " + "   ██████████  ██        ██       ██    ██  ██     ██  ██  ██  ██  ██         " + "                     ║");
        System.out.println("\t\t║                     " + "    ██    ██   ████████  ████████  ██████    ███████   ██      ██  ████████   " + "                     ║");
        UIElement.printRow(" ");
        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow(" ");
        UIElement.printCenteredRow("HOTEL MANAGEMENT SYSTEM - MAIN MENU");
        UIElement.printCenteredRow("[ System Ready ]");
        UIElement.printRow(" ");
        System.out.println("\t\t╠" + border + "╣");
        UIElement.printRow(" ");
        
        int j = 1;
        for (eMenu listMenu : eMenu.values()) {
            String optionText = "[" + j + "]  " + listMenu.getMenu();
            String leftPadding = " ".repeat(48); 
            int remainingSpace = 120 - leftPadding.length() - optionText.length();
            String rightPadding = " ".repeat(remainingSpace);
            System.out.println("\t\t║" + leftPadding + optionText + rightPadding + "║");
            j++;
        }
        UIElement.printRow(" ");
        System.out.println("\t\t╚" + border + "╝");
    }    
}