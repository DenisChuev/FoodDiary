package dc.food_diary;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class FoodPreferences {
    private SharedPreferences sharedPrefs;
    private String documentId;

    public FoodPreferences(Application application) {
        sharedPrefs = application.getApplicationContext().getSharedPreferences("dc.food_diary", Context.MODE_PRIVATE);
        documentId = application.getString(R.string.user_document_id);
    }


    public String getDocumentId() {
        return sharedPrefs.getString(documentId, "");
    }

    public void setDocumentId(String documentId) {
        sharedPrefs.edit().putString(documentId, documentId).apply();

    }
}
