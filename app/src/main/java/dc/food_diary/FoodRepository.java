package dc.food_diary;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dc.food_diary.dao.Food;
import dc.food_diary.dao.FoodDao;
import dc.food_diary.dao.FoodDatabase;

public class FoodRepository {
    private static final String TAG = "FoodRepository";
    private FoodDao foodDao;
    private LiveData<List<Food>> foodList;
    private FoodDatabase foodDb;
    private FirebaseFirestore foodFirestoreDb;
    private FoodPreferences prefs;
    private MutableLiveData<UserProfile> user = new MutableLiveData<>();

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

    public MutableLiveData<UserProfile> getUser() {
        return user;
    }

    public void updateUser() {
        UserProfile currentUser = new UserProfile();
        getFirestoreUser().get().addOnSuccessListener(userInfo -> {
            currentUser.setDisplayName(String.valueOf(userInfo.get("displayName")));
            currentUser.setPhotoUrl(String.valueOf(userInfo.get("photoUrl")));
            currentUser.setEmail(String.valueOf(userInfo.get("email")));
            currentUser.setGrowth((Double) userInfo.get("growth"));
            currentUser.setWeight((Double) userInfo.get("weight"));
            currentUser.setImt(String.valueOf(userInfo.get("imt")));

            user.setValue(currentUser);
        });
    }

    private DocumentReference getFirestoreUser() {
        String documentId = prefs.getDocumentId();
        Log.d("FoodRepository", documentId);
        return foodFirestoreDb.collection("users").document(documentId);
    }

    public void insert(Food food) {
        foodDb.databaseWriteExecutor.execute(() -> {
            foodDao.insert(food);
            //getFirestoreUser()
//                    .collection("food").add(food)
//                    .addOnSuccessListener(documentReference -> Log.d("FoodRepository", documentReference.getId()));
        });

    }

    public LiveData<List<String>> getTopFood() {
        return foodDao.getFood();
    }

    public void delete(Food food) {
        foodDb.databaseWriteExecutor.execute(() -> {
            foodDao.deleteFood(food.getId());
        });
    }

    public void updateUserGrowth(double growth) {
        Log.d(TAG, "updateUserGrowth " + growth);
        getFirestoreUser().update("growth", growth).addOnSuccessListener(u -> {
            Log.d(TAG, "updateUserGrowth updated");
            updateUser();
        });
    }


    public void updateUserGWeight(double weight) {
        Log.d(TAG, "updateUserWeight " + weight);
        getFirestoreUser().update("weight", weight).addOnSuccessListener(u -> {
            Log.d(TAG, "updateUserWeight updated");
            updateUser();
        });
    }

    public void updateUserIMT(String imt) {
        Log.d(TAG, "updateUserImt " + imt);
        getFirestoreUser().update("imt", imt).addOnSuccessListener(u -> {
            Log.d(TAG, "updateUserImt updated");
            updateUser();
        });
    }
}
