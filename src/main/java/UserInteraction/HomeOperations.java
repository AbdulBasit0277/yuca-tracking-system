package UserInteraction;

import model.*;
import service.CassavaBatchService;
import service.FarmerService;
import service.ShipmentService;
import service.TrackingService;
import utils.InputValidator;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class HomeOperations {
    public static void registerFarmer(Scanner sc){
        try {
            String farmerName = InputValidator.getRequiredText(sc, "Enter your name: ");

            String farmerPhoneNumber = InputValidator.getRequiredText(sc, "Enter your phone number: ");

            String farmerLocation = InputValidator.getRequiredText(sc, "Enter your location: ");

            Farmer farmer = new Farmer(farmerName, farmerPhoneNumber, farmerLocation);
            FarmerService.registerFarmer(farmer);
            System.out.println(farmerName + " successfully registered.");
        }catch (SQLException e){
            System.out.println("A database error occurred while registering farmer");
        }
    }

    public static void createBatch(Scanner sc){
        try {
            String batchCode = InputValidator.getRequiredText(sc, "Enter your batch code: ");

            long farmerID = InputValidator.getPositiveLong(sc, "Enter your ID: ");

            long quantity = InputValidator.getPositiveQuantity(sc, "Enter the quantity: ");

            LocalDate harvestDate = InputValidator.getValidDate(sc, "Enter the harvest date(dd-MM-yyyy): ");

            CassavaBatch batch = new CassavaBatch(batchCode,farmerID,quantity,harvestDate);
            long batchId = CassavaBatchService.createBatch(batch);
            if (batchId == -1){
                return;
            }
            System.out.println("Your batch ID is: " + batchId);
        }catch (SQLException e){
            // 23505 is PostgreSQL's SQL state for a unique constraint violation.
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Batch creation failed: this batch code already exists.");
            } else {
                System.out.println("A database error occurred while creating batch.");
            }
        }
    }

    public static void createShipment(Scanner sc){
        try {
            long confirmBatchId = InputValidator.getPositiveLong(sc, "Enter batch ID: ");

            String driverName = InputValidator.getRequiredText(sc, "Driver name: ");

            String vehicleNumber = InputValidator.getRequiredText(sc, "Vehicle number: ");

            String destination = InputValidator.getRequiredText(sc, "Destination: ");

            Shipping shipment = new Shipping(confirmBatchId, driverName, vehicleNumber, destination);

            long shipmentId = ShipmentService.createShipment(shipment);

            if (shipmentId == -1) {
                return;
            }

            System.out.println("Your shipment ID is: " + shipmentId);
        }catch (SQLException e){
            if ("23505".equals(e.getSQLState())) {
                System.out.println("Shipment creation failed: this batch already has a shipment.");
            } else {
                System.out.println("A database error occurred while creating shipment.");
            }
        }
    }

    public static void updateShipment(Scanner sc){
        try {
            long shipmentID = InputValidator.getPositiveLong(sc, "Enter shipment ID: ");

            String location = InputValidator.getRequiredText(sc, "Enter your location: ");

            String status = InputValidator.getRequiredText(sc, "Enter the shipment status: ");

            TrackingUpdate trackingUpdate = new TrackingUpdate(shipmentID, location, status);
            TrackingService.addTrackingUpdate(trackingUpdate);

        }catch (IllegalArgumentException | IllegalStateException e){
            System.out.println("Tracking update failed: " + e.getMessage());
        }catch (SQLException e){
            System.out.println("A database error occurred while updating the shipment.");
        }
    }

    public static void viewTrackingHistory(Scanner sc){
        try {
            long confirmShipmentId = InputValidator.getPositiveLong(sc, "Enter your shipment ID: ");

            List<TrackingUpdate> viewHistory = TrackingService.viewTrackingHistory(confirmShipmentId);
            UserFriendlyDisplay.displayTrackingHistory(viewHistory);
        }catch (SQLException e){
            System.out.println("A database exception occurred while viewing tracking history.");
        }
    }

    public static void viewRegisteredFarmer(){
        try {
            List<Farmer> registeredFarmer = FarmerService.viewRegisteredFarmer();
            UserFriendlyDisplay.displayFarmer(registeredFarmer);
        }catch (SQLException e){
            System.out.println("A database error occurred while viewing the farmers.");
        }
    }

    public static void viewCassavaBatch(){
        try {
            List<CassavaBatch> viewCassavaBatches = CassavaBatchService.viewCassavaBatch();
            UserFriendlyDisplay.viewCassavaBatch(viewCassavaBatches);
        }catch (SQLException e){
            System.out.println("A database error occurred while viewing cassava batches. ");
        }
    }

    public static void viewShipment(){
        try {
            List<Shipping> viewShipment = ShipmentService.viewShipment();
            UserFriendlyDisplay.displayShipments(viewShipment);
        }catch (SQLException e){
            System.out.println("A database error occurred while viewing shipment.");
        }
    }

    public static void searchBatch(Scanner sc){
        try {
            String searchBatchCode = InputValidator.getRequiredText(sc, "Enter batch code for search: ");

            List<CassavaBatch> viewSearch = CassavaBatchService.searchByBatchCode(searchBatchCode);
            UserFriendlyDisplay.searchBatch(viewSearch);
        }catch (SQLException e){
            System.out.println("A database error occurred while searching cassava batch.");
        }
    }

    public static void viewCassavaJourney(Scanner sc) {

        try {
            long batchId = InputValidator.getPositiveLong(sc, "Enter batch ID: ");
            CassavaJourney journey = TrackingService.viewCassavaJourney(batchId);

            if (journey == null) {
                System.out.println("Cassava batch with ID " + batchId + " does not exist.");
                return;
            }
            UserFriendlyDisplay.displayCassavaJourney(journey);
        } catch (SQLException e) {

            System.out.println("A database error occurred while viewing the cassava journey.");
        }
    }
}
