package dc.food_diary;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.List;

import dc.food_diary.dao.Food;
import dc.food_diary.dao.FoodDao;
import dc.food_diary.dao.FoodDatabase;

public class FoodRepository {

    private FoodDao foodDao;
    private LiveData<List<Food>> foodList;
    private FoodDatabase foodDb;
    private FirebaseFirestore foodFirestoreDb;
    private FoodPreferences prefs;


    public FoodRepository(Application application) {
        prefs = new FoodPreferences(application);
        foodFirestoreDb = FirebaseFirestore.getInstance();
        foodDb = FoodDatabase.getDatabase(application);
        foodDao = foodDb.foodDao();
        foodList = foodDao.getFoodList();
    }

    public LiveData<List<Food>> getFoodList() {
        return foodList;
    }

    public void insert(Food food) {
        foodDb.databaseWriteExecutor.execute(() -> {
            foodDao.insert(food);

            String documentId = prefs.getDocumentId();
            Log.d("FoodRepository", documentId);
            foodFirestoreDb.collection("users").document(documentId)
                    .collection("food").add(food)
                    .addOnSuccessListener(documentReference -> Log.d("FoodRepository", documentReference.getId()));
        });

    }

    public void delete(Food food) {
        foodDb.databaseWriteExecutor.execute(() -> {
            foodDao.deleteFood(food.getId());
        });
    }

    public void updateUserGrowth(double growth) {
        String documentId = prefs.getDocumentId();
        foodFirestoreDb.collection("users").document(documentId).update("growth", growth);
    }


    public void updateUserGWeight(double weight) {
        String documentId = prefs.getDocumentId();
        foodFirestoreDb.collection("users").document(documentId).update("weight", weight);
    }

    public void updateUserIMT(String imt) {
        String documentId = prefs.getDocumentId();
        foodFirestoreDb.collection("users").document(documentId).update("imt", imt);
    }
}
