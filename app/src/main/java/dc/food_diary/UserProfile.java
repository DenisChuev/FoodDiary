package dc.food_diary;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.text.DecimalFormat;

public class UserProfile {
    private String displayName;
    private String email;
    private String photoUrl;
    private double growth;
    private double weight;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private String imt;

    public UserProfile(GoogleSignInAccount account) {
        displayName = account.getDisplayName();
        email = account.getEmail();
        photoUrl = String.valueOf(account.getPhotoUrl());
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public double getGrowth() {
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

    public void setImt(String imt) {
        this.imt = imt;
    }
}
