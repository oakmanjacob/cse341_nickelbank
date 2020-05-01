package dao;

import util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Get all the branches currently in the database
     * @return A list of branch objects, the list will be empty if query failed
     */
    public static List<Branch> getAllBranch()
    {
        Connection conn = DBManager.getConnection();
        List<Branch> branch_list = new ArrayList<Branch>();

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT b.branch_id, b.type, a.*" +
                            "FROM branch b " +
                            "INNER JOIN address a " +
                            "ON b.address_id = a.address_id " +
                            "ORDER BY b.branch_id ASC");

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                do {
                    Address new_address = new Address(
                            result.getLong("address_id"),
                            result.getString("line_1"),
                            result.getString("line_2"),
                            result.getString("city"),
                            result.getString("state"),
                            result.getString("zip")
                    );

                    branch_list.add(new Branch(
                            result.getLong("branch_id"),
                            result.getString("type"),
                            new_address));
                }
                while (result.next());
            }
        }
        catch (SQLException e)
        {
            return new ArrayList<Branch>();
        }

        return branch_list;
    }

    /**
     * @return a representation of the branch of the form "ATM at 4 Farrington Square, Bethlehem, PA"
     */
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

    // Getters and Setters

    public long getBranchId() {
        return branch_id;
    }

    public void setBranchId(long branch_id) {
        this.branch_id = branch_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddress()
    {
        return this.address;
    }
}
