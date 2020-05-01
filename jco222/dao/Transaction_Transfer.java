package dao;

import oracle.jdbc.OraclePreparedStatement;
import util.DBManager;

import java.sql.*;

public class Transaction_Transfer extends Transaction {
      private Account from_account;
      private Account to_account;

      public Transaction_Transfer(double amount, Person person, Branch branch, String type, String status)
      {
            super(amount, person, branch, type, status);
      }

      /**
       * Create Transaction_Transfer object for a new deposit into a specific account
       * @param amount
       * @param account
       * @param person
       * @param branch
       * @return Transaction_Transfer object populated with transaction information or null if failed
       */
      public static Transaction_Transfer getDeposit(double amount, Account account, Person person, Branch branch)
      {
            if (account != null && account.getAccountId() != 0 &&
                    person != null && person.getPersonId() != 0 &&
                    branch != null && branch.getBranchId() != 0)
            {
                  Transaction_Transfer result = new Transaction_Transfer(amount, person, branch, "deposit", "pending");
                  result.to_account = account;
                  return result;
            }
            return null;
      }

      /**
       * Create Transaction_Transfer object for a new withdrawal from a specific account
       * @param amount
       * @param account
       * @param person
       * @param branch
       * @return Transaction_Transfer object populated with transaction information or null if failed
       */
      public static Transaction_Transfer getWithdrawal(double amount, Account account, Person person, Branch branch)
      {
            if (account != null && account.getAccountId() != 0 &&
                    person != null && person.getPersonId() != 0 &&
                    branch != null && branch.getBranchId() != 0)
            {
                  Transaction_Transfer result = new Transaction_Transfer(amount, person, branch, "deposit", "pending");
                  result.from_account = account;
                  return result;
            }
            return null;
      }

      /**
       * Save a new transfer transaction to the database, handling fees and failing if the transaction would overdraw
       * the account
       * @return boolean whether the transaction has been successfully saved
       */
      public boolean save()
      {
            Connection conn = DBManager.getConnection();

            String query = "insert into transaction_transfer" +
                    "(transaction_id, from_account_id, to_account_id)" +
                    "values (?, ?, ?)";
            try (
                    PreparedStatement ps = conn.prepareStatement(query);
            ) {
                  conn.setAutoCommit(false);

                  boolean fee = false;

                  // Block transactions which will set the sending account to less than $0
                  if (this.from_account != null) {
                        if (this.from_account.getBalance() < this.amount) {
                              DBManager.rollbackAndResetAutoCommit();
                              return false;
                        }

                        if (from_account.getBalance() - amount < from_account.getMinBalance())
                        {
                              fee = true;
                        }
                  }

                  if (!super.save()) {
                        DBManager.rollbackAndResetAutoCommit();
                        return false;
                  }

                  ps.setLong(1, this.transaction_id);
                  if (this.from_account != null) {
                        ps.setLong(2, this.from_account.getAccountId());
                  } else {
                        ps.setNull(2, Types.NUMERIC);
                  }

                  if (this.to_account != null) {
                        ps.setLong(3, this.to_account.getAccountId());
                  } else {
                        ps.setNull(3, Types.NUMERIC);
                  }

                  PreparedStatement update = conn.prepareStatement("update transaction set status = ? where transaction_id = ?");

                  if (ps.executeUpdate() == 0) {
                        update.setString(1, "failed");
                  } else {
                        update.setString(1, "fulfilled");
                  }

                  update.setLong(2, this.transaction_id);

                  if (update.executeUpdate() == 0)
                  {
                        DBManager.rollbackAndResetAutoCommit();
                        return false;
                  }

                  if (fee)
                  {
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

      // Getters and Setters

      public Account getFromAccount() {
            return from_account;
      }

      public void setFromAccount(Account from_account) {
            this.from_account = from_account;
      }

      public Account getToAccount() {
            return to_account;
      }

      public void setToAccount(Account to_account) {
            this.to_account = to_account;
      }
}
