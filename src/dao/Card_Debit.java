package dao;

public class Card_Debit extends Card {
    private long account_id;
    private Account account;

    public Card_Debit(Card card, long account_id)
    {
        super(card.card_id, card.person_id, card.type, card.card_number, card.cvc, card.status, card.created, card.modified);
        this.account_id = account_id;
    }

    /**
     * Get the account associated with this debit card
     * @return the account associated with this card or null if failed
     */
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

    /**
     * Get the current balance available to the credit card
     * @return the available balance or 0 if failed
     */
    public double getAvailableBalance()
    {
        Account account = this.getAccount();

        if (account == null)
        {
            return 0;
        }

        return account.getBalance();
    }
}
