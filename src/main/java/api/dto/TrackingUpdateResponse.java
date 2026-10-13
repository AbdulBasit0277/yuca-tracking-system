package api.dto;

import java.sql.Timestamp;

public class TrackingUpdateResponse {

    private long id;
    private long shipmentId;
    private String location;
    private String status;
    private Timestamp updateTime;

    public TrackingUpdateResponse() {
    }

    public TrackingUpdateResponse(long id, long shipmentId, String location, String status, Timestamp updateTime) {
        this.id = id;
        this.shipmentId = shipmentId;
        this.location = location;
        this.status = status;
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
