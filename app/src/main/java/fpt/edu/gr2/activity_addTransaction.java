package fpt.edu.gr2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem; // Thêm import cho MenuItem
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
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

import java.util.ArrayList;
import java.util.List;

import fpt.edu.gr2.DAO.NotificationDAO;
import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.DAO.UserDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.TransactionEntity;
import fpt.edu.gr2.Entity.UserEntity;

public class activity_addTransaction extends AppCompatActivity {
    private NotificationHelper notificationHelper;
    private EditText addDate, addNote, addAmount, addAddress;
    private Spinner transactionTypeSpinner;
    private GridLayout gridCategories;
    private List<View> incomeButtons;
    private List<View> expenseButtons;
    private Button selectedButton = null;
    private Button btnSave;

    private AppDatabase appDatabase;
    private TransactionDAO transactionDAO;
    private UserDAO userDAO;

    private NotificationDAO notificationDAO;
    private boolean isExpense = true;
    private int categoryId;
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_transaction);
        initializeDatabase();
        hideSoftKeyBoard();
        handleSpinner();
        initViews();
        saveButton();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }
        notificationHelper = new NotificationHelper(this);
        // Thiết lập BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_add_transaction);
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
    }

    private void handleSpinner() {
        transactionTypeSpinner = findViewById(R.id.spinner_transaction_type);
        initializeCategoryButtons();
        transactionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isExpense = parent.getItemAtPosition(position).toString().equals("Expense");
                updateCategoryButtons(isExpense);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không chọn gì cả
            }
        });
    }

    private void initializeCategoryButtons() {
        incomeButtons = new ArrayList<>();
        expenseButtons = new ArrayList<>();

        // Các nút thuộc Income
        incomeButtons.add(findViewById(R.id.btn_salary));
        incomeButtons.add(findViewById(R.id.btn_pocket));
        incomeButtons.add(findViewById(R.id.btn_bonus));
        incomeButtons.add(findViewById(R.id.btn_investment));
        incomeButtons.add(findViewById(R.id.btn_extra));

        // Các nút thuộc Expense
        expenseButtons.add(findViewById(R.id.btn_food));
        expenseButtons.add(findViewById(R.id.btn_houseware));
        expenseButtons.add(findViewById(R.id.btn_clothes));
        expenseButtons.add(findViewById(R.id.btn_cosmetic));
        expenseButtons.add(findViewById(R.id.btn_exchange));
        expenseButtons.add(findViewById(R.id.btn_medical));
        expenseButtons.add(findViewById(R.id.btn_education));
        expenseButtons.add(findViewById(R.id.btn_electric));
        expenseButtons.add(findViewById(R.id.btn_transportation));
        expenseButtons.add(findViewById(R.id.btn_housing));
        expenseButtons.add(findViewById(R.id.btn_contact_fee));
        expenseButtons.add(findViewById(R.id.btn_entertainment));
    }

    private void updateCategoryButtons(boolean transactionType) {
        // Ẩn tất cả các nút trước
        for (View button : incomeButtons) {
            button.setVisibility(View.GONE);
        }
        for (View button : expenseButtons) {
            button.setVisibility(View.GONE);
        }

        // Hiển thị nút tùy theo loại giao dịch
        if (transactionType == false) {
            for (View button : incomeButtons) {
                button.setVisibility(View.VISIBLE);
            }
        } else if (transactionType) {
            for (View button : expenseButtons) {
                button.setVisibility(View.VISIBLE);
            }
        }
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

    @SuppressLint("ResourceType")
    private void initViews() {
        addDate = findViewById(R.id.et_date);
        addNote = findViewById(R.id.et_note);
        addAmount = findViewById(R.id.et_amount);
        addAddress = findViewById(R.id.et_address);
        gridCategories = findViewById(R.id.grid_categories);
        for (int i = 0; i < gridCategories.getChildCount(); i++) {
            View view = gridCategories.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                button.setOnClickListener(v -> {
                    // Đặt lại màu cho button trước đó (nếu có)
                    if (selectedButton != null) {
                        selectedButton.setSelected(false);
                        selectedButton.setBackgroundResource(R.drawable.category_button_bg);
                    }
                    // Cập nhật button đang được chọn
                    selectedButton = button;
                    Log.d("CategorySelection", "Selected button: " + selectedButton.getText());
                    // Đặt màu cho button đang chọn
                    selectedButton.setSelected(true);
                    selectedButton.setBackgroundResource(R.drawable.category_button_selected_bg);
                    categoryId = getCategoryId(selectedButton.getText().toString());
                    Log.d("CategorySelection", "Updated categoryId: " + categoryId);
                });
            }
        }
    }

    private void saveButton() {
        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(view -> {
            Log.d("SaveButton", "Selected button ID: " + (selectedButton != null ? getCategoryId(String.valueOf(selectedButton.getText())) : "null"));
            // Check if a category button has been selected
            if (selectedButton != null) {
                // Proceed to add transaction if validation passes
                addTransaction();
            } else {
                // If no category is selected, show an appropriate message
                Toast.makeText(activity_addTransaction.this, "Please select a category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getCategoryId(String id) {
        switch (id) {
            case "Food":
                categoryId = 1;
                break;
            case "Houseware":
                categoryId = 2;
                break;
            case "Clothes":
                categoryId = 3;
                break;
            case "Cosmetic":
                categoryId = 4;
                break;
            case "Exchange":
                categoryId = 5;
                break;
            case "Medical":
                categoryId = 6;
                break;
            case "Education":
                categoryId = 7;
                break;
            case "Electric Bill":
                categoryId = 8;
                break;
            case "Transportation":
                categoryId = 9;
                break;
            case "Housing":
                categoryId = 10;
                break;
            case "Contact Fee":
                categoryId = 11;
                break;
            case "Entertainment":
                categoryId = 12;
                break;
            case "Salary":
                categoryId = 13;
                break;
            case "Pocket":
                categoryId = 14;
                break;
            case "Bonus":
                categoryId = 15;
                break;
            case "Investment":
                categoryId = 16;
                break;
            case "Extra":
                categoryId = 17;
                break;
            default:
                categoryId = -1;
                break;
        }
        return categoryId;
    }

    private void addTransaction() {

        String date = addDate.getText().toString().trim();
        String note = addNote.getText().toString().trim();
        Double amount = 0.0;
        String address = addAddress.getText().toString().trim();
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
                // Kiểm tra trạng thái thông báo từ SharedPreferences
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean notificationsEnabled = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false);

                if (notificationsEnabled) {
                    notificationHelper.showTransactionNotification(userId, "Transactions", "Transaction added successfully");
                } else {
                    notificationHelper.saveNotification(userId,"Transactions","Transaction added successfully");
                    Toast.makeText(this, "Notifications are disabled", Toast.LENGTH_SHORT).show();
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
            }
        }
    }

    private boolean checkValidation() {
        String date = addDate.getText().toString().trim();
        String note = addNote.getText().toString().trim();
        String amountText = addAmount.getText().toString().trim();
        String address = addAddress.getText().toString().trim();

        Log.d("checkValidation", "Date: " + date + ", Note: " + note + ", Amount: " + amountText + ", Address: " + address);
        Log.d("checkValidation", "CategoryId: " + categoryId);

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(amountText)) {
            Toast.makeText(activity_addTransaction.this, "Please enter Date and Expense", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categoryId == -1) {
            Toast.makeText(activity_addTransaction.this, "Category has not been selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
