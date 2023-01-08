package dc.food_diary;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class FoodPreferences {
    private SharedPreferences sharedPrefs;
    private String documentId;
    private static final String TAG = "FoodPreferences";

    public FoodPreferences(Application application) {
        sharedPrefs = application.getApplicationContext().getSharedPreferences("dc.food_diary", Context.MODE_PRIVATE);
        documentId = application.getString(R.string.user_document_id);
    }


    public String getDocumentId() {
        String document = sharedPrefs.getString(documentId, "");
        Log.d(TAG, document);
        return document;
    }

    public void setDocumentId(String document) {
        Log.d(TAG, document);
        sharedPrefs.edit().putString(documentId, document).apply();

    }
}
