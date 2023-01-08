package dc.food_diary.ui.main_tabs.recipe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository repository;
    private MutableLiveData<List<Recipe>> recipes;

    public RecipeViewModel() {
        super();
        repository = new RecipeRepository();
        recipes = repository.getRecipes();
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void updateRecipes() {
        repository.updateRecipes();
    }
}