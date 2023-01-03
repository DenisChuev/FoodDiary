package dc.food_diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import dc.food_diary.dialog.DialogGrowth;
import dc.food_diary.dialog.DialogWeight;

public class AccountActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        db = FirebaseFirestore.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        ImageView accountPhoto = findViewById(R.id.account_photo);
        TextView accountName = findViewById(R.id.account_name);
        TextView accountEmail = findViewById(R.id.account_email);
        TextView accountGrowthText = findViewById(R.id.account_growth_value);
        TextView accountWeightText = findViewById(R.id.account_weight_value);
        ImageView growthArrow = findViewById(R.id.arrow_growth);
        ImageView weightArrow = findViewById(R.id.arrow_weight);

        growthArrow.setOnClickListener(view -> new DialogGrowth().show(getSupportFragmentManager(), null));
        weightArrow.setOnClickListener(view -> new DialogWeight().show(getSupportFragmentManager(), null));

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("email").equals(account.getEmail())) {
                            Glide.with(AccountActivity.this).load(document.get("photoUrl")).into(accountPhoto);
                            accountName.setText((CharSequence) document.get("displayName"));
                            accountEmail.setText((CharSequence) document.get("email"));

                            try {
                                double growth = (double) document.get("growth");
                                if (growth != 0.0) {
                                    accountGrowthText.setText("" + growth);
                                }

                                double weight = (double) document.get("weight");
                                if (weight != 0.0) {
                                    accountWeightText.setText("" + weight);
                                }

                            } catch (Exception e) {
                                Log.e("AccountActivity", e.getMessage(), e);
                            }

                        }
                    }
                }
            }
        });

    }
}