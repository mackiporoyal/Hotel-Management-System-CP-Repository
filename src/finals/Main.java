package finals;

public class Main {
	public static void main(String[] args) {		
		Menu.eMenu[] menu = Menu.eMenu.values();
		for (Menu.eMenu listMenu: Menu.eMenu.values()) {
			System.out.println(listMenu);
		}
	}
}
 