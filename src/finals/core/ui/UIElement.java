package finals.core.ui;

import finals.core.config.ProgramConstants;

public class UIElement {

    public static String createBorder(String borderCharacter) {
        return borderCharacter.repeat(ProgramConstants.TERMINAL_WIDTH);
    }

    public static void printRow(String textLine) {
        if (textLine == null) textLine = "";
        int leftoverSpaces = ProgramConstants.TERMINAL_WIDTH - textLine.length();
        System.out.println("\t\t║" + textLine + " ".repeat(Math.max(0, leftoverSpaces)) + "║");
    }

    public static void printCenteredRow(String textLine) {
        if (textLine == null) textLine = "";
        String cleanText = textLine.trim();
        int totalPaddingNeeded = ProgramConstants.TERMINAL_WIDTH - cleanText.length();
        
        int leftPaddingSpaces = totalPaddingNeeded / 2;
        int rightPaddingSpaces = totalPaddingNeeded - leftPaddingSpaces;
        
        System.out.println("\t\t║" + " ".repeat(Math.max(0, leftPaddingSpaces)) + cleanText + " ".repeat(Math.max(0, rightPaddingSpaces)) + "║");
    }
}