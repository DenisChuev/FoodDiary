package dc.food_diary.ui.main_tabs.journal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;

import dc.food_diary.FoodRepository;
import dc.food_diary.NewFoodActivity;
import dc.food_diary.R;
import dc.food_diary.dao.Food;

public class FoodJournalFragment extends Fragment {

    private RecyclerView foodRecycler;
    private FoodListAdapter adapter;
    private FoodRepository foodRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_journal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodRecycler = view.findViewById(R.id.food_recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        foodRecycler.setLayoutManager(layoutManager);
        foodRecycler.setAdapter(initAdapter());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(foodRecycler.getContext(), DividerItemDecoration.VERTICAL);
        foodRecycler.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(FoodJournalFragment.this.getActivity(), NewFoodActivity.class);
            startActivity(intent);
        });

        enableSwipeToDeleteAndUndo();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(requireContext()) {
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

        foodRepository = new FoodRepository(getActivity().getApplication());
        foodRepository.getFoodList().observe(this.getActivity(), foodList -> {
            Collections.reverse(foodList);
            adapter.submitList(foodList);
        });
        return adapter;
    }
}