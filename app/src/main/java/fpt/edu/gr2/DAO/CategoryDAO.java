package fpt.edu.gr2.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import fpt.edu.gr2.Entity.CategoryEntity;

@Dao
public interface CategoryDAO {

    // Insert a new category
    @Insert
    void insertCategory(CategoryEntity category);

    // Update an existing category
    @Update
    void updateCategory(CategoryEntity category);

    // Delete a category by id
    @Query("DELETE FROM categories WHERE categoryId = :categoryId")
    void deleteCategory(int categoryId);

    // Get all categories
    @Query("SELECT * FROM categories ORDER BY categoryName ASC")
    List<CategoryEntity> getAllCategories();
}
