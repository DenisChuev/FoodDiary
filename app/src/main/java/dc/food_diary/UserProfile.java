package dc.food_diary;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DecimalFormat;

public class UserProfile {
    private String displayName;
    private String email;
    private Uri photoUrl;
    private double growth;
    private double weight;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public UserProfile(GoogleSignInAccount account) {
        displayName = account.getDisplayName();
        email = account.getEmail();
        photoUrl = account.getPhotoUrl();
    }

    public UserProfile() {

    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public double setGrowth() {
        return growth;
    }

    public void setGrowth(double growth) {
        this.growth = growth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getImt() {
        return df.format(weight / (growth * growth) * 10000);
    }
}
