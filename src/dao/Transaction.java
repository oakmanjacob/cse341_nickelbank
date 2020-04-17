package dao;

public class Transaction {

    private long transaction_id;
    private long parent_transaction_id;
    private long person_id;
    private long branch_id;
    private double amount;
    private String type;
    private String status;

    public boolean save()
    {
        return false;
    }

    public boolean delete()
    {
        return false;
    }
}
