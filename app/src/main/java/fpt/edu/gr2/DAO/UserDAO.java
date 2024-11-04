package fpt.edu.gr2.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import fpt.edu.gr2.Entity.UserEntity;
import java.util.List;

@Dao
public interface UserDAO {

    // Truy vấn để lấy tất cả người dùng
    @Query("SELECT * FROM user_table")
    List<UserEntity> getAllUsers();

    @Query("SELECT * FROM user_table WHERE user_id = (:userId) LIMIT 1")
    UserEntity getUserById(int userId);
    // Insert a new user (Registration)
    @Insert
    void insertUser(UserEntity userEntity);

    // Query for logging in a user
    @Query("SELECT * FROM user_table WHERE user_name = (:username) AND password = (:password)")
    UserEntity loginUser(String username, String password);

    // Update user details (e.g., profile, password reset)
    @Update
    void updateUser(UserEntity user);

    // Validate security question answer (For password recovery)
    @Query("SELECT * FROM user_table WHERE password = (:password) ")
    UserEntity changeInfo(String password);


    @Query("SELECT * FROM user_table WHERE user_name = (:username) AND security_question = (:question) AND answer = (:answer)")
    UserEntity validateSecurityAnswer(String username, String question, String answer);

}

