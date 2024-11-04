package fpt.edu.gr2;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem; // Import MenuItem cho BottomNavigationView

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kích hoạt Edge-to-Edge
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);


        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Đặt mục Settings là mục đã chọn
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    // Chuyển về màn hình Home
                    Intent homeIntent = new Intent(activity_settings.this, activity_home.class);
                    startActivity(homeIntent);
                    return true;
                } else if (id == R.id.navigation_add_transaction) {
                    // Chuyển sang màn hình Add Transaction
                    Intent addTransactionIntent = new Intent(activity_settings.this, activity_addTransaction.class);
                    startActivity(addTransactionIntent);
                    return true;
                } else if (id == R.id.navigation_statistics) {
                    // Chuyển sang màn hình Statistics
                    Intent statisticsIntent = new Intent(activity_settings.this, activity_statistics.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (id == R.id.navigation_settings) {
                    // Đã ở màn hình Settings, không cần chuyển
                    return true;
                }

                return false;
            }
        });

        // Xử lý cho các mục trong màn hình cài đặt
        findViewById(R.id.option_edit_profile).setOnClickListener(v -> {
            Intent intent = new Intent(activity_settings.this, activity_editProfile.class);
            startActivity(intent);
        });

        findViewById(R.id.option_notifications).setOnClickListener(v -> {
            Intent intent = new Intent(activity_settings.this, activity_editNoti.class);
            startActivity(intent);
        });

        findViewById(R.id.option_language).setOnClickListener(v -> {
            Intent intent = new Intent(activity_settings.this, activity_language.class);
            startActivity(intent);
        });

        findViewById(R.id.option_help).setOnClickListener(v -> {
            Intent intent = new Intent(activity_settings.this, activity_help.class);
            startActivity(intent);
        });

        findViewById(R.id.option_logout).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Xóa thông tin đăng nhập từ SharedPreferences
                        getSharedPreferences("user_prefs", MODE_PRIVATE)
                                .edit()
                                .remove("current_user_id") // Xóa ID người dùng đã lưu
                                .apply();

                        Intent intent = new Intent(activity_settings.this, activity_login.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
