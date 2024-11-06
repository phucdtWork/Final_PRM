package fpt.edu.gr2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

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
    private AppDatabase appData;
    private TransactionDAO eventDao;
    private RecyclerView recyclerView;
    public ListViewAdapter listViewApdapter;
    List<TransactionEntity> list = new ArrayList<>();
    private SearchView searchView;
//    private ImageButton btnFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        innitData();
        innitView();
    }

    private void innitData() {
        AppDatabase appData = AppDatabase.getDatabase(this);
        eventDao = appData.TransactionDAO();
    }

    private void innitView() {
        btnAddTrans = findViewById(R.id.btn_addTrans);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerview);
        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tạo danh sách sản phẩm mẫu
        list = new ArrayList<>();
        //phần này phần add dữ liệu cứng vô
        list.add(new TransactionEntity());
        listViewApdapter = new ListViewAdapter(this, list, activity_transaction_list.this);
        recyclerView.setAdapter(listViewApdapter);
        loadEvents();
        searchEvent();
        btnAddTrans.setOnClickListener(v -> addEvent());


    }

    // search Event theo tên
    private void searchEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.isEmpty()) {
                    loadEvents(); // Load all Events if search text is empty
                } else {
                    List<TransactionEntity> EventFromDb = AppDatabase.getDatabase(activity_transaction_list.this).TransactionDAO().searchTransaction(s);
                    list.clear();
                    list.addAll(EventFromDb);
                    System.out.println("Search results: " + EventFromDb.size()); // Debug statement
                }
                listViewApdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    loadEvents(); // Reload all Events if search text is cleared
                } else {
                    List<TransactionEntity> EventFromDb = AppDatabase.getDatabase(activity_transaction_list.this).TransactionDAO().searchTransaction(s);
                    list.clear();
                    list.addAll(EventFromDb);
                    System.out.println("Search results on change: " + EventFromDb.size()); // Debug statement
                }
                listViewApdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    // chuyển active từ main sang addEvent
    private void addEvent() {
        Intent intent = new Intent(activity_transaction_list.this, activity_addTransaction.class);
        startActivityForResult(intent, 1);
        finish();
    }

    // load sản phẩm khi thay đổi đều gì đó
    public void loadEvents() {
        list.clear();
        List<TransactionEntity> EventFromDb = AppDatabase.getDatabase(this).TransactionDAO().getAllTransactions();
        list.addAll(EventFromDb);
        listViewApdapter.notifyDataSetChanged(); // Update adapter
    }

}