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
import androidx.lifecycle.Observer;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dc.food_diary.FoodRepository;
import dc.food_diary.R;
import dc.food_diary.UserProfile;

public class DialogGrowth extends BottomSheetDialogFragment {
    private FoodRepository repository;
    private EditText growthInput;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        repository = new FoodRepository(this.getActivity().getApplication());
        repository.getUser().observe(this, userProfile -> {
            growthInput.setText(String.valueOf(userProfile.getGrowth()));
        });
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_growth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        growthInput = view.findViewById(R.id.growth_input);
        Button saveGrowth = view.findViewById(R.id.save_growth_button);

        repository.updateUser();
        saveGrowth.setOnClickListener(v -> {
            repository.updateUserGrowth(Double.parseDouble(growthInput.getText().toString()));
            DialogGrowth.this.dismiss();
        });
    }
}
