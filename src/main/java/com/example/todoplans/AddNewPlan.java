package com.example.todoplans;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todoplans.model.TodoModel;
import com.example.todoplans.utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewPlan extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText etNewPlan;
    private Button btnSavePlan;
    private DatabaseHandler db;

    public static AddNewPlan newInstance() {
        return new AddNewPlan();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_item, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNewPlan = getView().findViewById(R.id.tv_new_plan);
        btnSavePlan = getView().findViewById(R.id.btn_save_plan);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String plan = bundle.getString("plan");
            etNewPlan.setText(plan);
            assert plan != null;
            if (plan.length() > 0)
            btnSavePlan.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        etNewPlan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    btnSavePlan.setEnabled(false);
                    btnSavePlan.setTextColor(Color.GRAY);
                } else {
                    btnSavePlan.setEnabled(true);
                    btnSavePlan.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        btnSavePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etNewPlan.getText().toString();
                if (finalIsUpdate) {
                    db.updatePlan(bundle.getInt("id"), text);
                } else {
                    TodoModel plan = new TodoModel();
                    plan.setPlan(text);
                    plan.setStatus(0);
                    db.insertPlan(plan);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }
}
