package service;

import configuration.DbConfig;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrackingService {
    private static Connection con() {
        return DbConfig.getConnection();
    }

    // add tracking update
    public static void addTrackingUpdate(TrackingUpdate trackingUpdate) throws SQLException {

        String status = trackingUpdate.getStatus().trim().toUpperCase().replace(" ", "_");
        if (!status.equals("IN_TRANSIT") && !status.equals("ARRIVED")) {
            throw new IllegalArgumentException("Invalid tracking status, use IN TRANSIT or ARRIVED");
        }
        Connection connection = con();
//        PreparedStatement pst;
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement pst = connection.prepareStatement("select id, status from shipments where id = ?")) {
                pst.setLong(1, trackingUpdate.getShipmentId());
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Shipment with ID " + trackingUpdate.getShipmentId() + " does not exist.");
                    }
                    String currentStatus = rs.getString("status");
                    if (currentStatus == null) {
                        throw new IllegalStateException("Shipment has no current status.");
                    }
                    // shipment lifecycle process
                    currentStatus = currentStatus.toUpperCase().trim();
                    if (currentStatus.equals("CREATED") && !status.equals("IN_TRANSIT")) {
                        throw new IllegalStateException("A CREATED shipment must move to IN_TRANSIT first.");
                    }
                    if (currentStatus.equals("IN_TRANSIT") && !status.equals("ARRIVED")) {
                        throw new IllegalStateException("An IN_TRANSIT shipment must move to ARRIVED.");
                    }
                    if (currentStatus.equals("ARRIVED")) {
                        throw new IllegalStateException("An ARRIVED shipment cannot receive another tracking update.");
                    }
                }
            }
            // save tracking history
            try (PreparedStatement pst = connection.prepareStatement("insert into tracking_updates(shipment_id, location, status, update_time) values (?,?,?, current_timestamp)")) {
                pst.setLong(1, trackingUpdate.getShipmentId());
                pst.setString(2, trackingUpdate.getLocation());
                pst.setString(3, status);
                pst.executeUpdate();
            }

            // update current shipment state
            try (PreparedStatement pst = connection.prepareStatement("update shipments set current_location = ?, status = ?, update_time = current_timestamp, arrival_time = case when upper(?) = 'ARRIVED' then current_timestamp else arrival_time end where id = ?")) {
                pst.setString(1, trackingUpdate.getLocation());
                pst.setString(2, status);
                pst.setString(3, status);
                pst.setLong(4, trackingUpdate.getShipmentId());
                pst.executeUpdate();
            }

            // update current cassava batch
            String batchStatus = status.equals("IN_TRANSIT") ? "In TRANSIT" : "ARRIVED";
            try (PreparedStatement pst = connection.prepareStatement("update cassava_batches set status = ? where id = (select batch_id from shipments where id = ?)")) {
                pst.setString(1, batchStatus);
                pst.setLong(2, trackingUpdate.getShipmentId());
                pst.executeUpdate();
            }

            // commit everything
            connection.commit();
            System.out.println("Tracking information updated successfully.");
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }

    }

    // View Tracking History
    public static List<TrackingUpdate> viewTrackingHistory(long shipmentId) throws SQLException {
        Connection connection = con();
        try (PreparedStatement pst = connection.prepareStatement("select id from shipments where id = ?")) {
            pst.setLong(1, shipmentId);
            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("shipment ID " + shipmentId + " does not exist.");
                    return new ArrayList<>();
                }
            }
        }

//        pst = con().prepareStatement("update cassava_batches set status = 'delivered' where id = ?");
//        pst.setLong(1, shipmentId);
//        pst.executeUpdate();

        try (PreparedStatement pst = connection.prepareStatement("select id, shipment_id, location, status, update_time from tracking_updates where shipment_id = ? order by id")) {
            pst.setLong(1, shipmentId);
            try (ResultSet rs = pst.executeQuery()) {
                List<TrackingUpdate> trackingUpdates = new ArrayList<>();
                while (rs.next()) {
                    TrackingUpdate trackingUpdate = new TrackingUpdate();
                    trackingUpdate.setId(rs.getLong("id"));
                    trackingUpdate.setShipmentId(rs.getLong("shipment_id"));
                    trackingUpdate.setLocation(rs.getString("location"));
                    trackingUpdate.setStatus(rs.getString("status"));
                    trackingUpdate.setUpdateTime(rs.getTimestamp("update_time"));
                    trackingUpdates.add(trackingUpdate);
                }
                return trackingUpdates;
            }
        } finally {
            connection.close();
        }
    }

    // viewing complete cassava journey process
    public static CassavaJourney viewCassavaJourney(long batchId) throws SQLException {
        Connection connection = con();
        try {
            CassavaBatch batch = null;
            Farmer farmer = null;
            Shipping shipment = null;
            List<TrackingUpdate> trackingHistory = new ArrayList<>();

            String batchQuery =
                    "select " +
                            "cb.id as batch_id, cb.batch_code, cb.farmer_id, " +
                            "cb.quantity, cb.harvest_date, cb.status as batch_status, " +
                            "f.id as farmer_id, f.name as farmer_name, " +
                            "f.phone as farmer_phone, f.location as farmer_location, " +
                            "s.id as shipment_id, s.batch_id as shipment_batch_id, " +
                            "s.driver_name, s.vehicle_number, s.destination, " +
                            "s.departure_time, s.arrival_time, s.current_location, " +
                            "s.status as shipment_status, s.update_time " +
                            "from cassava_batches cb " +
                            "join farmers f on cb.farmer_id = f.id " +
                            "left join shipments s on cb.id = s.batch_id " +
                            "where cb.id = ? " +
                            "order by s.id desc " +
                            "limit 1";

            try (PreparedStatement pst = connection.prepareStatement(batchQuery)) {
                pst.setLong(1, batchId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    batch = new CassavaBatch();
                    batch.setId(rs.getLong("batch_id"));
                    batch.setBatchCode(rs.getString("batch_code"));
                    batch.setFarmerId(rs.getLong("farmer_id"));
                    batch.setQuantity(rs.getLong("quantity"));
                    batch.setHarvestDate(rs.getDate("harvest_date").toLocalDate());
                    batch.setStatus(rs.getString("batch_status"));

                    farmer = new Farmer();
                    farmer.setId(rs.getLong("farmer_id"));
                    farmer.setName(rs.getString("farmer_name"));
                    farmer.setPhoneNumber(rs.getString("farmer_phone"));
                    farmer.setLocation(rs.getString("farmer_location"));

                    long shipmentId = rs.getLong("shipment_id");
                    if (!rs.wasNull()) {
                        shipment = new Shipping();

                        shipment.setId(shipmentId);
                        shipment.setBatchId(rs.getLong("shipment_batch_id"));
                        shipment.setDriverName(rs.getString("driver_name"));
                        shipment.setVehicleNumber(rs.getString("vehicle_number"));
                        shipment.setDestination(rs.getString("destination"));
                        shipment.setDepartureTime(rs.getTimestamp("departure_time"));
                        shipment.setArrivalTime(rs.getTimestamp("arrival_time"));
                        shipment.setCurrentLocation(rs.getString("current_location"));
                        shipment.setStatus(rs.getString("shipment_status"));
                        shipment.setUpdateTime(rs.getTimestamp("update_time"));
                    }
                }
            }

            if (shipment != null) {

                String trackingQuery = "select id, shipment_id, location, status, update_time from tracking_updates where shipment_id = ? order by id";
                try (PreparedStatement pst = connection.prepareStatement(trackingQuery)) {
                    pst.setLong(1, shipment.getId());
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            TrackingUpdate trackingUpdate = new TrackingUpdate();

                            trackingUpdate.setId(rs.getLong("id"));
                            trackingUpdate.setShipmentId(rs.getLong("shipment_id"));
                            trackingUpdate.setLocation(rs.getString("location"));
                            trackingUpdate.setStatus(rs.getString("status"));
                            trackingUpdate.setUpdateTime(rs.getTimestamp("update_time"));
                            trackingHistory.add(trackingUpdate);
                        }
                    }
                }
            }
            return new CassavaJourney(batch, farmer, shipment, trackingHistory);
        } finally {
            connection.close();
        }
    }
}
