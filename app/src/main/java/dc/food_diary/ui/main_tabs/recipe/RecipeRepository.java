package dc.food_diary.ui.main_tabs.recipe;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {
    public static final String TAG = "RecipeRepository";
    private MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();


    class RecipesTask extends AsyncTask<Void, Void, List<Recipe>> {

        protected List<Recipe> doInBackground(Void... arg0) {
            List<Recipe> recipes = new ArrayList<>();
            try {
                recipes = getRecipes(1);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            return recipes;
        }

        protected void onPostExecute(List<Recipe> result) {
            recipes.setValue(result);
        }
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void updateRecipes() {
        new RecipesTask().execute();
    }

    private List<Recipe> getRecipes(int page) throws IOException {
        List<Recipe> recipesList = new ArrayList<>();

        Document doc = Jsoup.connect("https://www.povarenok.ru/recipes/~" + page + "/?sort=date_create&order=desc").get();
        Elements recipes = doc.getElementsByTag("article");

        for (Element e : recipes) {
            Recipe recipe = new Recipe();
            Element recipeInfo = e.getElementsByTag("h2").first().getElementsByAttribute("href").first();
            recipe.setUrl(recipeInfo.attr("href"));
            recipe.setName(String.valueOf(recipeInfo.firstChild()));
            recipe.setImg(e.getElementsByTag("img").first().attr("src"));
            recipe.setCategory(String.valueOf(e.getElementsByClass("article-breadcrumbs").first().getElementsByAttribute("href").first().firstChild()));
            recipe.setSummary(String.valueOf(e.getElementsByTag("p").get(1).firstChild()));
            recipesList.add(recipe);
        }
        Log.d(TAG, String.valueOf(recipesList));
        return recipesList;
    }

}
