package dao;

import util.DBManager;
import util.IOManager;

import java.sql.*;

public class Card {
    protected long card_id;
    protected long person_id;
    protected String type;
    protected long card_number;
    protected int cvc;
    protected String status;
    protected Timestamp created;
    protected Timestamp modified;

    /**
     * Internal constructor for creating using data fetched from a record in the db
     * @param card_id
     * @param person_id
     * @param type
     * @param card_number
     * @param cvc
     * @param status
     * @param created
     * @param modified
     */
    protected Card(long card_id, long person_id, String type, long card_number, int cvc, String status, Timestamp created, Timestamp modified)
    {
        this.card_id = card_id;
        this.person_id = person_id;
        this.type = type;
        this.card_number = card_number;
        this.cvc = cvc;
        this.status = status;
        this.created = created;
        this.modified = modified;
    }

    /**
     * Lookup based on the card number and cvc
     * @param card_number
     * @param cvc
     * @return Card object with matching card_number and cvc from db or null if error
     */
    public static Card fromCardNumber(long card_number, int cvc)
    {
        Connection conn = DBManager.getConnection();
        Card card = null;

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT " +
                            "c.card_id, c.person_id, c.type, c.card_number, c.cvc, c.status, c.created, c.modified, cc.interest_rate, cc.credit_limit, cd.account_id " +
                            "FROM card c " +
                            "LEFT JOIN card_credit cc " +
                            "ON c.card_id = cc.card_id " +
                            "LEFT JOIN card_debit cd " +
                            "ON c.card_id = cd.card_id " +
                            "WHERE c.card_number = ?" +
                            "AND c.cvc = ?");

            ps.setLong(1, card_number);
            ps.setInt(2, cvc);

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                card = new Card(result.getLong("card_id"),
                        result.getLong("person_id"),
                        result.getString("type"),
                        result.getLong("card_number"),
                        result.getInt("cvc"),
                        result.getString("status"),
                        result.getTimestamp("created"),
                        result.getTimestamp("modified"));

                switch (card.type)
                {
                    case "credit":
                        card = new Card_Credit(card, result.getDouble("interest_rate"), result.getDouble("credit_limit"));
                        break;
                    case "debit":
                        card = new Card_Debit(card, result.getLong("account_id"));
                        break;
                    default:
                        return null;
                }
            }
            else {
                return null;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return card;
    }

    /**
     * Use card to create transaction to pay a vendor a specific amount
     * @param amount
     * @param vendor
     * @return boolean of whether the payment was successful
     */
    public boolean pay(double amount, Vendor vendor)
    {
        if (vendor == null)
        {
            return false;
        }

        Transaction_External payment = new Transaction_External(amount, this, vendor);
        return payment.save();
    }

    /**
     * Placeholder method used to be able to access getAvailableBalance methods in child objects without casting
     * @return the amount of money available to be spent from the card
     */
    public double getAvailableBalance()
    {
        return 0;
    }

    /**
     * @return a string representation of the Card object of the form "CREDIT CARD ending in ************1234 Balance: $123.45"
     */
    public String toString()
    {
        return this.type.toUpperCase() + " CARD ending in ************" + this.getLastFour() + " Balance: " + IOManager.formatCurrency(this.getAvailableBalance());
    }

    /**
     * @return the last four digits of the card number
     */
    public long getLastFour()
    {
        return card_number % 10000;
    }


    // Getters and Setters

    public long getCardId() {
        return card_id;
    }

    public long getPersonId() {
        return person_id;
    }

    public String getType() {
        return type;
    }

    public long getCardNumber() {
        return card_number;
    }

    public int getCVC() {
        return cvc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}
