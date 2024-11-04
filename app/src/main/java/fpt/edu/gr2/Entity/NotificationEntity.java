package fpt.edu.gr2.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class NotificationEntity {

    @PrimaryKey(autoGenerate = true)
    private int notificationId;

    private int userId; // Foreign Key from User
    private String message;
    private String date;
    private boolean isRead;

    // Constructors, Getters, and Setters
    public NotificationEntity(int userId, String message, String date, boolean isRead) {
        this.userId = userId;
        this.message = message;
        this.date = date;
        this.isRead = isRead;
    }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}

