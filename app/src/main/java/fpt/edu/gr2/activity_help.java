package fpt.edu.gr2;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_help extends AppCompatActivity {

    private EditText etMessage;
    private ImageButton btnSend;
    private LinearLayout chatContainer;
    private ScrollView scrollViewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Liên kết các view với XML
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        chatContainer = findViewById(R.id.chatContainer);
        scrollViewChat = findViewById(R.id.scrollViewChat);

        // Xử lý nút Back
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay về màn hình trước đó
            }
        });

        // Xử lý sự kiện gửi tin nhắn
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    addMessageToChat(message, true); // Thêm tin nhắn từ người dùng
                    etMessage.setText(""); // Xóa text sau khi gửi
                    simulateSystemResponse(message); // Giả lập trả lời từ hệ thống
                } else {
                    Toast.makeText(activity_help.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm thêm tin nhắn vào giao diện chat
    private void addMessageToChat(String message, boolean isUserMessage) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16f);
        textView.setPadding(16, 8, 16, 8);
        if (isUserMessage) {
            textView.setBackgroundResource(R.drawable.user_message_bg); // Background cho tin nhắn người dùng
        } else {
            textView.setBackgroundResource(R.drawable.system_message_bg); // Background cho tin nhắn hệ thống
        }

        // Thêm TextView vào container
        chatContainer.addView(textView);

        // Tự động cuộn xuống cuối cùng của ScrollView
        scrollViewChat.post(new Runnable() {
            @Override
            public void run() {
                scrollViewChat.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    // Giả lập phản hồi của hệ thống
    private void simulateSystemResponse(String userMessage) {
        // Tạo một phản hồi đơn giản
        String systemResponse = "Hệ thống đã nhận được: " + userMessage;
        addMessageToChat(systemResponse, false);
    }
}
