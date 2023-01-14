package dc.food_diary.ui.main_tabs.recipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dc.food_diary.R;

public class RecipeWebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_webview);
        WebView recipeView = findViewById(R.id.recipe_webview);
        String url = getIntent().getStringExtra("RECIPE_URL");
        recipeView.loadUrl(url);
        recipeView.getSettings().setJavaScriptEnabled(true);
        recipeView.setWebViewClient(new WebViewClient());
    }
}