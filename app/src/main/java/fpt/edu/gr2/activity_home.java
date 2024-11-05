package fpt.edu.gr2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.Database.AppDatabase;

public class activity_home extends AppCompatActivity {


    private static final String PREF_NAME = "user_session";
    private static final String USER_ID_KEY = "user_id";
    private int userId;
    private static final int ADD_TRANSACTION_REQUEST_CODE = 1;
    private Double totalBalance = 0.0;
    private TextView totalExpense;
    private TextView totalIncome;
    private ActivityResultLauncher<Intent> addTransactionLauncher;
    private AppDatabase appDatabase; // Khai báo AppDatabase
    private TransactionDAO transactionDAO; // Khai báo TransactionDAO
    private Button btnGoogleMaps;
    private ImageButton btn_notification ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        // lay bien uerid
        userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("current_user_id", 0);
        // Khởi tạo database và transactionDAO
        appDatabase = AppDatabase.getDatabase(this);
        transactionDAO = appDatabase.TransactionDAO();

        totalExpense = findViewById(R.id.tv_total_expense);
        totalIncome = findViewById(R.id.tv_total_income);
        btnGoogleMaps = findViewById(R.id.btn_google_map);
        btn_notification= findViewById(R.id.btn_notification);

        btn_notification.setOnClickListener(v->Notification());
        // chuyển sang trang google maps
        btnGoogleMaps.setOnClickListener(v -> {
            Intent intent = new Intent(activity_home.this, activity_maps.class);
            startActivity(intent);
            finish();
        });

        // Lấy tổng số expense and incometừ database
        loadTotalExpense();
        loadTotalIncome();

        addTransactionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Lấy dữ liệu từ activity_addTransaction
                        Intent data = result.getData();
                        if (data != null) {
                            double amount = data.getDoubleExtra("amount", 0.0);
                            totalBalance += amount;  // Cập nhật số dư
                            totalExpense.setText("Total Expenses: $" + totalBalance);  // Hiển thị số dư
                        }
                    }
                }
        );



//        // Lấy button từ layout
//        Button btnAddTransaction = findViewById(R.id.btn_add_transaction);
//
//        // Thêm listener cho button
//        btnAddTransaction.setOnClickListener(v -> {
//            Intent intent = new Intent(activity_home.this, activity_addTransaction.class);
//            addTransactionLauncher.launch(intent);
//        });/

        // Lấy BottomNavigationView từ layout
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set sự kiện chọn item cho BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    // Chuyển về màn hình Home (nếu cần thiết)
                    return true;
                } else if (id == R.id.navigation_add_transaction) {
                    // Chuyển sang màn hình Add Transaction
                    Intent addTransactionIntent = new Intent(activity_home.this, activity_addTransaction.class);
                    addTransactionLauncher.launch(addTransactionIntent);
                    return true;
                } else if (id == R.id.navigation_statistics) {
                    // Chuyển sang màn hình Statistics (tùy chỉnh activity khác nếu có)
                    Intent statisticsIntent = new Intent(activity_home.this, activity_statistics.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (id == R.id.navigation_settings) {
                    // Chuyển sang màn hình Settings (tùy chỉnh activity khác nếu có)
                    Intent settingsIntent = new Intent(activity_home.this, activity_settings.class);
                    startActivity(settingsIntent);
                    return true;
                }

                return false;
            }
        });
    }

    private void Notification() {
        Intent intent= new Intent(activity_home.this,NotificationActivity.class);
        startActivity(intent);
        finish();
    }

    // Phương thức để lấy tổng số expense từ cơ sở dữ liệu và cập nhật giao diện
    private void loadTotalExpense() {
        totalBalance = transactionDAO.getTotalExpense(userId);
        if (totalBalance != null) {
            totalExpense.setText("$" + totalBalance);
        } else {
            totalExpense.setText("");
        }
    }
    // Phương thức để lấy tổng số expense từ cơ sở dữ liệu và cập nhật giao diện

    private void loadTotalIncome() {
        totalBalance = transactionDAO.getTotalIncome(userId);
        if (totalBalance != null) {
            totalIncome.setText("$" + totalBalance);
        } else {
            totalIncome.setText("");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại tổng số expense khi Activity trở lại foreground
        loadTotalExpense();
        loadTotalIncome();
    }
}