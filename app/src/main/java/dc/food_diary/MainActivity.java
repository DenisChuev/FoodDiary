package dc.food_diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LiveData<List<Food>> foodList;
    private RecyclerView foodRecycler;
    private FoodListAdapter adapter;
    private FoodRepository foodRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodRecycler = findViewById(R.id.food_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        foodRecycler.setLayoutManager(layoutManager);
        foodRecycler.setAdapter(initAdapter());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(foodRecycler.getContext(), DividerItemDecoration.VERTICAL);
        foodRecycler.addItemDecoration(dividerItemDecoration);
        enableSwipeToDeleteAndUndo();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewFoodActivity.class);
            startActivity(intent);
        });
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                foodRepository.delete(adapter.getCurrentList().get(position));

                Snackbar snackbar = Snackbar.make(foodRecycler, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(foodRecycler);
    }

    private FoodListAdapter initAdapter() {
        adapter =
                new FoodListAdapter(new DiffUtil.ItemCallback<Food>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull Food oldItem, @NonNull Food newItem) {
                        return oldItem.getId() == newItem.getId();
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull Food oldItem, @NonNull Food newItem) {
                        return oldItem.getFoodText().equals(newItem.getFoodText());
                    }
                });

        foodRepository = new FoodRepository(this.getApplication());
        foodRepository.getFoodList().observe(this, foodList -> {
            adapter.submitList(foodList);
        });
        return adapter;
    }
}