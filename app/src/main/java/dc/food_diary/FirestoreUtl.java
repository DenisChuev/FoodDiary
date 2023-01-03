package dc.food_diary;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FirestoreUtl {
    public static boolean haveUser(@NonNull FirebaseFirestore db, String email) {
        final boolean[] result = new boolean[1];

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("Firestore", document.getId());
                    Log.d("Firestore", String.valueOf(document.getData()));
                    if (String.valueOf(document.get("email")).equals(email)) {
                        result[0] = true;
                        break;
                    }
                }
            }
        });
        Log.d("Firestore", String.valueOf(result[0]));
        return result[0];
    }

    @NonNull
    public static UserProfile getUser(@NonNull FirebaseFirestore db, String email) {
        UserProfile user = new UserProfile();

        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.get("email").equals(email)) {
                        user.setEmail(email);
                        user.setDisplayName(String.valueOf(document.get("displayName")));
                        user.setPhotoUrl(Uri.parse(String.valueOf(document.get("photoUrl"))));
                        double growth = (double) document.get("growth");
                        double weight = (double) document.get("weight");
                        user.setGrowth(growth);
                        user.setWeight(weight);
                        break;
                    }
                }
            }
        });
        return user;
    }
}
