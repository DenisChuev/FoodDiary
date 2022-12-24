package dc.food_diary;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodRepository {

    private FoodDao foodDao;
    private LiveData<List<Food>> foodList;
    FoodDatabase db;

    FoodRepository(Application application) {
        db = FoodDatabase.getDatabase(application);
        foodDao = db.foodDao();
        foodList = foodDao.getFoodList();
    }

    LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    void insert(Food food) {
        db.databaseWriteExecutor.execute(() -> {
            foodDao.insert(food);
        });
    }

    void delete(Food food) {
        db.databaseWriteExecutor.execute(() -> {
            foodDao.deleteFood(food.getId());
        });
    }

}
