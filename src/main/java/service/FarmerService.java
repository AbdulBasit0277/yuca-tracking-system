package service;

import configuration.DbConfig;
import model.Farmer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FarmerService {
    private static Connection con(){
        return DbConfig.getConnection();
    }
    // Register Farmer
    public static void registerFarmer(Farmer farmer) throws SQLException {
        try (Connection connection = con()) {
            try (PreparedStatement pst = connection.prepareStatement("insert into farmers(name, phone, location) values (?,?,?)")) {
                pst.setString(1, farmer.getName());
                pst.setString(2, farmer.getPhoneNumber());
                pst.setString(3, farmer.getLocation());
                pst.executeUpdate();
            } finally {
                connection.close();
            }
        }
    }
    // view farmers
    public static List<Farmer> viewRegisteredFarmer() throws SQLException {
        try (Connection connection = con()) {
            try (PreparedStatement pst = connection.prepareStatement("select * from farmers");
                 ResultSet rs = pst.executeQuery()) {
                List<Farmer> farmers = new ArrayList<>();
                while (rs.next()) {
                    Farmer farmer = new Farmer();
                    farmer.setId(rs.getLong("id"));
                    farmer.setName(rs.getString("name"));
                    farmer.setPhoneNumber(rs.getString("phone"));
                    farmer.setLocation(rs.getString("location"));
                    farmers.add(farmer);
                }
                return farmers;
            }
        }
    }
}
