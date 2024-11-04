package fpt.edu.gr2.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "statistics")
public class StatisticEntity {

    @PrimaryKey(autoGenerate = true)
    private int statisticId;

    private int userId; // Foreign Key from User
    private String period; // weekly, monthly, yearly
    private double totalIncome;
    private double totalExpense;

    // Constructors, Getters, and Setters
    public StatisticEntity(int userId, String period, double totalIncome, double totalExpense) {
        this.userId = userId;
        this.period = period;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public int getStatisticId() { return statisticId; }
    public void setStatisticId(int statisticId) { this.statisticId = statisticId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public double getTotalIncome() { return totalIncome; }
    public void setTotalIncome(double totalIncome) { this.totalIncome = totalIncome; }

    public double getTotalExpense() { return totalExpense; }
    public void setTotalExpense(double totalExpense) { this.totalExpense = totalExpense; }
}
