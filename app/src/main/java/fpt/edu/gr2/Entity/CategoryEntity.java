package fpt.edu.gr2.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class CategoryEntity {

    @PrimaryKey(autoGenerate = true)
    private int categoryId;

    private String categoryName;
    private String icon; // Drawable resource name or URI
    private int userId; // Can be used for custom categories

    // Constructors, Getters, and Setters
    public CategoryEntity(String categoryName, String icon, int userId) {
        this.categoryName = categoryName;
        this.icon = icon;
        this.userId = userId;
    }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}

