package dc.food_diary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food ORDER BY datetime(food_date) DESC")
    LiveData<List<Food>> getFoodList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Food food);

    @Query("DELETE FROM food")
    void deleteAll();

    @Query("DELETE from food where id = :foodId")
    void deleteFood(int foodId);

    @Query("SELECT food_text from food")
    LiveData<List<String>> getFood();
}
