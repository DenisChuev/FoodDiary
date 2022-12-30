package dc.food_diary;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FoodRepository {

    private FoodDao foodDao;
    private LiveData<List<Food>> foodList;
    private FoodDatabase foodDb;
    private FirebaseFirestore foodFirestoreDb;
    private SharedPreferences sharedPref;

    FoodRepository(Application application) {
        sharedPref = application.getApplicationContext().getSharedPreferences("dc.food_diary", Context.MODE_PRIVATE);

        foodFirestoreDb = FirebaseFirestore.getInstance();
        foodDb = FoodDatabase.getDatabase(application);
        foodDao = foodDb.foodDao();
        foodList = foodDao.getFoodList();
    }

    LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    void insert(Food food) {
        foodDb.databaseWriteExecutor.execute(() -> {
            foodDao.insert(food);
        });
        String documentId = sharedPref.getString("userDocumentId", "");
        Log.d("FoodRepository", documentId);

        foodFirestoreDb.collection("users").document(documentId)
                .collection("food").add(food)
                .addOnSuccessListener(documentReference -> Log.d("FoodRepository", documentReference.getId()));
    }

    void delete(Food food) {
        foodDb.databaseWriteExecutor.execute(() -> {
            foodDao.deleteFood(food.getId());
        });
    }

}
