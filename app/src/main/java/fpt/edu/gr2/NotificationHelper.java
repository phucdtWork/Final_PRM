package fpt.edu.gr2;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fpt.edu.gr2.DAO.NotificationDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.NotificationEntity;

public class NotificationHelper {
    private AppDatabase appDatabase;  // Sửa thành final
    private final NotificationDAO notificationDao; // Sửa thành final
    private final Context context;
    private final NotificationManager notificationManager;
    private static final String TRANSACTION_CHANNEL_ID = "transaction_channel";
    private static final String ALERT_CHANNEL_ID = "alert_channel";

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Khởi tạo AppDatabase và NotificationDAO
        this.appDatabase = AppDatabase.getDatabase(context);
        this.notificationDao = appDatabase.NotificationDAO();
        createNotificationChannels(); // Tạo kênh nếu chưa tồn tại
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Kênh cho Transaction
            NotificationChannel transactionChannel = new NotificationChannel(
                    TRANSACTION_CHANNEL_ID,
                    "Transaction Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            transactionChannel.setDescription("Notifications for successful transactions");

            // Kênh cho Alert
            NotificationChannel alertChannel = new NotificationChannel(
                    ALERT_CHANNEL_ID,
                    "Alert Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            alertChannel.setDescription("Important alerts");

            // Đăng ký các kênh với NotificationManager
            notificationManager.createNotificationChannel(transactionChannel);
            notificationManager.createNotificationChannel(alertChannel);
        }
    }

    // Hiển thị và lưu thông báo Transaction
    public void showTransactionNotification(int userId, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TRANSACTION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());

        // Lưu thông báo vào database
        saveNotification(userId, title, content);
    }

    // Hiển thị và lưu thông báo Alert
    public void showAlertNotification(int userId, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ALERT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());

        // Lưu thông báo vào database
        saveNotification(userId, title, content);
    }

    // Phương thức lưu thông báo vào database
    public void saveNotification(int userId, String title, String content) {
        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        NotificationEntity notification = new NotificationEntity(userId, title, content, date);
        new Thread(() -> notificationDao.insertNotification(notification)).start(); // Thực hiện trong một luồng phụ
    }
}
