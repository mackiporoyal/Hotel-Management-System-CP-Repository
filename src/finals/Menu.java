package finals;

public class Menu {
	enum eMenu{
		CREATE_BOOKING("Create New Booking"),
		SEARCH_BOOKING("Check Existing Booking"),
		CHECK_ROOM_AVAILABILITY("Check Room Availability"),
		MANAGE_STAY_AND_BILLING("Manage Stay and Billing"),
		MASTER_RECORD("Master Record"); 
	 
		private final String option;
	    eMenu(String option) {
	    	this.option = option; 
	    }
	    
	    public String getMenu(){
	    	return option;
	    }
	}

	public void displayMenu() {
		System.out.println("\t\t\t\t___________________Select Option___________________");
		int j = 1;
		for(Menu.eMenu listMenu: Menu.eMenu.values()) {
    		System.out.println("\t\t\t\t\t  "+ (j++) +". "+listMenu.option);
    	}
	}	
}
