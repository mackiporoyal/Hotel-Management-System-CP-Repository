package finals.Booking;

public class Menu {
	enum eMenu{
		NEW_BOOKING("New Booking"),
		CHECK_BOOKING("Check Booking"),
		CHECK_AVAILABILITY("Check Availability"),
		FOLIO_MANAGEMENT("Folio Management"),;

		private String option; 
	    
		eMenu(String option) {
	    	this.option = option; 
	    }
	    
	    public String getMenu(){
	    	return option;
	    }
	}

	public void displayMenu() {
		System.out.println("\t\t╔══════════════════════════════════════════════════════════════════════════════╗");
		System.out.println("\t\t║                                                                              ║");
		System.out.println("\t\t║   ██      ██  ████████  ██        ██████    ███████   ███    ███  ████████   ║");
		System.out.println("\t\t║   ██      ██  ██        ██       ██    ██  ██     ██  ████  ████  ██         ║");
		System.out.println("\t\t║   ██  ██  ██  ██████    ██       ██        ██     ██  ██ ████ ██  ██████     ║");
		System.out.println("\t\t║   ██████████  ██    	  ██       ██    ██  ██     ██  ██  ██  ██  ██         ║");
		System.out.println("\t\t║    ██    ██   ████████  ████████  ██████    ███████   ██      ██  ████████   ║");
		System.out.println("\t\t║                                                                              ║");
		System.out.println("\t\t╠══════════════════════════════════════════════════════════════════════════════╣");
		System.out.println("\t\t║                                                                              ║");
		System.out.println("\t\t║                     WELCOME TO HOTEL MANAGEMENT SYSTEM                       ║");
		System.out.println("\t\t║                         - System Ready to Use -                              ║");
		System.out.println("\t\t║                                                                              ║");
		System.out.println("\t\t╚══════════════════════════════════════════════════════════════════════════════╝");
		System.out.println("\t\t\t\t___________________Select Option___________________");
		int j = 1;
		for(Menu.eMenu listMenu: Menu.eMenu.values()) {
    		System.out.println("\t\t\t\t\t  "+ (j++) +". "+listMenu.option);
    	}
	}	
}
