package dc.food_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import dc.food_diary.dao.Food;

public class NewFoodActivity extends AppCompatActivity {
    private FoodRepository repository;
    private String foodType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        repository = new FoodRepository(this.getApplication());
        Food food = new Food();

        TextView foodDate = findViewById(R.id.food_date);
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        foodDate.setText(timeStamp);

        TextView foodText = findViewById(R.id.food_text);

        Spinner foodType = findViewById(R.id.food_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodType.setAdapter(adapter);
        foodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String[] foodTypes = getResources().getStringArray(R.array.food_types);
                food.setFoodType(foodTypes[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button saveBtn = findViewById(R.id.button_save);
        saveBtn.setOnClickListener(view -> {
            food.setFoodDate(foodDate.getText().toString());
            food.setFoodText(foodText.getText().toString());
            repository.insert(food);
            finish();
        });
    }
}