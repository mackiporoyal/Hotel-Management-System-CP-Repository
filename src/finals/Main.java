package finals;
import java.util.Scanner;
public class Main {
	public static void main(String[] args) {		
		Menu menu = new Menu();
		Scanner scan = new Scanner(System.in);
		TextFileLogic test = new TextFileLogic();
		
		System.out.println("\t\t\t\t==================================================");
		System.out.println("\t\t\t\t____WELCOME TO HOTEL MANAGEMENT CENTRAL SYSTEM____");
		System.out.println();
		menu.displayMenu();
		System.out.print("\t\t\t\tENTER HERE: ");
		int chooseMenu = scan.nextInt();
		scan.nextLine();
		
		switch(chooseMenu) {
		case 1: test.d();;break;//goes to create booking class
		case 2: System.out.println("2");break;//goes to search booking class
		//etc option
		default: System.out.println("Enter + //logic to count how many case/length of enum values// only!");
		}
		scan.close(); // 
		
	}
}
