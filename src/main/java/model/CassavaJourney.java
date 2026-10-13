package model;

import java.util.List;

public class CassavaJourney {

    private CassavaBatch batch;
    private Farmer farmer;
    private Shipping shipment;
    private List<TrackingUpdate> trackingHistory;

    public CassavaJourney() {
    }

    public CassavaJourney(CassavaBatch batch, Farmer farmer, Shipping shipment, List<TrackingUpdate> trackingHistory) {
        this.batch = batch;
        this.farmer = farmer;
        this.shipment = shipment;
        this.trackingHistory = trackingHistory;
    }

    public CassavaBatch getBatch() {
        return batch;
    }

    public void setBatch(CassavaBatch batch) {
        this.batch = batch;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Shipping getShipment() {
        return shipment;
    }

    public void setShipment(Shipping shipment) {
        this.shipment = shipment;
    }

    public List<TrackingUpdate> getTrackingHistory() {
        return trackingHistory;
    }

    public void setTrackingHistory(List<TrackingUpdate> trackingHistory) {
        this.trackingHistory = trackingHistory;
    }
}
