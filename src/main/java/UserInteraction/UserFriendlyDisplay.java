package UserInteraction;

import model.*;

import java.util.List;

public class UserFriendlyDisplay {
    public static void displayTrackingHistory(List<TrackingUpdate> history){
        if (history.isEmpty()){
            System.out.println("No tracking history found.");
            return;
        }
        System.out.println("===== TRACKING HISTORY =====");

        for (TrackingUpdate update : history) {

            System.out.println("Tracking ID : " + update.getId());
            System.out.println("Location    : " + update.getLocation());
            System.out.println("Status      : " + update.getStatus());
            System.out.println("Updated At  : " + update.getUpdateTime());
            System.out.println("----------------------------");
        }
    }

    public static void displayShipments(List<Shipping> shipments) {

        if (shipments.isEmpty()) {
            System.out.println("No shipments found.");
            return;
        }

        System.out.println("===== SHIPMENTS =====");

        for (Shipping shipment : shipments) {

            System.out.println("Shipment ID    : " + shipment.getId());
            System.out.println("Batch ID       : " + shipment.getBatchId());
            System.out.println("Driver         : " + shipment.getDriverName());
            System.out.println("Vehicle        : " + shipment.getVehicleNumber());
            System.out.println("Destination    : " + shipment.getDestination());
            System.out.println("Departure Time : " + shipment.getDepartureTime());
            System.out.println("Arrival Time   : " + shipment.getArrivalTime());
            System.out.println("Current Place  : " + shipment.getCurrentLocation());
            System.out.println("Status         : " + shipment.getStatus());
            System.out.println("Updated At     : " + shipment.getUpdateTime());
            System.out.println("----------------------------");
        }
    }
    public static void displayFarmer(List<Farmer> farmers){
        if (farmers.isEmpty()){
            System.out.println("No farmer found.");
            return;
        }
        System.out.println("==== FARMERS ====");
        for (Farmer farmer : farmers){
            System.out.println("Farmer ID :  " + farmer.getId());
            System.out.println("Name      :  " + farmer.getName());
            System.out.println("Phone no  :  " + farmer.getPhoneNumber());
            System.out.println("Location  :  " + farmer.getLocation());
            System.out.println("--------------------------------");
        }
    }

    public static void viewCassavaBatch(List<CassavaBatch> batches){
        if (batches.isEmpty()){
            System.out.println("No cassava batch found.");
            return;
        }
        System.out.println("==== CASSAVA BATCHES ====");
        for (CassavaBatch batch : batches){
            System.out.println("Batch ID        : " + batch.getId());
            System.out.println("Batch code      : " + batch.getBatchCode());
            System.out.println("Farmer ID       : " + batch.getFarmerId());
            System.out.println("Quantity        : " + batch.getQuantity());
            System.out.println("Harvested Date  : " + batch.getHarvestDate());
            System.out.println("Status          : " + batch.getStatus());
            System.out.println("--------------------------------");
        }
    }

    public static void searchBatch(List<CassavaBatch> searchBatches){
        if (searchBatches.isEmpty()){
            System.out.println("No result found.");
            return;
        }
        System.out.println("==== SEARCH RESULT ====");
        for (CassavaBatch batch : searchBatches){
            System.out.println("Batch ID        : " + batch.getId());
            System.out.println("Batch code      : " + batch.getBatchCode());
            System.out.println("Farmer ID       : " + batch.getFarmerId());
            System.out.println("Quantity        : " + batch.getQuantity());
            System.out.println("Harvested Date  : " + batch.getHarvestDate());
            System.out.println("Status          : " + batch.getStatus());
            System.out.println("--------------------------------");
        }
    }

    public static void displayCassavaJourney(CassavaJourney journey) {

        System.out.println();
        System.out.println("========== CASSAVA JOURNEY ==========");

        CassavaBatch batch = journey.getBatch();
        Farmer farmer = journey.getFarmer();
        Shipping shipment = journey.getShipment();

        System.out.println();
        System.out.println("Batch");
        System.out.println("--------------------------------------");
        System.out.println("Batch ID     : " + batch.getId());
        System.out.println("Batch Code   : " + batch.getBatchCode());
        System.out.println("Quantity     : " + batch.getQuantity() + " kg");
        System.out.println("Harvest Date : " + batch.getHarvestDate());
        System.out.println("Status       : " + batch.getStatus());

        System.out.println();
        System.out.println("Farmer");
        System.out.println("--------------------------------------");
        System.out.println("Farmer ID    : " + farmer.getId());
        System.out.println("Name         : " + farmer.getName());
        System.out.println("Phone        : " + farmer.getPhoneNumber());
        System.out.println("Location     : " + farmer.getLocation());

        System.out.println();

        if (shipment == null) {
            System.out.println("Shipment");
            System.out.println("--------------------------------------");
            System.out.println("No shipment has been created for this batch yet.");
            System.out.println();
            return;
        }

        System.out.println("Shipment");
        System.out.println("--------------------------------------");
        System.out.println("Shipment ID  : " + shipment.getId());
        System.out.println("Driver       : " + shipment.getDriverName());
        System.out.println("Vehicle      : " + shipment.getVehicleNumber());
        System.out.println("Destination  : " + shipment.getDestination());
        System.out.println("Departure    : " + shipment.getDepartureTime());
        System.out.println("Arrival      : " + shipment.getArrivalTime());
        System.out.println("Location     : " + shipment.getCurrentLocation());
        System.out.println("Status       : " + shipment.getStatus());

        System.out.println();
        System.out.println("Tracking History");
        System.out.println("--------------------------------------");

        List<TrackingUpdate> history = journey.getTrackingHistory();

        if (history.isEmpty()) {
            System.out.println("No tracking updates available.");
            return;
        }

        for (TrackingUpdate update : history) {
            System.out.println(update.getStatus() + "  →  " + update.getLocation() + "  |  " + update.getUpdateTime());
        }
        System.out.println("======================================");
    }
}
