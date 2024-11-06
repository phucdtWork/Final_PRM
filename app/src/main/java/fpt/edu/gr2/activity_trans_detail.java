package fpt.edu.gr2;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.Entity.TransactionEntity;

public class activity_trans_detail extends AppCompatActivity {

    private TransactionDAO transactionDAO;
    private TextView transactionDate, transactionAmount, transactionNote, transactionAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_detail);

        // Ánh xạ các TextView với các view trong layout
        transactionDate = findViewById(R.id.transactionDate);
        transactionAmount = findViewById(R.id.transactionAmount);
        transactionNote = findViewById(R.id.transactionNote);
        transactionAddress = findViewById(R.id.transactionAddress);

        // Lấy transactionId từ Intent
        int transactionId = getIntent().getIntExtra("transactionId", -1);

        // Kiểm tra nếu transactionId hợp lệ
        if (transactionId == -1) {
            Toast.makeText(this, "Invalid transaction ID", Toast.LENGTH_SHORT).show();
            finish(); // Đóng activity nếu không có transactionId hợp lệ
            return;
        }

        // Khởi tạo Room Database và DAO
        transactionDAO = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "your_database_name")
                .allowMainThreadQueries() // Chỉ sử dụng cho demo, không nên dùng trên production
                .build()
                .TransactionDAO();

        // Lấy thông tin chi tiết giao dịch từ transactionId
        TransactionEntity transaction = transactionDAO.getTransactionById(transactionId);

        if (transaction != null) {
            // Hiển thị chi tiết giao dịch
            transactionDate.setText(transaction.getDate());
            transactionAmount.setText(String.valueOf(transaction.getAmount()));
            transactionNote.setText(transaction.getNote());
            transactionAddress.setText(transaction.getLocation());
        } else {
            Toast.makeText(this, "Transaction not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
