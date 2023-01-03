package dc.food_diary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import dc.food_diary.dao.Food;

public class NewFoodActivity extends AppCompatActivity {
    FoodRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        repository = new FoodRepository(this.getApplication());

        TextView foodDate = findViewById(R.id.food_date);
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        foodDate.setText(timeStamp);

        TextView foodText = findViewById(R.id.food_text);


        Button saveBtn = findViewById(R.id.button_save);
        saveBtn.setOnClickListener(view -> {
            Food food = new Food();
            food.setFoodDate(foodDate.getText().toString());
            food.setFoodText(foodText.getText().toString());
            repository.insert(food);
            finish();
        });
    }
}