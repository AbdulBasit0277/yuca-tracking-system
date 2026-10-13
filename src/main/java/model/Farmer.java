package model;

public class Farmer {
    private long id;
    private String name;
    private String phoneNumber;
    private String location;

    public Farmer() {
    }

    public Farmer(String name, String phoneNumber, String location){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
