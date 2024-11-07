package fpt.edu.gr2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.TransactionEntity;

import java.util.ArrayList;
import java.util.List;

public class activity_transaction_list extends AppCompatActivity {
    private Button btnAddTrans;
    private AppDatabase appDatabase;
    private TransactionDAO transactionDAO;
    private RecyclerView recyclerView;
    private ListViewAdapter listViewAdapter;
    private List<TransactionEntity> transactionList = new ArrayList<>();
    private SearchView searchView;
    private ImageButton btnFilter;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaction_list);

        // Initialize database and DAO
        appDatabase = AppDatabase.getDatabase(this);
        transactionDAO = appDatabase.TransactionDAO();

        // Initialize UI components
        btnAddTrans = findViewById(R.id.btn_addTrans);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        btnFilter = findViewById(R.id.ed_filter);
        btn_back = findViewById(R.id.btn_back);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listViewAdapter = new ListViewAdapter(this, transactionList, activity_transaction_list.this);
        recyclerView.setAdapter(listViewAdapter);

        // Load initial transaction list
        loadTransactions();

        // Set up listeners
        btnAddTrans.setOnClickListener(v -> openAddTransaction());
        btnFilter.setOnClickListener(v -> filterTransactions());
        searchTransactions();

        // Thêm sự kiện onClick cho btn_back
        btn_back.setOnClickListener(v -> onBack());
    }

    private void onBack() {
        Intent intent = new Intent(activity_transaction_list.this, activity_home.class);
        startActivity(intent);
        finish();
    }

    // Open Add Transaction screen
    private void openAddTransaction() {
        Intent intent = new Intent(activity_transaction_list.this, activity_addTransaction.class);
        startActivityForResult(intent, 1);
    }

    // Filter transactions by date in descending order
    private void filterTransactions() {
        List<TransactionEntity> filteredList = transactionDAO.getTransactionsSortedByDate();
        if (filteredList != null && !filteredList.isEmpty()) {
            transactionList.clear();
            transactionList.addAll(filteredList);
            listViewAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Transactions sorted by date", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No transactions available to sort", Toast.LENGTH_SHORT).show();
        }
    }

    // Search transactions by name
    private void searchTransactions() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAndUpdateList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchAndUpdateList(query);
                return false;
            }
        });
    }

    private void searchAndUpdateList(String query) {
        if (query.isEmpty()) {
            loadTransactions(); // Load all transactions if search query is empty
        } else {
            List<TransactionEntity> searchResults = transactionDAO.searchTransaction(query);
            if (searchResults != null && !searchResults.isEmpty()) {
                transactionList.clear();
                transactionList.addAll(searchResults);
                listViewAdapter.notifyDataSetChanged();
                Toast.makeText(this, searchResults.size() + " transactions found", Toast.LENGTH_SHORT).show();
            } else {
                transactionList.clear();
                listViewAdapter.notifyDataSetChanged();
                Toast.makeText(this, "No transactions found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Load all transactions from the database, sorted by date in descending order
    private void loadTransactions() {
        transactionList.clear();
        List<TransactionEntity> transactionsFromDb = transactionDAO.getTransactionsSortedByDate();
        if (transactionsFromDb != null && !transactionsFromDb.isEmpty()) {
            transactionList.addAll(transactionsFromDb);
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No transactions available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            loadTransactions(); // Reload transactions after returning from AddTransaction
        }
    }
}
