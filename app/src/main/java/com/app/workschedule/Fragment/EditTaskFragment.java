//package com.app.workschedule.Fragment;
//
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CompoundButton;
//import android.widget.TextView;
//
//import com.app.workschedule.R;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.checkbox.MaterialCheckBox;
//import com.google.android.material.chip.ChipGroup;
//import com.google.android.material.textfield.TextInputEditText;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class EditTaskFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
//
//
//    TextInputEditText textInputEditTextDate;
//    TextInputEditText textInputEditTextTime;
//    TextInputEditText textInputEditTextInstructions;
//    MaterialCheckBox checkBoxRepeatDaily;
//    MaterialCheckBox checkBoxAllowSms;
//    ChipGroup chipGroupRepeatTask;
//    TextView textViewAddAttachement;
//    RecyclerView recyclerViewAttachment;
//    MaterialButton btnAdd;
//
//    public EditTaskFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_edit_task, container, false);
//
//        textInputEditTextDate = view.findViewById(R.id.edit_text_date);
//        textInputEditTextTime = view.findViewById(R.id.edit_text_time);
//        textInputEditTextInstructions = view.findViewById(R.id.edit_text_instructions);
//        checkBoxRepeatDaily = view.findViewById(R.id.checkbox_repeat_daily);
//        chipGroupRepeatTask = view.findViewById(R.id.chip_group_repeat);
//        textViewAddAttachement = view.findViewById(R.id.text_view_attachment);
//        recyclerViewAttachment = view.findViewById(R.id.recycler_view_attachment);
//        btnAdd = view.findViewById(R.id.btn_add);
//        checkBoxAllowSms = view.findViewById(R.id.checkbox_allow_msg);
//
//        // putting ids to list
////        chipRepeatTaskIds.add(R.id.chip_sun);
////        chipRepeatTaskIds.add(R.id.chip_mon);
////        chipRepeatTaskIds.add(R.id.chip_tue);
////        chipRepeatTaskIds.add(R.id.chip_wed);
////        chipRepeatTaskIds.add(R.id.chip_thu);
////        chipRepeatTaskIds.add(R.id.chip_fri);
////        chipRepeatTaskIds.add(R.id.chip_sat);
//
//
//        textInputEditTextDate.setOnClickListener(this);
//        textInputEditTextTime.setOnClickListener(this);
//        textViewAddAttachement.setOnClickListener(this);
//        checkBoxRepeatDaily.setOnCheckedChangeListener(this);
//        btnAdd.setOnClickListener(this);
//
//
//        textInputEditTextInstructions.clearFocus();
//
//
//        return view;
//    }
//
//    @Override
//    public void onClick(View view) {
//
//    }
//}
