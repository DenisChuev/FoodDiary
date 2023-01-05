package dc.food_diary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import dc.food_diary.dao.Food;

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

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_account) {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();

                Food food = adapter.getCurrentList().get(position);
                foodRepository.delete(food);

                Snackbar snackbar = Snackbar.make(foodRecycler, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        foodRepository.insert(food);
                    }
                });
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