package dao;

public class Branch {
    private long branch_id;
    private String type;
    private Address address;

    public Branch(long branch_id, String type, Address address)
    {
        this.branch_id = branch_id;
        this.type = type;
        this.address = address;
    }

    public String toString()
    {
        if (this.type.equals("atm"))
        {
            return "ATM at " + this.address.toString();
        }
        else {
            return "BRANCH at " + this.address.toString();
        }
    }
}
