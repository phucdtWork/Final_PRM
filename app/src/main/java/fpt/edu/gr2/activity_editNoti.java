package fpt.edu.gr2;

import android.os.Bundle;
import android.view.View;  // Import cần thiết cho OnClickListener

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_editNoti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_noti);

        // Thiết lập insets cho switch_notifications
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.switch_notifications), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Thêm Click Listener cho nút back để quay lại màn hình Settings
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Quay lại màn hình trước đó (màn hình Settings)
            }
        });
    }
}
