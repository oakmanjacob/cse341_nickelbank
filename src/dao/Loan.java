package dao;

public class Loan {
    public int loan_id;
    public int person_id;
    public double amount;

    public Loan (int loan_id, int person_id, double amount)
    {
        this.loan_id = loan_id;
        this.person_id = person_id;
        this.amount = amount;
    }
}
