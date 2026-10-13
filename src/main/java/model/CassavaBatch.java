package model;

import java.time.LocalDate;
public class CassavaBatch {
    private long id;
    private String batchCode;
    private long farmerId;
    private long quantity;
    private LocalDate harvestDate;
    private String status;

    public CassavaBatch() {
    }

    public CassavaBatch(String batchCode, long farmerId, long quantity, LocalDate harvestDate, String status) {
        this.batchCode = batchCode;
        this.farmerId = farmerId;
        this.quantity = quantity;
        this.harvestDate = harvestDate;
        this.status = status;
    }

    public CassavaBatch(String batchCode, long farmerId, long quantity, LocalDate harvestDate) {
        this.batchCode = batchCode;
        this.farmerId = farmerId;
        this.quantity = quantity;
        this.harvestDate = harvestDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(long farmerId) {
        this.farmerId = farmerId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
