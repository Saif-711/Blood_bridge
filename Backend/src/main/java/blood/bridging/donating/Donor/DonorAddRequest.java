package blood.bridging.donating.Donor;

public class DonorAddRequest {
    private String name;
    private String bloodType;
    private String location;
    private boolean available;

    public DonorAddRequest() {}

    public DonorAddRequest(String name, String bloodType, String location, boolean available) {
        this.name = name;
        this.bloodType = bloodType;
        this.location = location;
        this.available = available;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}