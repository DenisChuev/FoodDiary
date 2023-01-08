package dc.food_diary.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dc.food_diary.FoodRepository;
import dc.food_diary.R;

public class DialogWeight extends BottomSheetDialogFragment {
    private FoodRepository repository;
    private EditText weightInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        repository = new FoodRepository(this.getActivity().getApplication());
        repository.getUser().observe(this, userProfile -> {
            weightInput.setText(String.valueOf(userProfile.getWeight()));
        });
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_weight, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weightInput = view.findViewById(R.id.weight_input);
        Button saveWeight = view.findViewById(R.id.save_weight_button);

        repository.updateUser();
        saveWeight.setOnClickListener(view1 -> {
            repository.updateUserGWeight(Double.parseDouble(weightInput.getText().toString()));
            DialogWeight.this.dismiss();
        });
    }

}
