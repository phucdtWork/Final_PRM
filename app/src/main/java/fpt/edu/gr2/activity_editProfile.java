package fpt.edu.gr2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import fpt.edu.gr2.DAO.UserDAO;
import fpt.edu.gr2.Database.AppDatabase;
import fpt.edu.gr2.Entity.UserEntity;

public class activity_editProfile extends AppCompatActivity {

    private EditText
            etAnswer,
            etDob,
            etOldPassword,
            etNewPassword,
            etConfirmNewPassword;
    private Button btnSaveChanges;
    private ImageButton btnBack;
    private Spinner etQuestion ;
    private AppDatabase appDatabase;
    private UserDAO userDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        initializeDatabase();
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


    private void initializeDatabase() {
        appDatabase = AppDatabase.getDatabase(this);
        userDAO = appDatabase.UserDAO();
    }

    private void initView() {
        etDob = findViewById(R.id.et_dob);
        etQuestion = findViewById(R.id.spinner_security_question);
        etAnswer = findViewById(R.id.et_answer);
        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmNewPassword = findViewById(R.id.et_confirm_new_password);
        btnSaveChanges = findViewById(R.id.btn_save_changes);
        btnBack = findViewById(R.id.btn_back);

        btnSaveChanges.setOnClickListener(v -> updateProfile());
        btnBack.setOnClickListener(v -> onBack());
    }

    private void onBack() {
        Intent intent = new Intent(activity_editProfile.this, activity_settings.class);
        startActivity(intent);
        finish();
    }

    private void updateProfile() {
        String edDob = etDob.getText().toString().trim();
        String edQuestion = etQuestion.getSelectedItem().toString().trim();
        String edAnswer = etAnswer.getText().toString().trim();
        String edOldPassword = etOldPassword.getText().toString().trim();
        String edNewPassword = etNewPassword.getText().toString().trim();
        String edComfirmPassWord = etConfirmNewPassword.getText().toString().trim();
        try {
            UserEntity user = userDAO.changeInfo(edOldPassword);
            if (validateInput()) {
                if (user != null) {
                    user.setPassword(edNewPassword);
                    user.setDob(edDob);
                    user.setSecurity_question(edQuestion);
                    user.setAnswer(edAnswer);
                    userDAO.updateUser(user);
                    Toast.makeText(activity_editProfile.this, "Update profile successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_editProfile.this, activity_settings.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(activity_editProfile.this, "Update profile failed", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity_editProfile.this, "Error during update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {

        String edDob = etDob.getText().toString().trim();
        String edOldPassword = etOldPassword.getText().toString().trim();
        String edNewPassword = etNewPassword.getText().toString().trim();
        String edComfirmPassWord = etConfirmNewPassword.getText().toString().trim();
        String edQuestion = etQuestion.getSelectedItem().toString().trim();
        String edAnswer = etAnswer.getText().toString().trim();
        if (TextUtils.isEmpty(edAnswer) || TextUtils.isEmpty(edDob) || TextUtils.isEmpty(edOldPassword) || TextUtils.isEmpty(edNewPassword)) {
            Toast.makeText(activity_editProfile.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edOldPassword.equals(edNewPassword)) {
            Toast.makeText(activity_editProfile.this, "Please enter Old password alike New password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!edNewPassword.equals(edComfirmPassWord)) {
            Toast.makeText(activity_editProfile.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
