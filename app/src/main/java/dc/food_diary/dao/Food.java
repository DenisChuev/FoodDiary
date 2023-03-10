package dc.food_diary.dao;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Food {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "food_date")
    private String foodDate = "";

    @NonNull
    @ColumnInfo(name = "food_text")
    private String foodText = "";

    @ColumnInfo(name = "food_type")
    private String foodType = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getFoodDate() {
        return foodDate;
    }

    public void setFoodDate(String foodDate) {
        this.foodDate = foodDate;
    }

    @NonNull
    public String getFoodText() {
        return foodText;
    }

    public void setFoodText(String foodText) {
        this.foodText = foodText;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
