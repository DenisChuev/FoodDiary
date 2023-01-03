package dc.food_diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private SignInButton signInButton;
    private GoogleSignInClient signInClient;
    private static final int RC_SIGN_IN = 1;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View view) {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        openMainActivity(account);
    }

    private void openMainActivity(GoogleSignInAccount account) {
        if (account != null) {
            UserProfile user = new UserProfile(account);

            db.collection("users").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    boolean haveUser = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Firestore", document.getId());
                        Log.d("Firestore", String.valueOf(document.getData()));
                        if (String.valueOf(document.get("email")).equals(account.getEmail())) {
                            haveUser = true;

                            SharedPreferences sharedPref = LoginActivity.this.getApplicationContext().getSharedPreferences("dc.food_diary", Context.MODE_PRIVATE);
                            sharedPref.edit().putString(getString(R.string.user_document_id), document.getId()).apply();

                            break;
                        }
                    }
                    if (!haveUser) {
                        db.collection("users").add(user)
                                .addOnSuccessListener(documentReference ->
                                {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    SharedPreferences sharedPref = LoginActivity.this.getApplicationContext().getSharedPreferences("dc.food_diary", Context.MODE_PRIVATE);
                                    sharedPref.edit().putString(getString(R.string.user_document_id), documentReference.getId()).apply();
                                })
                                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                    }
                }
            });

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.d(TAG, "account is null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String idToken = account.getIdToken();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            FirebaseAuth firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> openMainActivity(account));

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }
}