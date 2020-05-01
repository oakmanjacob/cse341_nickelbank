package dao;

import oracle.jdbc.OraclePreparedStatement;
import util.DBManager;

import java.sql.*;

public class Vendor {
    private long vendor_id;
    private String name;

    public Vendor(String name)
    {
        this.vendor_id = 0;
        this.name = name;
    }

    public Vendor(long vendor_id, String name)
    {
        this.vendor_id = vendor_id;
        this.name = name;
    }

    /**
     * Save a new vendor to the database
     * @return whether the save was successful
     */
    public boolean save()
    {
        Connection conn = DBManager.getConnection();

        if (this.vendor_id == 0) {
            String query = "insert into vendor" +
                    "(name)" +
                    "values (?) returning vendor_id into ?";
            try (
                    OraclePreparedStatement ps = (OraclePreparedStatement)conn.prepareStatement(query);
            ) {
                ps.setString(1, this.name);
                ps.registerReturnParameter(2, Types.NUMERIC);

                if (ps.executeUpdate() == 0) {
                    return false;
                }

                ResultSet rs = ps.getReturnResultSet();

                if (rs != null && rs.next()) {
                    this.vendor_id = rs.getLong(1);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Lookup vendor in the database via a name
     * Match case insensitive
     * @param name
     * @return the Vendor found or null if not found
     */
    public static Vendor fromName(String name)
    {
        Connection conn = DBManager.getConnection();
        Vendor vendor = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT v.vendor_id, v.name " +
                        "FROM vendor v " +
                        "WHERE LOWER(v.name) = LOWER(?)");

            ps.setString(1, name);

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                vendor = new Vendor(
                        result.getLong("vendor_id"),
                        result.getString("name")
                );
            }
            else {
                return null;
            }
        }
        catch (SQLException e)
        {
            return null;
        }

        return vendor;
    }


    // Getters and Setters
    public long getVendorId() {
        return vendor_id;
    }

    public String getName() {
        return name;
    }
}
