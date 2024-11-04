package fpt.edu.gr2.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class UserEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int user_id;
    private String user_name;
    private String password;
    private String dob;
    private double balance;
    private String security_question;
    private String answer;


//    private double expence;

//    public double getExpence() {
//        return expence;
//    }
//
//    public void setExpence(double expence) {
//        this.expence = expence;
//    }

    public UserEntity() {
    }

    public UserEntity(String user_name, String password, String dob, double balance, String security_question, String answer) {
        this.user_name = user_name;
        this.password = password;
        this.dob = dob;
        this.balance = balance;
        this.security_question = security_question;
        this.answer = answer;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public String getSecurity_question() {
        return security_question;
    }

    public void setSecurity_question(String security_question) {
        this.security_question = security_question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    // Constructor

}
