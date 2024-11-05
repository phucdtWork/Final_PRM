package fpt.edu.gr2;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem; // Thêm import cho MenuItem
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fpt.edu.gr2.DAO.NotificationDAO;
import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.DAO.UserDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.TransactionEntity;
import fpt.edu.gr2.Entity.UserEntity;

public class activity_addTransaction extends AppCompatActivity {
    private NotificationHelper notificationHelper;
    private EditText addDate, addNote, addAmount, addAddress;
    private Spinner transactiontypeSelect;
    private Button btnSave;
    private AppDatabase appDatabase;
    private TransactionDAO transactionDAO;
    private UserDAO userDAO;
    private NotificationDAO notificationDAO;
    private boolean isExpense = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);
        initViews();
        initializeDatabase();
        hideSoftKeyBoard();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }
        notificationHelper = new NotificationHelper(this);
        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Đặt mục Add Transaction là mục đã chọn
        bottomNavigationView.setSelectedItemId(R.id.navigation_add_transaction);
        // Set sự kiện chọn item cho BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    Intent addTransactionIntent = new Intent(activity_addTransaction.this, activity_home.class);
                    startActivity(addTransactionIntent);
                    // Chuyển về màn hình Home (nếu cần thiết)
                    return true;
                } else if (id == R.id.navigation_add_transaction) {
                    // Chuyển sang màn hình Add Transaction
                    return true;
                } else if (id == R.id.navigation_statistics) {
                    // Chuyển sang màn hình Statistics (tùy chỉnh activity khác nếu có)
                    Intent statisticsIntent = new Intent(activity_addTransaction.this, activity_statistics.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (id == R.id.navigation_settings) {
                    // Chuyển sang màn hình Settings (tùy chỉnh activity khác nếu có)
                    Intent settingsIntent = new Intent(activity_addTransaction.this, activity_settings.class);
                    startActivity(settingsIntent);
                    return true;
                }

                return false;
            }
        });


        transactiontypeSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                isExpense = parent.getItemAtPosition(position).toString().equals("Expense");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void hideSoftKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase() {
        appDatabase = AppDatabase.getDatabase(this);
        transactionDAO = appDatabase.TransactionDAO();
        userDAO = appDatabase.UserDAO();
        notificationDAO = appDatabase.NotificationDAO();
    }

    private void initViews() {
        addDate = findViewById(R.id.et_date);
        addNote = findViewById(R.id.et_note);
        addAmount = findViewById(R.id.et_amount);
        addAddress = findViewById(R.id.et_address);
        btnSave = findViewById(R.id.btn_save);
        transactiontypeSelect = findViewById(R.id.spinner_transaction_type);

        btnSave.setOnClickListener(v -> addTransaction());
    }

    private void addTransaction() {

        String date = addDate.getText().toString().trim();
        String note = addNote.getText().toString().trim();
        Double amount = 0.0;
        String address = addAddress.getText().toString().trim();
        int categoryId = 1;
        try {
            amount = Double.parseDouble(addAmount.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }
//         lấy duex liệu từ bản user
        UserEntity user = userDAO.getUserById(1);
        if (user == null) {
            Toast.makeText(activity_addTransaction.this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("current_user_id", 0);

        if (checkValidation()) {
            TransactionEntity transaction = new TransactionEntity(userId, date, categoryId, amount, note, address, isExpense);
            try {

                transactionDAO.insertTransaction(transaction);
                Toast.makeText(activity_addTransaction.this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    notificationHelper.showTransactionNotification(userId,"Transactions","Transaction added successfully");
                } else {
                    Toast.makeText(this, "Notification permission not granted", Toast.LENGTH_SHORT).show();
                }
                Intent intent1 = new Intent(activity_addTransaction.this, activity_home.class);
                Intent intent = new Intent();
                intent.putExtra("amount", amount);
                setResult(RESULT_OK, intent);
                startActivity(intent1);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity_addTransaction.this, "Error during adding transaction", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_addTransaction.this, activity_addTransaction.class);
                startActivity(intent);
                finish();
            }
        }
    }


    private boolean checkValidation() {
        String date = addDate.getText().toString().trim();
        String note = addNote.getText().toString().trim();
        Double amount = Double.parseDouble(addAmount.getText().toString().trim());
        String address = addAddress.getText().toString().trim();
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(amount.toString())) {
            Toast.makeText(activity_addTransaction.this, "Please enter Date and Expense", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
