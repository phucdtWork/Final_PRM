package fpt.edu.gr2.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

import fpt.edu.gr2.Entity.StatisticEntity;

@Dao
public interface StatisticDAO {

    // Insert a new statistic record
    @Insert
    void insertStatistic(StatisticEntity statisticEntity);

    // Update an existing statistic record
    @Update
    void updateStatistic(StatisticEntity statisticEntity);

    // Delete a statistic record
    @Delete
    void deleteStatistic(StatisticEntity statisticEntity);

    // Query to get all statistics for a user by period (e.g., "monthly", "yearly")
    @Query("SELECT * FROM statistics WHERE userId = :userId AND period = :period")
    List<StatisticEntity> getStatisticsByUserAndPeriod(int userId, String period);

    // Query to get total income and total expense for a user in a specific period
    @Query("SELECT SUM(totalIncome) FROM statistics WHERE userId = :userId AND period = :period")
    double getTotalIncomeForUserInPeriod(int userId, String period);

    @Query("SELECT SUM(totalExpense) FROM statistics WHERE userId = :userId AND period = :period")
    double getTotalExpenseForUserInPeriod(int userId, String period);

    // Get a statistic by its ID
    @Query("SELECT * FROM statistics WHERE statisticId = :statisticId")
    StatisticEntity getStatisticById(int statisticId);

    // Get all statistics for a user
    @Query("SELECT * FROM statistics WHERE userId = :userId")
    List<StatisticEntity> getAllStatisticsForUser(int userId);
}
