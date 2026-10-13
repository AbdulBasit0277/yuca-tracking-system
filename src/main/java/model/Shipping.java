package model;

import java.sql.Timestamp;

public class Shipping {
    private long id;
    private long batchId;
    private String driverName;
    private String vehicleNumber;
    private String destination;
    private Timestamp departureTime;
    private Timestamp arrivalTime;
    private String currentLocation;
    private String status;
    private Timestamp updateTime;

    public Shipping() {
    }

    public Shipping(long batchId, String driverName, String vehicleNumber, String destination, Timestamp departureTime, Timestamp arrivalTime, String currentLocation, String status, Timestamp updateTime) {
        this.batchId = batchId;
        this.driverName = driverName;
        this.vehicleNumber = vehicleNumber;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.currentLocation = currentLocation;
        this.status = status;
        this.updateTime = updateTime;
    }

    public Shipping(long batchId, String driverName, String vehicleNumber, String destination) {
        this.batchId = batchId;
        this.driverName = driverName;
        this.vehicleNumber = vehicleNumber;
        this.destination = destination;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBatchId() {
        return batchId;
    }

    public void setBatchId(long batchId) {
        this.batchId = batchId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Timestamp getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Timestamp departureTime) {
        this.departureTime = departureTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
