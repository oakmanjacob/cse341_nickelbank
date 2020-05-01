package dao;

import util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class Transaction_External extends Transaction {
    private Card card;
    private Vendor vendor;

    public Transaction_External(double amount, Card card, Vendor vendor)
    {
        super(amount, Person.fromPersonId(card.person_id), null, "external", "pending");
        this.card = card;
        this.vendor = vendor;
    }

    /**
     * Save a new external transaction to the database, handling fees and failing if the transaction would overdraw
     * the card
     * @return boolean whether the transaction has been successfully saved
     */
    public boolean save()
    {
        Connection conn = DBManager.getConnection();

        if (this.vendor != null) {
            String query = "insert into transaction_external" +
                    "(transaction_id, card_id, vendor_id)" +
                    "values (?, ?, ?)";
            try (
                    PreparedStatement ps = conn.prepareStatement(query);
            ) {
                conn.setAutoCommit(false);

                if (vendor.getVendorId() == 0)
                {
                    if (!vendor.save())
                    {
                        DBManager.rollbackAndResetAutoCommit();
                        return false;
                    }
                }

                // Block transactions which will set the sending account to less than $0
                boolean fee = false;
                if (card.getAvailableBalance() < amount)
                {
                    DBManager.rollbackAndResetAutoCommit();
                    return false;
                }
                else if (card instanceof Card_Debit && ((Card_Debit) card).getAccount().getBalance() - amount < ((Card_Debit) card).getAccount().getMinBalance())
                {
                    fee = true;
                }

                if (!super.save()) {
                    DBManager.rollbackAndResetAutoCommit();
                    return false;
                }

                // Send transaction
                ps.setLong(1, this.transaction_id);
                ps.setLong(2, this.card.getCardId());
                ps.setLong(3, this.vendor.getVendorId());

                PreparedStatement update = conn.prepareStatement("update transaction set status = ? where transaction_id = ?");

                if (ps.executeUpdate() == 0) {
                    update.setString(1, "failed");
                } else {
                    update.setString(1, "fulfilled");
                }

                update.setLong(2, this.transaction_id);

                if (update.executeUpdate() == 0) {
                    DBManager.rollbackAndResetAutoCommit();
                    return false;
                }

                if (fee) {
                    System.out.println("FEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE!!!");
                }

                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
                DBManager.rollbackAndResetAutoCommit();
                return false;
            }
            return true;
        }
        return false;
    }
}
