package fpt.edu.gr2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.TransactionEntity;

public class activity_statistics extends AppCompatActivity {

    private int userId;
    ListView listView;
    TransactionDAO transactionDAO;
    List<TransactionEntity> transactionList;
    ArrayAdapter<TransactionEntity> arrayAdapter;

    TextView tvExpense, tvIncome,tvExpenseTotal, tvIncomeTotal,tvTotal;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_statistics);
        userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("current_user_id", 0);
        transactionDAO = AppDatabase.getDatabase(this).TransactionDAO();

        tvExpense = findViewById(R.id.tvExpense);
        tvIncome = findViewById(R.id.tvIncome);
        pieChart = findViewById(R.id.piechart);

        tvExpenseTotal = findViewById(R.id.tv_total_expense);
        tvIncomeTotal = findViewById(R.id.tv_total_income);
        tvTotal = findViewById(R.id.tv_income_expense_total);

        setData();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        transactionList = transactionDAO.getAllTransactions();
//        if (transactionList == null) {
//            transactionList = new ArrayList<>(); // Tạo danh sách rỗng nếu cần
//        }
//        listView = findViewById(R.id.listview);
//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionList);
//        listView.setAdapter(arrayAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    Intent home = new Intent(activity_statistics.this, activity_home.class);
                    startActivity(home);
                    return true;
                } else if (id == R.id.navigation_add_transaction) {
                    Intent addTransactionIntent = new Intent(activity_statistics.this, activity_addTransaction.class);
                    startActivity(addTransactionIntent);
                    return true;
                } else if (id == R.id.navigation_statistics) {
                    return true;
                } else if (id == R.id.navigation_settings) {
                    Intent settingsIntent = new Intent(activity_statistics.this, activity_settings.class);
                    startActivity(settingsIntent);
                    return true;
                }
                return false;
            }
        });
    }

    private void setData() {
        // Set the percentage of language used
        transactionDAO = AppDatabase.getDatabase(this).TransactionDAO();
        Double expense = transactionDAO.getTotalExpense(userId);
        Double income = transactionDAO.getTotalIncome(userId);
        Double total = expense + income;
        Double debt = income-expense;

        // Chuyển đổi tỷ lệ thành số nguyên để sử dụng trong PieChart
        int expensePercentage = (int) Math.round((expense / total) * 100);
        int incomePercentage = (int) Math.round((income / total) * 100);

        // Đặt tỷ lệ phần trăm vào TextViews
        tvExpense.setText(expensePercentage + "%");
        tvIncome.setText(incomePercentage + "%");
        tvExpenseTotal.setText(String.valueOf(expense));
        tvIncomeTotal.setText(String.valueOf(income));
        tvTotal.setText(String.valueOf(debt));

        // Set the data and color to the pie chart
        pieChart.addPieSlice(new PieModel("Expense", expensePercentage, Color.parseColor("#5cc2f2")));
        pieChart.addPieSlice(new PieModel("Income", incomePercentage, Color.parseColor("#66BB6A")));
        // To animate the pie chart
        pieChart.startAnimation();
    }
}