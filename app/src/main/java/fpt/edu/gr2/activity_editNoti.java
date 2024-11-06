package fpt.edu.gr2;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;  // Import cần thiết cho OnClickListener
import android.widget.Switch;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_editNoti extends AppCompatActivity {

    private Switch switchNotifications;
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_noti);

        // Thiết lập switch_notifications
        switchNotifications = findViewById(R.id.switch_notifications);

        // Kiểm tra trạng thái hiện tại của thông báo và cập nhật công tắc
        switchNotifications.setChecked(isNotificationEnabled());

        // Lắng nghe thay đổi trên switch_notifications
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Bật thông báo
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
                    } else {
                        enableNotifications();
                    }
                } else {
                    enableNotifications();
                }
            } else {
                // Tắt thông báo
                disableNotifications();
            }
        });

        // Thiết lập insets cho switch_notifications
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.switch_notifications), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Thêm Click Listener cho nút back
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private boolean isNotificationEnabled() {
        // Đọc trạng thái từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false); // Mặc định là tắt (false)
    }

    private void enableNotifications() {
        // Bật thông báo và lưu trạng thái vào SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, true);
        editor.apply();
    }

    private void disableNotifications() {
        // Tắt thông báo và lưu trạng thái vào SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_NOTIFICATIONS_ENABLED, false);
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            enableNotifications();
        } else {
            switchNotifications.setChecked(false); // Nếu không được cấp quyền, đặt switch về trạng thái tắt
        }
    }
}
