package dc.food_diary.ui.main_tabs.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dc.food_diary.R;

public class RecipeFragment extends Fragment {
    private RecipeViewModel mViewModel;
    private RecyclerView recipesRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        recipesRecycler = view.findViewById(R.id.recipes_recyclerview);
        recipesRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        RecipeAdapter recipesAdapter = new RecipeAdapter(requireActivity());
        recipesRecycler.setAdapter(recipesAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recipesRecycler.getContext(), DividerItemDecoration.VERTICAL);
        recipesRecycler.addItemDecoration(dividerItemDecoration);

        mViewModel.updateRecipes();
        mViewModel.getRecipes().observe(requireActivity(), recipesAdapter::submitList);
    }
}
