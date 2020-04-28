package dao;

import java.sql.Timestamp;

public class Card_Debit extends Card {
    private long account_id;
    private Account account;

    public Card_Debit(Card card, long account_id)
    {
        super(card.card_id, card.person_id, card.type, card.card_number, card.cvc, card.status, card.created, card.modified);
        this.account_id = account_id;
    }

    protected void setAccountId(long account_id)
    {
        this.account_id = account_id;
    }

    public Account getAccount()
    {
        if (this.account == null)
        {
            return Account.fromAccountId(this.account_id);
        }
        else
        {
            return this.account;
        }
    }

    public double getAvailableBalance()
    {
        Account account = this.getAccount();

        if (account == null)
        {
            return 0;
        }

        account.updateBalance();
        return account.getBalance();
    }
}
