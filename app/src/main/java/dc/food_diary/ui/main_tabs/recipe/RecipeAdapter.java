package dc.food_diary.ui.main_tabs.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import dc.food_diary.R;

public class RecipeAdapter extends ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder> {
    private Context context;

    public RecipeAdapter(Context context) {
        super(new DiffUtil.ItemCallback<Recipe>() {
            @Override
            public boolean areItemsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
                return oldItem.getUrl().equals(newItem.getUrl());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Recipe oldItem, @NonNull Recipe newItem) {
                return oldItem.getUrl().equals(newItem.getUrl());
            }
        });
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeTitle;
        private ImageView recipeImg;

        public RecipeViewHolder(@NonNull View view) {
            super(view);
            recipeTitle = view.findViewById(R.id.recipe_title);
            recipeImg = view.findViewById(R.id.recipe_image);
        }

        public void bind(Recipe recipe) {
            recipeTitle.setText(recipe.getName());
            Glide.with(context).load(recipe.getImg()).into(recipeImg);
        }
    }

}
