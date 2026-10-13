import UserInteraction.HomeOperations;

import utils.InputValidator;
import java.sql.SQLException;
import java.util.Scanner;

public class Home {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        System.out.println("Welcome to YucaChain Limited");
        while (running){
            System.out.println("press 1 to register farmer");
            System.out.println("press 2 to create cassava batch");
            System.out.println("Press 3 to create shipment");
            System.out.println("press 4 to update the shipment");
            System.out.println("press 5 to view the tracking history");
            System.out.println("press 6 to view registered farmer");
            System.out.println("press 7 to view cassava batch");
            System.out.println("press 8 to view shipment");
            System.out.println("Press 9 to search batch by batch code");
            System.out.println("press 10 to view cassava journey history");

            System.out.println("Press 0 to exit");

            int action = InputValidator.getMenuChoice(sc, "Enter your choice: ", 0,10);

            switch (action){
                case 1:
                    HomeOperations.registerFarmer(sc);
                    break;
                case 2:
                    HomeOperations.createBatch(sc);
                    break;
                case 3:
                   HomeOperations.createShipment(sc);
                    break;
                case 4:
                   HomeOperations.updateShipment(sc);
                    break;
                case 5:
                   HomeOperations.viewTrackingHistory(sc);
                    break;
                case 6:
                    HomeOperations.viewRegisteredFarmer();
                    break;
                case 7:
                    HomeOperations.viewCassavaBatch();
                    break;
                case 8:
                    HomeOperations.viewShipment();
                    break;
                case 9:
                    HomeOperations.searchBatch(sc);
                    break;
                case 10:
                    HomeOperations.viewCassavaJourney(sc);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Enter the valid input.");
            }
        }
        System.out.println("Thanks for choosing YucaChain Limited.");
    }
}
