package ui;

import java.util.Scanner;

public class YNReader {
    static Scanner scanner;
    static boolean ynReader (String prompt) {
        System.out.println(prompt);
        do {
            String userInput = scanner.nextLine();
            switch (userInput) {
                case "Y", "y" -> { return true; }
                case "N", "n" -> { return false; }
                default -> {
                    System.out.println("Input is invalid; please enter Y/N.");
                    System.out.println(prompt);
                }
            }
        } while (true);
    }
}
