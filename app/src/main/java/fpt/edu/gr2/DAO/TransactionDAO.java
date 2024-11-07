package fpt.edu.gr2.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import fpt.edu.gr2.Entity.TransactionEntity;

@Dao
public interface TransactionDAO {

    // Insert a new transaction
    @Insert
    void insertTransaction(TransactionEntity transaction);
    // Update an existing transaction
    @Update
    void updateTransaction(TransactionEntity transaction);

    // Delete a transaction by id
    @Query("DELETE FROM transactions WHERE transactionId = :transactionId")
    void deleteTransaction(int transactionId);

    // Get all transactions ordered by date
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    List<TransactionEntity> getAllTransactions();

    // Get a transaction by id
    @Query("SELECT * FROM transactions WHERE transactionId = :transactionId")
    TransactionEntity getTransactionById(int transactionId);

    // Get total expense
    @Query("SELECT SUM(amount) FROM transactions WHERE isExpense = 1 AND userid= :userid")
    Double getTotalExpense(int userid);

    // Get total income
    @Query("SELECT SUM(amount) FROM transactions WHERE isExpense = 0 AND userid= :userid" )
    Double getTotalIncome(int userid);

    // Tim kiem san pham co ten giong voi truy van (searchQuery)
    @Query("SELECT * FROM transactions WHERE note LIKE '%' || :searchQuery || '%'")
    List<TransactionEntity> searchTransaction(String searchQuery);

    // New method to select the 5 most recent transactions
    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 5")
    List<TransactionEntity> getRecentTransactions();

    // Lay tat ca san pham, sap xep theo gia tang dan
    @Query("SELECT * FROM transactions ORDER BY date ASC")
    List<TransactionEntity> getTransactionsSortedByDate();

}
