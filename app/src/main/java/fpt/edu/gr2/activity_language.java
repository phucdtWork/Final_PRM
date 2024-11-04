package fpt.edu.gr2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class activity_language extends AppCompatActivity {

    private RadioButton radioVietnamese, radioEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        // Liên kết các view
        radioVietnamese = findViewById(R.id.radio_vietnamese);
        radioEnglish = findViewById(R.id.radio_english);

        // Kiểm tra ngôn ngữ đã lưu và cập nhật RadioButton
        String currentLanguage = getSharedPreferences("Settings", MODE_PRIVATE)
                .getString("My_Lang", "en"); // Mặc định là English
        if (currentLanguage.equals("vi")) {
            radioVietnamese.setChecked(true);
        } else {
            radioEnglish.setChecked(true);
        }


        // Thiết lập sự kiện cho nút quay lại
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Quay lại màn hình trước đó (Settings)
            }
        });

        // Xử lý sự kiện chọn ngôn ngữ
        radioVietnamese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi chọn Tiếng Việt, uncheck English
                radioEnglish.setChecked(false);
                setLocale("vi");
                // Thực hiện các thao tác liên quan đến chuyển đổi ngôn ngữ tại đây
            }
        });

        radioEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi chọn English, uncheck Vietnamese
                radioVietnamese.setChecked(false);
                setLocale("en");
                // Thực hiện các thao tác liên quan đến chuyển đổi ngôn ngữ tại đây
            }
        });
    }

    private void setLocale(String lang) {
        // Cập nhật ngôn ngữ
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Lưu ngôn ngữ đã chọn vào SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

        // Khởi động lại Activity để áp dụng ngôn ngữ mới
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
