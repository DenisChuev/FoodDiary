package dc.food_diary.ui.main_tabs;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.chart.common.dataentry.DataEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dc.food_diary.FoodRepository;
import dc.food_diary.R;
import dc.food_diary.dao.FoodDatabase;

public class StatsFragment extends Fragment {
    private FoodRepository foodRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AnyChartView anyChartView = (AnyChartView) getActivity().findViewById(R.id.stats_top_products);
        foodRepository = new FoodRepository(getActivity().getApplication());

        foodRepository.getTopFood().observe(this.getActivity(), allFood -> {
                    HashMap<String, Integer> top = new HashMap<>();
                    for (String foodRecord : allFood) {
                        String[] foodMeal = foodRecord.replaceAll(", ", ",").split(",");
                        for (String food : foodMeal) {
                            if (top.containsKey(food)) {
                                top.put(food, top.get(food) + 1);
                            } else {
                                top.put(food, 1);
                            }
                        }
                    }

                    @SuppressLint({"NewApi", "LocalSuppress"}) Map<String, Integer> sorted = top
                            .entrySet()
                            .stream()
                            .sorted(comparingByValue())
                            .collect(
                                    toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                            LinkedHashMap::new));
                    Pie pie = AnyChart.pie();

                    List<DataEntry> data = new ArrayList<>();
                    int counter = 0;
                    for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
                        if (++counter < sorted.size() - 6) continue;
                        data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                    }
                    Collections.reverse(data);
                    Log.d("Top7", String.valueOf(data));

                    pie.data(data);
                    anyChartView.setChart(pie);
                }
        );


    }
}
