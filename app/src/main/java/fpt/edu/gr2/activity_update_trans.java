package fpt.edu.gr2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.DAO.UserDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.TransactionEntity;

public class activity_update_trans extends AppCompatActivity {
    private EditText updateDate, updateNote, updateAmount, updateAddress;
    private Spinner transactionTypeSpinner;
    private GridLayout gridCategories;
    private List<View> incomeButtons;
    private List<View> expenseButtons;
    private Button selectedButton = null;
    private Button btnUpdate;
    private TextView tv_back;

    private AppDatabase appDatabase;
    private TransactionDAO transactionDAO;
    private boolean isExpense;
    private int categoryId, transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_trans);
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(v -> onBack());

        initializeDatabase();
        hideSoftKeyBoard();
        handleSpinner();
        initViews();
        loadTransactionData();
        updateButton();

    }

    private void onBack() {
        Intent intent = new Intent(activity_update_trans.this, activity_home.class);
        startActivity(intent);
        finish();
    }

    private void loadTransactionData() {
        transactionId = getIntent().getIntExtra("transaction_id", -1);
//        if (transactionId == -1) {
//            Toast.makeText(this, "Transaction not found", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(activity_update_trans.this, activity_home.class);  // Thay PreviousActivity bằng tên màn hình trước đó
//            startActivity(intent);
//            finish();
//            return;
//        }

        TransactionEntity transaction = transactionDAO.getTransactionById(transactionId);
        if (transaction != null) {
            updateDate.setText(transaction.getDate());
            updateNote.setText(transaction.getNote());
            updateAmount.setText(String.valueOf(transaction.getAmount()));
            updateAddress.setText(transaction.getLocation());
            isExpense = transaction.isExpense();
            categoryId = transaction.getCategoryId();
            updateCategoryButtons(isExpense);
        }
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initializeCategoryButtons() {
        incomeButtons = new ArrayList<>();
        expenseButtons = new ArrayList<>();

        incomeButtons.add(findViewById(R.id.btn_salary));
        incomeButtons.add(findViewById(R.id.btn_pocket));
        incomeButtons.add(findViewById(R.id.btn_bonus));
        incomeButtons.add(findViewById(R.id.btn_investment));
        incomeButtons.add(findViewById(R.id.btn_extra));

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
        for (View button : incomeButtons) button.setVisibility(View.GONE);
        for (View button : expenseButtons) button.setVisibility(View.GONE);

        if (!transactionType) {
            for (View button : incomeButtons) button.setVisibility(View.VISIBLE);
        } else {
            for (View button : expenseButtons) button.setVisibility(View.VISIBLE);
        }
    }

    private void hideSoftKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.d("TAG", "hideSoftKeyBoard: View is null");
        }
    }

    private void initializeDatabase() {
        appDatabase = AppDatabase.getDatabase(this);
        transactionDAO = appDatabase.TransactionDAO();
    }

    @SuppressLint("ResourceType")
    private void initViews() {
        updateDate = findViewById(R.id.et_date);
        updateNote = findViewById(R.id.et_note);
        updateAmount = findViewById(R.id.et_amount);
        updateAddress = findViewById(R.id.et_address);
        gridCategories = findViewById(R.id.grid_categories);
        for (int i = 0; i < gridCategories.getChildCount(); i++) {
            View view = gridCategories.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                button.setOnClickListener(v -> {
                    if (selectedButton != null) {
                        selectedButton.setSelected(false);
                        selectedButton.setBackgroundResource(R.drawable.category_button_bg);
                    }
                    selectedButton = button;
                    selectedButton.setSelected(true);
                    selectedButton.setBackgroundResource(R.drawable.category_button_selected_bg);
                    categoryId = getCategoryId(selectedButton.getText().toString());
                });
            }
        }
    }

    private void updateButton() {
        btnUpdate = findViewById(R.id.btn_save);
        btnUpdate.setOnClickListener(view -> {
            if (selectedButton != null) {
                updateTransaction();
            } else {
                Toast.makeText(activity_update_trans.this, "Please select a category", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getCategoryId(String id) {
        // Similar category mapping logic
        // ...
        return categoryId;
    }

    private void updateTransaction() {
        String date = updateDate.getText().toString().trim();
        String note = updateNote.getText().toString().trim();
        Double amount;
        String address = updateAddress.getText().toString().trim();
        try {
            amount = Double.parseDouble(updateAmount.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkValidation()) {
            TransactionEntity transaction = new TransactionEntity(transactionId, date, categoryId, amount, note, address, isExpense);
            transactionDAO.updateTransaction(transaction);
            Toast.makeText(activity_update_trans.this, "Transaction updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean checkValidation() {
        String date = updateDate.getText().toString().trim();
        String amountText = updateAmount.getText().toString().trim();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(amountText)) {
            Toast.makeText(activity_update_trans.this, "Please enter Date and Expense", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categoryId == -1) {
            Toast.makeText(activity_update_trans.this, "Category has not been selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
