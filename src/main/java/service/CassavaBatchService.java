package service;

import configuration.DbConfig;
import model.CassavaBatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CassavaBatchService {
    private static Connection con(){
        return DbConfig.getConnection();
    }
    // creating batch for cassava
    public static long createBatch(CassavaBatch batch) throws SQLException {
        try(Connection connection = con()) {
            try (PreparedStatement pst = connection.prepareStatement("select id from farmers where id = ?")) {
                pst.setLong(1, batch.getFarmerId());
                try (ResultSet rs = pst.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalArgumentException("Farmer with ID " + batch.getFarmerId() + " has not been registered.");
                    }
                }
            }
            try (PreparedStatement pst = connection.prepareStatement("insert into cassava_batches(batch_code, farmer_id, quantity, harvest_date, status) values (?,?,?,?,'created') returning id")) {
                pst.setString(1, batch.getBatchCode());
                pst.setLong(2, batch.getFarmerId());
                pst.setLong(3, batch.getQuantity());
                pst.setDate(4, java.sql.Date.valueOf(batch.getHarvestDate()));
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                }
            }
        }
        throw new SQLException("Fail to create cassava batch.");
    }

    // Get batch by ID
    public static CassavaBatch getBatchById(long batchId) throws SQLException {

        Connection connection = con();

        try (PreparedStatement pst = connection.prepareStatement("select id, batch_code, farmer_id, quantity, harvest_date, status from cassava_batches where id = ?")) {

            pst.setLong(1, batchId);

            try (ResultSet rs = pst.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                CassavaBatch batch = new CassavaBatch();
                batch.setId(rs.getLong("id"));
                batch.setBatchCode(rs.getString("batch_code"));
                batch.setFarmerId(rs.getLong("farmer_id"));
                batch.setQuantity(rs.getLong("quantity"));
                batch.setHarvestDate(rs.getDate("harvest_date").toLocalDate());
                batch.setStatus(rs.getString("status"));
                return batch;
            }
        } finally {
            connection.close();
        }
    }
    // view cassava batch
    public static List<CassavaBatch> viewCassavaBatch() throws SQLException {
        Connection connection = con();
        try (PreparedStatement pst = connection.prepareStatement("select * from cassava_batches");
             ResultSet rs = pst.executeQuery()) {
            List<CassavaBatch> batches = new ArrayList<>();
            while (rs.next()) {
                CassavaBatch batch = new CassavaBatch();
                batch.setId(rs.getLong("id"));
                batch.setBatchCode(rs.getString("batch_code"));
                batch.setFarmerId(rs.getLong("farmer_id"));
                batch.setQuantity(rs.getLong("quantity"));
                batch.setHarvestDate(rs.getDate("harvest_date").toLocalDate());
                batch.setStatus(rs.getString("status"));
                batches.add(batch);
            }
            return batches;
        } finally {
            connection.close();
        }
    }
    // search batch by batch code
    public static List<CassavaBatch> searchByBatchCode(String batchCode) throws SQLException {
        Connection connection = con();
        try (PreparedStatement pst = connection.prepareStatement("select * from cassava_batches where lower(batch_code) like concat('%', lower(?) , '%') order by batch_code")){
        pst.setString(1, batchCode);
        try (ResultSet rs = pst.executeQuery()) {
            List<CassavaBatch> batches = new ArrayList<>();
            while (rs.next()) {
                CassavaBatch batch = new CassavaBatch();
                batch.setId(rs.getLong("id"));
                batch.setBatchCode(rs.getString("batch_code"));
                batch.setFarmerId(rs.getLong("farmer_id"));
                batch.setQuantity(rs.getLong("quantity"));
                batch.setHarvestDate(rs.getDate("harvest_date").toLocalDate());
                batch.setStatus(rs.getString("status"));
                batches.add(batch);
            }
            return batches;
        }
        }finally {
            connection.close();
        }
    }
    // show complete journey of cassava
}
