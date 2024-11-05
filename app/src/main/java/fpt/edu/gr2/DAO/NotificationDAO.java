package fpt.edu.gr2.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import fpt.edu.gr2.Entity.NotificationEntity;

@Dao
public interface NotificationDAO {

    // Insert a new notification
    @Insert
    void insertNotification(NotificationEntity notification);

    // Get all notifications
    @Query("SELECT * FROM notifications WHERE userId=:userId ORDER BY notificationId DESC")
    List<NotificationEntity> getNotificationsByUserId(int userId);

    // Get notifications based on a specific date range
    @Query("SELECT * FROM notifications WHERE date BETWEEN :startDate AND :endDate")
    List<NotificationEntity> getNotificationsByDateRange(long startDate, long endDate);
}
