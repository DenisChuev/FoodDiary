package dc.food_diary.ui.main_tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import dc.food_diary.FoodListAdapter;
import dc.food_diary.FoodRepository;
import dc.food_diary.MainActivity;
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
        foodRepository.getFoodList().observe(this, foodList -> {
            adapter.submitList(foodList);
        });
        return adapter;
    }
}