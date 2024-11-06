package fpt.edu.gr2.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "notifications",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "user_id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE // Xóa tất cả thông báo khi xóa người dùng
        )
)
public class NotificationEntity {

    @PrimaryKey(autoGenerate = true)
    private int notificationId;

    private int userId; // Foreign Key từ UserEntity
    private String title; // Thêm trường title
    private String content; // Thêm trường content
    private String date;

    // Constructors, Getters, and Setters
    public NotificationEntity(int userId, String title, String content, String date) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public int getNotificationId() { return notificationId; }
    public void setNotificationId(int notificationId) { this.notificationId = notificationId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}

