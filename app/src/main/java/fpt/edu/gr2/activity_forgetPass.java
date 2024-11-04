package fpt.edu.gr2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fpt.edu.gr2.DAO.UserDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.UserEntity;

public class activity_forgetPass extends AppCompatActivity {

    private EditText userName,forGotPassword, confirmPassword, answer;
    private Spinner forGotQuestion;
    private TextView backToLogin;
    private Button sendRequest;
    private AppDatabase appDatabase;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_pass);
        initView();
        initializeDatabase();
        hideSoftKeyBoard();

        // Lấy Spinner
        Spinner securityQuestionSpinner = findViewById(R.id.spinner_security_question);

        // Tạo Adapter từ mảng câu hỏi bảo mật
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.security_questions_array, android.R.layout.simple_spinner_item);

        // Thiết lập layout cho dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán Adapter cho Spinner
        securityQuestionSpinner.setAdapter(adapter);
    }

    public void hideSoftKeyBoard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void initializeDatabase() {
        appDatabase = AppDatabase.getDatabase(this);
        userDAO = appDatabase.UserDAO();
    }

    private void initView() {
        userName = findViewById(R.id.et_username);
        forGotPassword = findViewById(R.id.et_new_password);
        backToLogin = findViewById(R.id.tv_back_to_login);
        sendRequest = findViewById(R.id.btn_send_request);
        confirmPassword = findViewById(R.id.et_confirm_new_password);
        answer = findViewById(R.id.et_answer);
        forGotQuestion = findViewById(R.id.spinner_security_question);
        backToLogin.setOnClickListener(v -> backToLogin());
        sendRequest.setOnClickListener(v -> sendRequest());
    }

    private void sendRequest() {
        String userName = this.userName.getText().toString().trim();
        String password = forGotPassword.getText().toString().trim();
        String confirmPassword = this.confirmPassword.getText().toString().trim();
        String answer = this.answer.getText().toString().trim();
        String forGotQuestion = this.forGotQuestion.getSelectedItem().toString();
        if (validationForGotPassword()) {
            try {
                UserEntity user = userDAO.validateSecurityAnswer(userName,forGotQuestion, answer);
                if (user != null) {
                    user.setPassword(password);
                    userDAO.updateUser(user);
                    Toast.makeText(activity_forgetPass.this, "Forgot password successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_forgetPass.this, activity_login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(activity_forgetPass.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity_forgetPass.this, "Error durring forgot password", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void backToLogin() {
        Intent intent = new Intent(activity_forgetPass.this, activity_login.class);
        startActivity(intent);
        finish();
    }

    private boolean validationForGotPassword() {
        String username = userName.getText().toString().trim();
        String password = forGotPassword.getText().toString().trim();
        String confirmPassword = this.confirmPassword.getText().toString().trim();
        String answer = this.answer.getText().toString().trim();
        String forGotQuestion = this.forGotQuestion.getSelectedItem().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)|| TextUtils.isEmpty(answer) || TextUtils.isEmpty(forGotQuestion)) {
            Toast.makeText(this, "Please enter all field ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!confirmPassword.equals(password)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


}
