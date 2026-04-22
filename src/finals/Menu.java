package finals;

public class Menu {
	enum eMenu{
		CREATE_BOOKING(""), SEARCH_BOOKING(""), CHECK_ROOM_AVAILABILITY(""), MANAGE_STAY_AND_BILLING(""), MASTER_RECORD(""); 
	 
		private final String option;
	    eMenu(String option) {
	    	this.option = option; 
	    }
	    public String getMenu(){
	    	return option;
	    }
	}	
}
