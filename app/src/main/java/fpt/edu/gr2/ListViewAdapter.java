package fpt.edu.gr2;

import android.annotation.SuppressLint;
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

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.EventViewHolder> {
    Context context;
    List<TransactionEntity> eventEntities;
    Activity activity;
    private TransactionDAO TransactionDAO;

    // Constructor của adapter, nhận vào context, danh sách sản phẩm và activity
    public ListViewAdapter(Context context, List<TransactionEntity> eventEntities, Activity activity) {
        this.context = context;
        this.eventEntities = eventEntities;
        this.activity = activity;
        this.TransactionDAO = AppDatabase.getDatabase(context).TransactionDAO(); // Khởi tạo một lần
    }

    @NonNull
    @Override
    public ListViewAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Khởi tạo LayoutInflater và tạo view cho mỗi item
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.transaction_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewAdapter.EventViewHolder holder, int position) {
        if (eventEntities != null && !eventEntities.isEmpty()) {
            TransactionEntity TransactionEntity = eventEntities.get(position);
            if (TransactionEntity != null) {
                // Hiển thị tên, giá và số lượng sản phẩm
                holder.tv_name.setText(TransactionEntity.getCategoryId());
                holder.tv_date.setText(String.valueOf(TransactionEntity.getDate()));
                holder.tv_location.setText(String.valueOf(TransactionEntity.getLocation()));
                //descrip

                // Xử lý sự kiện khi nhấn vào tên sản phẩm
                holder.tv_name.setOnClickListener(v -> {
                    openUpdateeventActivity(TransactionEntity.getTransactionId());
                });

                // Xử lý sự kiện khi nhấn vào giá sản phẩm
                holder.tv_date.setOnClickListener(v -> {
                    openUpdateeventActivity(TransactionEntity.getTransactionId());
                });

                // Xử lý sự kiện khi nhấn nút chỉnh sửa
                holder.editbtn.setOnClickListener(v -> {
                    openUpdateeventActivity(TransactionEntity.getTransactionId());
                });

                // Xử lý sự kiện khi nhấn nút xóa
                holder.deletebtn.setOnClickListener(view -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Event")
                            .setMessage("Are you sure you want to delete this event?")
                            .setPositiveButton("OK", (dialogInterface, i) -> {
                                TransactionDAO.deleteTransaction(TransactionEntity.getTransactionId());
                                eventEntities.remove(position); // Xóa sản phẩm khỏi danh sách
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, eventEntities.size());
                                Toast.makeText(context, "Event deleted successfully", Toast.LENGTH_SHORT).show();
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
        return eventEntities != null ? eventEntities.size() : 0; // Kiểm tra null
    }

    // Class holder chứa các view của một sản phẩm
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_date, tv_location, tv_des;
        ImageButton editbtn, deletebtn;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            // Gán view cho các biến trong holder
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_location = itemView.findViewById(R.id.tv_location);
            //descrip
            editbtn = itemView.findViewById(R.id.editbtn);
            deletebtn = itemView.findViewById(R.id.deletebtn);
        }
    }

    // Mở activity Updateevent
    private void openUpdateeventActivity(int eventId) {
        Intent intent = new Intent(context, activity_edit_trans.class);
        intent.putExtra("eventId", String.valueOf(eventId));
        activity.startActivityForResult(intent, 1);
    }
}
