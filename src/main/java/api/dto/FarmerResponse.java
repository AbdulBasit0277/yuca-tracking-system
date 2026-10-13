package api.dto;

public class FarmerResponse {

    private long farmerId;
    private String name;
    private String phoneNumber;
    private String location;

    public FarmerResponse() {
    }

    public FarmerResponse(long farmerId, String name, String phoneNumber, String location) {

        this.farmerId = farmerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(long farmerId) {
        this.farmerId = farmerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
