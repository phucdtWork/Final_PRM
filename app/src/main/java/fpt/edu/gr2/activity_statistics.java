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

        // Lấy BottomNavigationView từ layout
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_statistics);
        // Set sự kiện chọn item cho BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_statistics) {
                    // Chuyển về màn hình Home (nếu cần thiết)
                    return true;
                } else if (id == R.id.navigation_home) {
                    // Chuyển sang màn hình Add Transaction
                    Intent homeIntent = new Intent(activity_statistics.this, activity_home.class);
                    startActivity(homeIntent);
                    return true;
                } else if (id == R.id.navigation_add_transaction) {
                    // Chuyển sang màn hình Statistics (tùy chỉnh activity khác nếu có)
                    Intent statisticsIntent = new Intent(activity_statistics.this, activity_addTransaction.class);
                    startActivity(statisticsIntent);
                    return true;
                } else if (id == R.id.navigation_settings) {
                    // Chuyển sang màn hình Settings (tùy chỉnh activity khác nếu có)
                    Intent settingsIntent = new Intent(activity_statistics.this, activity_settings.class);
                    startActivity(settingsIntent);
                    return true;
                }

                return false;
            }
        });
    }

    private void setData() {
        transactionDAO = AppDatabase.getDatabase(this).TransactionDAO();
        Double expense = transactionDAO.getTotalExpense(userId);
        Double income = transactionDAO.getTotalIncome(userId);

        if ((expense == null || expense == 0) && (income == null || income == 0)) {
            // No data available for both expense and income
            tvExpense.setText("0%");
            tvIncome.setText("0%");
            tvExpenseTotal.setText("0");
            tvIncomeTotal.setText("0");
            tvTotal.setText("0");

            pieChart.clearChart(); // Clear the chart if there's no data
            return;
        }

        // Handle missing or zero expense or income
        if (expense == null || expense == 0) {
            expense = 0.0;
            tvExpense.setText("0%");
            tvExpenseTotal.setText("0");
        }
        if (income == null || income == 0) {
            income = 0.0;
            tvIncome.setText("0%");
            tvIncomeTotal.setText("0");
        }

        Double total = expense + income;
        Double debt = income - expense;

        // Calculate percentages and handle cases where only one has a value
        int expensePercentage = total > 0 ? (int) Math.round((expense / total) * 100) : 0;
        int incomePercentage = total > 0 ? (int) Math.round((income / total) * 100) : 0;

        // Set calculated values to TextViews
        tvExpense.setText(expensePercentage + "%");
        tvIncome.setText(incomePercentage + "%");
        tvExpenseTotal.setText(String.valueOf(expense));
        tvIncomeTotal.setText(String.valueOf(income));
        tvTotal.setText(String.valueOf(debt));
        // Set data for the pie chart
        pieChart.clearChart();
        if (expense > 0) {
            pieChart.addPieSlice(new PieModel("Expense", expensePercentage, Color.parseColor("#5cc2f2")));
        }
        if (income > 0) {
            pieChart.addPieSlice(new PieModel("Income", incomePercentage, Color.parseColor("#66BB6A")));
        }

        // Animate the pie chart if there's data
        if (total > 0) {
            pieChart.startAnimation();
        }
    }

}