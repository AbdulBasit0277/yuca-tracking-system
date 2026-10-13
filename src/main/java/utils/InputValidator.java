package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidator {
    // validating a positive long number
    public static long getPositiveLong(Scanner sc, String message){
        while (true){
            System.out.print(message);
            String input = sc.nextLine();
            try {
                long value = Long.parseLong(input);
                if (value <= 0){
                    System.out.println("Please enter a number greater than 0.");
                    continue;
                }
                return value;
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // getting required text
    public static String getRequiredText(Scanner sc, String message){
        while (true){
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (input.isEmpty()){
                System.out.println("This field can not be empty.");
                continue;
            }
            return input;
        }
    }

    // getting positive quantity
    public static long getPositiveQuantity(Scanner sc, String message){
        while (true){
            System.out.print(message);
            String input = sc.nextLine();
            try {
                long quantity = Long.parseLong(input);
                if (quantity <= 0){
                    System.out.println("Quantity must be greater than 0.");
                    continue;
                }
                return quantity;
            }catch (NumberFormatException e){
                System.out.println("Please enter a valid quantity.");
            }
        }
    }

    // validating menu choice
    public static int getMenuChoice(Scanner sc, String message, int min, int max) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static LocalDate getValidDate(Scanner sc, String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Please use the format dd-MM-yyyy.");
            }
        }
    }
}
