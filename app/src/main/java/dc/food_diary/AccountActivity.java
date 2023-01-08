package dc.food_diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import dc.food_diary.dialog.DialogGrowth;
import dc.food_diary.dialog.DialogWeight;

public class AccountActivity extends AppCompatActivity {
    private FoodRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        repository = new FoodRepository(getApplication());

        ImageView accountPhoto = findViewById(R.id.account_photo);
        TextView accountName = findViewById(R.id.account_name);
        TextView accountEmail = findViewById(R.id.account_email);
        TextView accountGrowthText = findViewById(R.id.account_growth_value);
        TextView accountWeightText = findViewById(R.id.account_weight_value);
        TextView imt = findViewById(R.id.account_imt_value);

        View viewWeight = findViewById(R.id.account_weight);
        View viewGrowth = findViewById(R.id.account_growth);
        View viewImt = findViewById(R.id.account_imt);

        Toolbar account_toolbar = findViewById(R.id.account_toolbar);
        setSupportActionBar(account_toolbar);

        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(view -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient client = GoogleSignIn.getClient(AccountActivity.this, gso);

            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(client.asGoogleApiClient()).setResultCallback(
                    status -> {
                        finish();
                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                    });
        });


        viewGrowth.setOnClickListener(view -> new DialogGrowth().show(getSupportFragmentManager(), null));
        viewWeight.setOnClickListener(view -> new DialogWeight().show(getSupportFragmentManager(), null));

        repository.updateUser();
        repository.getUser().observe(this, userProfile -> {
            Glide.with(AccountActivity.this).load(userProfile.getPhotoUrl()).into(accountPhoto);
            accountName.setText(userProfile.getDisplayName());
            accountEmail.setText(userProfile.getEmail());
            if (userProfile.getGrowth() != 0.0) {
                accountGrowthText.setText("" + userProfile.getGrowth());
            }
            if (userProfile.getWeight() != 0.0) {
                accountWeightText.setText("" + userProfile.getWeight());
            }
            if (userProfile.getGrowth() != 0.0 && userProfile.getWeight() != 0.0) {
                imt.setText(userProfile.getImt());
                repository.updateUserIMT(String.valueOf(imt.getText()));
            }
        });

    }
}