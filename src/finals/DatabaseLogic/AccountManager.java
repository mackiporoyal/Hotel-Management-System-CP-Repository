package finals.DatabaseLogic;

import java.io.*;
import java.util.*;

public class AccountManager {
    private final String accountsPath = System.getProperty("user.dir") + File.separator + "src" + 
                                       File.separator + "finals" + File.separator + "DatabaseLogic" + 
                                       File.separator + "Accounts.txt";

    public boolean authenticate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(accountsPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(username)) {
                    // Check if password matches (or is empty for receptionist)
                    if (parts.length > 1) {
                        return parts[1].equals(password);
                    } else {
                        return password.isEmpty(); // Receptionist no password
                    }
                }
            }
        } catch (IOException e) { System.out.println("Accounts file not found."); }
        return false;
    }
}