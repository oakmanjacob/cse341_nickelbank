package dao;

public class Card {
    public int card_id;
    public int person_id;
    public String type;
    public double balance;
    public int account_id;
    public double credit_limit;

    public Card(int card_id, int person_id, String type)
    {
        this.card_id = card_id;
        this.person_id = person_id;
        this.type = type;
        this.balance = 0;
    }
}
