package dao;

public class Address {
    private long address_id;
    private String line_1;
    private String line_2;
    private String city;
    private String state;
    private String zip;

    public Address(long address_id, String line_1, String line_2, String city, String state, String zip)
    {
        this.address_id = address_id;
        this.line_1 = line_1;
        this.line_2 = line_2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String toString()
    {
        String street_address = !line_2.equals("") ? line_1 + " " + line_2 : line_1;

        return street_address  + ", " + city + ", " + state;
    }

    public long getAddressId()
    {
        return address_id;
    }

    public String getLine1() {
        return line_1;
    }

    public void setLine1(String line_1) {
        this.line_1 = line_1;
    }

    public String getLine2() {
        return line_2;
    }

    public void setLine2(String line_2) {
        this.line_2 = line_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
