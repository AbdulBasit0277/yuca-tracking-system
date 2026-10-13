package api.dto;

import java.time.LocalDate;
import java.util.List;

public class CassavaJourneyResponse {

    private BatchResponse batch;
    private FarmerResponse farmer;
    private ShipmentResponse shipment;
    private List<TrackingUpdateResponse> trackingHistory;

    public CassavaJourneyResponse() {
    }

    public BatchResponse getBatch() {
        return batch;
    }

    public void setBatch(BatchResponse batch) {
        this.batch = batch;
    }

    public FarmerResponse getFarmer() {
        return farmer;
    }

    public void setFarmer(FarmerResponse farmer) {
        this.farmer = farmer;
    }

    public ShipmentResponse getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentResponse shipment) {
        this.shipment = shipment;
    }

    public List<TrackingUpdateResponse> getTrackingHistory() {
        return trackingHistory;
    }

    public void setTrackingHistory(
            List<TrackingUpdateResponse> trackingHistory) {

        this.trackingHistory = trackingHistory;
    }
}