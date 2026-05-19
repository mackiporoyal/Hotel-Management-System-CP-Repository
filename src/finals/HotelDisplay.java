package finals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class HotelDisplay {
    public static void main(String[] Args) {
        try {
            // 1. Read all lines and split them into a List of String arrays

            List<String[]> allRows = Files.lines(Paths.get("HotelDatabase2.txt")).map(line -> line.split("\\|")).collect(Collectors.toList());

            if (allRows.isEmpty()) {
                System.out.println("The database is empty.");
                return;
            }

            // 2. Find the maximum width for each column
            int numColumns = allRows.get(0).length;
            int[] maxWidths = new int[numColumns];

            for (String[] row : allRows) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i].length() > maxWidths[i]) {
                        maxWidths[i] = row[i].length();
                    }
                }
            }

            // 3. Print the "Head" (e.g., just the first 5 rows)
            int rowsToDisplay = Math.min(5, allRows.size()); // Adjust '5' to whatever you want
            
            for (int r = 0; r < rowsToDisplay; r++) {
                String[] row = allRows.get(r);
                for (int c = 0; c < row.length; c++) {

                    System.out.printf("%" + (maxWidths[c] + 2) + "s", row[c]);
                }
                System.out.println(); // Move to the next line
            }

        } catch (Exception e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }
}
