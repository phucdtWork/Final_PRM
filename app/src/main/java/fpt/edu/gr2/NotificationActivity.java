package fpt.edu.gr2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import fpt.edu.gr2.DAO.NotificationDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.NotificationEntity;

public class NotificationActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private NotificationDAO notificationDAO;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationEntity> notificationList;
    private LinearLayout btnHomeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.notificationsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int userId = getCurrentUserId(); // Lấy userId từ phiên đăng nhập
        notificationList = loadNotificationsByUserId(userId);

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        btnHomeContainer = findViewById(R.id.btn_home_container);
        btnHomeContainer.setOnClickListener(view -> {
            Intent intent = new Intent(NotificationActivity.this, activity_home.class);
            startActivity(intent);
            finish();
        });
    }

    private int getCurrentUserId() {
        // Lấy userId của người dùng đang đăng nhập từ SharedPreferences hoặc session
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getInt("current_user_id", -1);
    }

    private List<NotificationEntity> loadNotificationsByUserId(int userId) {
        appDatabase = AppDatabase.getDatabase(this);
        notificationDAO = appDatabase.NotificationDAO();
        return notificationDAO.getNotificationsByUserId(userId);
    }
}
