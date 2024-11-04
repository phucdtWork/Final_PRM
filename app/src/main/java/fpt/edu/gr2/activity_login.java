package fpt.edu.gr2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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

public class activity_login extends AppCompatActivity {
    private EditText usernamelogin, passwordlogin;
    private Button button_login;
    private TextView forgotpassword, signup;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        initializeDatabase();
        initView();
        hideSoftKeyBoard();

    }

    // Tìm kiếm các thành phần giao diện
    private void initView() {
        usernamelogin = findViewById(R.id.et_username);
        passwordlogin = findViewById(R.id.et_password);
        button_login = findViewById(R.id.btn_login);
        forgotpassword = findViewById(R.id.tv_forget_password);
        signup = findViewById(R.id.tv_sign_up);

        button_login.setOnClickListener(v -> UserLogin());
        forgotpassword.setOnClickListener(v -> backForgotPassword());
        signup.setOnClickListener(v -> backSignUp());
    }

    private void initializeDatabase() {
        AppDatabase dbConnection = AppDatabase.getDatabase(this);
        userDAO = dbConnection.UserDAO();
    }


    private void UserLogin() {
        String username = usernamelogin.getText().toString().trim();
        String passeword = passwordlogin.getText().toString().trim();
        if (username.isEmpty() || passeword.isEmpty()) {
            Toast.makeText(activity_login.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
        } else {
            try {
                UserEntity user = userDAO.loginUser(username, passeword);



                if (user == null){
                    Toast.makeText(activity_login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(activity_login.this, activity_home.class);
//                   intent.putExtra("income" ,user.getBalance());

//
//                  intent.putExtra("expense" ,user.getExpence());
                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                            .edit()
                            .putInt("current_user_id", user.getUser_id())
                            .apply();
                    Toast.makeText(activity_login.this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
                Intent intent = new Intent(activity_login.this, activity_login.class);
                startActivity(intent);
                finish();
                Toast.makeText(activity_login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

            }

        }
    }



    // back to forgot password
    private void backForgotPassword() {
        Intent intent = new Intent(activity_login.this, activity_forgetPass.class);
        startActivity(intent);
        finish();

    }


    // back to sign up
    private void backSignUp() {
        Intent intent = new Intent(activity_login.this, activity_signup.class);
        startActivity(intent);
        finish();
    }

    public void hideSoftKeyBoard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
