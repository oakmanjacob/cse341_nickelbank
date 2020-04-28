package dao;

import util.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Card_Credit extends Card {
    private double interest_rate;
    private double credit_limit;

    public Card_Credit(Card card, double interest_rate, double credit_limit)
    {
        super(card.card_id, card.person_id, card.type, card.card_number, card.cvc, card.status, card.created, card.modified);
        this.interest_rate = interest_rate;
        this.credit_limit = credit_limit;
    }

    public double getAvailableBalance()
    {
        if (this.card_id == 0)
        {
            return 0;
        }

        Connection conn = DBManager.getConnection();

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT cb.balance " +
                            "FROM credit_balance cb " +
                            "WHERE cb.card_id = ?");

            ps.setLong(1, this.card_id);

            ResultSet result = ps.executeQuery();

            if (result != null && result.next()) {
                return this.credit_limit - result.getDouble("balance");
            }
            else
            {
                return 0;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return 0;
        }
    }
}
