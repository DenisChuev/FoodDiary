package dc.food_diary.ui.main_tabs.journal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import dc.food_diary.R;
import dc.food_diary.dao.Food;

public class FoodListAdapter extends ListAdapter<Food, FoodListAdapter.FoodViewHolder> {
    public FoodListAdapter(@NonNull DiffUtil.ItemCallback<Food> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = getItem(position);
        holder.bind(food);
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodDate;
        TextView foodText;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodDate = itemView.findViewById(R.id.food_date);
            foodText = itemView.findViewById(R.id.food_text);
        }

        public void bind(Food food) {
            foodDate.setText(food.getFoodDate());
            foodText.setText(food.getFoodText());
        }
    }
}
