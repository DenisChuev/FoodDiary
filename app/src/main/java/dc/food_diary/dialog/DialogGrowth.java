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

public class DialogGrowth extends BottomSheetDialogFragment {
    private FoodRepository repository;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        repository = new FoodRepository(this.getActivity().getApplication());
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
        EditText growthInput = view.findViewById(R.id.growth_input);
        Button saveGrowth = view.findViewById(R.id.save_growth_button);
        saveGrowth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repository.updateUserGrowth(Double.parseDouble(growthInput.getText().toString()));
                DialogGrowth.this.dismiss();
            }
        });
    }
}
