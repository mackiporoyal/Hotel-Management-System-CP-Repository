package finals;

public class Menu {
	enum eMenu{
		CREATE_BOOKING("1"), SEARCH_BOOKING("2"), CHECK_ROOM_AVAILABILITY("3"), MANAGE_STAY_AND_BILLING("4"), MASTER_RECORD("5"); 
	 
		private final String option;
	    eMenu(String option) {
	    	this.option = option; 
	    }
	    
	    public String getMenu(){
	    	return option;
	    }
	}

	public void displayMenu() {
		System.out.println("\t\t\t\t___________Choose what you wanna to do___________");
		
		for(Menu.eMenu listMenu: Menu.eMenu.values()) {
    		System.out.println("\t\t\t\t\t  "+listMenu.option);
    	}
	}	
}
