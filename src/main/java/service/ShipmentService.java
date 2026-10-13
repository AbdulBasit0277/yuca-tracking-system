package service;

import configuration.DbConfig;
import model.Shipping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipmentService {
    private static Connection con(){
        return DbConfig.getConnection();
    }
    // creating shipping
    public static long createShipment(Shipping shipment) throws SQLException {
        Connection connection = con();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement pst = connection.prepareStatement("select id from cassava_batches where id = ?")) {
                pst.setLong(1, shipment.getBatchId());
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Cassava batch with ID " + shipment.getBatchId() + " does not exist.");
                        connection.rollback();
                        return -1;
                    }
                }
            }

            long shipmentId;
            try (PreparedStatement pst = connection.prepareStatement("insert into shipments(batch_id, driver_name,vehicle_number,destination, departure_time, status, update_time) values (?,?,?,?,current_timestamp,'CREATED', current_timestamp) returning id")) {
                pst.setLong(1, shipment.getBatchId());
                pst.setString(2, shipment.getDriverName());
                pst.setString(3, shipment.getVehicleNumber());
                pst.setString(4, shipment.getDestination());
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Fail to create shipment.");
                    }
                    shipmentId = rs.getLong("id");
                }
            }
            // creating first tracking record
            try (PreparedStatement pst = connection.prepareStatement("insert into tracking_updates(shipment_id,location,status,update_time) values (?, null, 'CREATED', current_timestamp)")) {
                pst.setLong(1, shipmentId);
                pst.executeUpdate();
            }

            // commiting both operations
            connection.commit();
            System.out.println("Shipment and initial tracking record created successfully.");
            return shipmentId;
        }catch (SQLException e){
            connection.rollback();
            throw e;
        }finally {
            connection.setAutoCommit(true);
            connection.close();
        }


    }
    // view shipment
    public static List<Shipping> viewShipment() throws SQLException {
        Connection connection = con();
        try (PreparedStatement pst = connection.prepareStatement("select * from shipments");
             ResultSet rs = pst.executeQuery()) {
            List<Shipping> shippings = new ArrayList<>();
            while (rs.next()) {
                Shipping shipping = new Shipping();
                shipping.setId(rs.getLong("id"));
                shipping.setBatchId(rs.getLong("batch_id"));
                shipping.setDriverName(rs.getString("driver_name"));
                shipping.setVehicleNumber(rs.getString("vehicle_number"));
                shipping.setDestination(rs.getString("destination"));
                shipping.setDepartureTime(rs.getTimestamp("departure_time"));
                shipping.setArrivalTime(rs.getTimestamp("arrival_time"));
                shipping.setCurrentLocation(rs.getString("current_location"));
                shipping.setStatus(rs.getString("status"));
                shipping.setUpdateTime(rs.getTimestamp("update_time"));
                shippings.add(shipping);
            }
            return shippings;
        } finally {
            connection.close();
        }
    }

    public static Shipping getShipmentById(long shipmentId) throws SQLException {

        Connection connection = con();

        try (PreparedStatement pst = connection.prepareStatement("select * from shipments where id = ?")) {
            pst.setLong(1, shipmentId);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Shipping shipping = new Shipping();

                shipping.setId(rs.getLong("id"));
                shipping.setBatchId(rs.getLong("batch_id"));
                shipping.setDriverName(rs.getString("driver_name"));
                shipping.setVehicleNumber(rs.getString("vehicle_number"));
                shipping.setDestination(rs.getString("destination"));
                shipping.setDepartureTime(rs.getTimestamp("departure_time"));
                shipping.setArrivalTime(rs.getTimestamp("arrival_time"));
                shipping.setCurrentLocation(rs.getString("current_location"));
                shipping.setStatus(rs.getString("status"));
                shipping.setUpdateTime(rs.getTimestamp("update_time"));
                return shipping;
            }
        } finally {
            connection.close();
        }
    }
}
