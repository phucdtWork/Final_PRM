package fpt.edu.gr2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fpt.edu.gr2.DAO.UserDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.UserEntity;

public class activity_signup extends AppCompatActivity {
    private TextView usernameId, passwordId, dateId, comfirmpasswordId, answerId;
    private Spinner questionId;
    private Button signupId;
    private TextView signinId;
    private UserDAO userDAO;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        initView();
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

    private void initView() {
        usernameId = findViewById(R.id.et_username);
        passwordId = findViewById(R.id.et_password);
        dateId = findViewById(R.id.et_dob);
        questionId = findViewById(R.id.spinner_security_question);
        answerId = findViewById(R.id.et_answer);
        comfirmpasswordId = findViewById(R.id.et_confirm_password);
        signupId = findViewById(R.id.btn_sign_up);
        signinId = findViewById(R.id.tv_sign_in);
        AppDatabase appDatabase = AppDatabase.getDatabase(this);
        userDAO = appDatabase.UserDAO();

        signupId.setOnClickListener(v -> RegisterUser());
        signinId.setOnClickListener(v -> BackloginUser());
    }


    private void BackloginUser() {
        Intent intent = new Intent(activity_signup.this, activity_login.class);
        startActivity(intent);
        finish();
    }


    private void RegisterUser() {
        if (validateInput()) {
            String username = usernameId.getText().toString().trim();
            String password = passwordId.getText().toString().trim();
            String dob = dateId.getText().toString().trim();
            String question = questionId.getSelectedItem().toString();
            String answer = answerId.getText().toString().trim();
            String comfirmpassword = comfirmpasswordId.getText().toString().trim();
            try {
                UserEntity newUser = new UserEntity(username, password, dob, 0.0, question, answer);
                AppDatabase.getDatabase(this).UserDAO().insertUser(newUser);
                Toast.makeText(getApplicationContext(), "Register Succesfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_signup.this, activity_login.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                Intent intent = new Intent(activity_signup.this, activity_home.class);
                startActivity(intent);
                showToastOnMainThread("Error during registration!");
            }
        }
    }


    private void showToastOnMainThread(String message) {
        runOnUiThread(() -> Toast.makeText(activity_signup.this, message, Toast.LENGTH_SHORT).show());
    }


    private boolean validateInput() {
        String userName = usernameId.getText().toString().trim();
        String dob = dateId.getText().toString().trim();
        String confirmPass = comfirmpasswordId.getText().toString().trim();
        String password = passwordId.getText().toString().trim();
        String question = questionId.getSelectedItem().toString();
        String answer = answerId.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(confirmPass) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(question)|| TextUtils.isEmpty(answer)) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!confirmPass.equals(password)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}













