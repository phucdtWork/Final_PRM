package fpt.edu.gr2.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Query;

@Entity(tableName = "transactions",
        foreignKeys = @ForeignKey(entity = UserEntity.class,
                parentColumns = "user_id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "userId")}) // Create an index on userId
public class TransactionEntity {

    @PrimaryKey(autoGenerate = true)
    private int transactionId;

    private int userId; // Foreign Key from User
    private String date;
    private int categoryId; // Foreign Key from Category
    private double amount;
    private String note;
    private String location;
    private boolean isExpense;

    // Constructors, Getters, and Setters
    public TransactionEntity(int userId, String date, int categoryId, double amount, String note, String location, boolean isExpense) {
        this.userId = userId;
        this.date = date;
        this.categoryId = categoryId;
        this.amount = amount;
        this.note = note;
        this.location = location;
        this.isExpense = isExpense;
    }

    public TransactionEntity() {

    }

    public TransactionEntity(String date, double amount, String note ,String location) {
        this.date = date;
        this.amount = amount;
        this.note = note;
        this.location = location ;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public boolean isExpense() { return isExpense; }
    public void setExpense(boolean isExpense) { this.isExpense = isExpense; }
}
