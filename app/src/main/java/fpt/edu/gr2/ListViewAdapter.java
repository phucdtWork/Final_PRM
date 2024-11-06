package fpt.edu.gr2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fpt.edu.gr2.DAO.TransactionDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.TransactionEntity;

import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.TransactionViewHolder> {
    Context context;
    List<TransactionEntity> transactionEntities;
    Activity activity;
    private TransactionDAO TransactionDAO;

    // Constructor của adapter, nhận vào context, danh sách sản phẩm và activity
    public ListViewAdapter(Context context, List<TransactionEntity> transactionEntities, Activity activity) {
        this.context = context;
        this.transactionEntities = transactionEntities;
        this.activity = activity;
        this.TransactionDAO = AppDatabase.getDatabase(context).TransactionDAO(); // Khởi tạo một lần
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Khởi tạo LayoutInflater và tạo view cho mỗi item
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        if (transactionEntities != null && !transactionEntities.isEmpty()) {
            TransactionEntity TransactionEntity = transactionEntities.get(position);
            if (TransactionEntity != null) {
                // Hiển thị phan loai, ngay, gia tri, loai
                holder.tv_note.setText(TransactionEntity.getNote());
                holder.tv_date.setText(String.valueOf(TransactionEntity.getDate()));
                holder.tv_is_expense.setText(transactionEntities.get(position).isExpense() ? "Expense" : "Income");
                holder.tv_amount.setText(String.valueOf(TransactionEntity.getAmount()));

                // Xử lý sự kiện khi nhấn vào note
                holder.tv_note.setOnClickListener(v -> {
                    showTransactionDetailPopup(TransactionEntity);
                });

                // Xử lý sự kiện khi nhấn vào giá sản phẩm
                holder.tv_date.setOnClickListener(v -> {
                    showTransactionDetailPopup(TransactionEntity);
                });

                // Xử lý sự kiện khi nhấn nút chỉnh sửa
                holder.editbtn.setOnClickListener(v -> {
                    openUpdateTransactionActivity(TransactionEntity.getTransactionId());
                });

                // Xử lý sự kiện khi nhấn nút xóa
                holder.deletebtn.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete transaction")
                            .setMessage("Are you sure you want to delete this transaction?")
                            .setPositiveButton("OK", (dialogInterface, i) -> {
                                TransactionDAO.deleteTransaction(TransactionEntity.getTransactionId());
                                transactionEntities.remove(position); // Xóa sản phẩm khỏi danh sách
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, transactionEntities.size());
                                Toast.makeText(context, "transaction deleted successfully", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            })
                            .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                });
            } else {
                Log.d("TAG", "onBindViewHolder: Null TransactionEntity");
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactionEntities != null ? transactionEntities.size() : 0; // Kiểm tra null
    }

    // Class holder chứa các view của một sản phẩm
    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tv_note, tv_date, tv_is_expense, tv_amount;
        ImageButton editbtn, deletebtn;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Gán view cho các biến trong holder
            tv_note = itemView.findViewById(R.id.tv_note);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_is_expense = itemView.findViewById(R.id.tv_is_expense);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            //button
            editbtn = itemView.findViewById(R.id.editbtn);
            deletebtn = itemView.findViewById(R.id.deletebtn);
        }
    }

    // Mở activity Updatetransaction
    private void openUpdateTransactionActivity(int transactionId) {
        Intent intent = new Intent(context, activity_update_trans.class);
        intent.putExtra("transactionId", transactionId);
        context.startActivity(intent);
    }

    // Hiển thị popup chi tiết giao dịch
    private void showTransactionDetailPopup(TransactionEntity transactionEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Sử dụng custom title
        View customTitle = LayoutInflater.from(context).inflate(R.layout.dialog_custom_title, null);
        builder.setCustomTitle(customTitle);

        // Tạo layout cho phần nội dung của popup
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_transaction_detail, null);
        TextView detailNote = view.findViewById(R.id.detail_note);
        TextView detailDate = view.findViewById(R.id.detail_date);
        TextView detailAmount = view.findViewById(R.id.detail_amount);
        TextView detailIsExpense = view.findViewById(R.id.detail_is_expense);

        // Gán dữ liệu từ transactionEntity vào các TextView
        detailNote.setText(transactionEntity.getNote());
        detailDate.setText(String.valueOf(transactionEntity.getDate()));
        detailAmount.setText(String.valueOf(transactionEntity.getAmount()));
        detailIsExpense.setText(transactionEntity.isExpense() ? "Expense" : "Income");

        builder.setView(view);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }
}
