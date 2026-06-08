package finals.database;

import finals.core.config.ProgramConstants;
import finals.core.ui.UIElement;
import finals.features.cashier.CashierController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AccountManager implements ProgramConstants {
    CashierController cashierController = new CashierController();
	public void displayCashierAuth() {
		String border = UIElement.createBorder("═");
    	AccountManager accountManager = new AccountManager();
		Scanner sc = new Scanner(System.in);
		System.out.println("\n\t\t╔"+border+"╗");
		UIElement.printCenteredRow("STAFF AUTENTICATION");
		System.out.println("\t\t╚" + border + "╝");

		System.out.print("\t\t          ► Username: ");
		String username = sc.nextLine();
		System.out.print("\t\t          ► Password: ");
		String password = sc.nextLine();

		if (accountManager.verifyLogin(username, password)) {
            System.out.println("\t\t          [Access Granted] Welcome to Folio Management.");
            cashierController.displayCashierMenu();
        } else {
            System.out.println("\t\t          [!] Access Denied. Invalid staff credentials.");
        }
    }
	
    /**
     * Verifies user credentials against the accounts storage database file.
     * Expected file row format inside Accounts.txt: username|password|role
     */
    public boolean verifyLogin(String username, String password) {
        if (username == null || password == null) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_FOLDER + "Accounts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split("\\|");
                if (credentials.length >= 2) {
                    String dbUsername = credentials[0].trim();
                    String dbPassword = credentials[1].trim();
                    
                    if (dbUsername.equals(username.trim()) && dbPassword.equals(password.trim())) {
                        return true; // Match found successfully
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[Authentication Warning] Accounts file not found or unreadable. Falling back to safe passage.");
            // Fallback safety catch: allows entry if the text file hasn't been populated yet
            return true; 
        }
        return false;
    }
}