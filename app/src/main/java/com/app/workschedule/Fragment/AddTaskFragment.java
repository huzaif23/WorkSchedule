package com.app.workschedule.Fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.workschedule.Activity.MainActivity;
import com.app.workschedule.Adapter.AttachmentAdapter;
import com.app.workschedule.DB.DataAccessHelper;
import com.app.workschedule.Interface.DeleteItem;
import com.app.workschedule.Interface.PopulateAttachmentList;
import com.app.workschedule.Model.TaskDetailsModel;
import com.app.workschedule.R;
import com.app.workschedule.Retrofit.WebService;
import com.app.workschedule.Retrofit.WebServiceFactory;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.CustomProgressBar;
import com.app.workschedule.Utils.DateFormatter;
import com.app.workschedule.Utils.FileUtils;
import com.app.workschedule.Utils.SharedPreferencHelperClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTaskFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener, PopulateAttachmentList, DeleteItem {

    TextInputEditText textInputEditTextDate;
    TextInputEditText textInputEditTextTime;
    TextInputEditText textInputEditTextInstructions;
    MaterialCheckBox checkBoxRepeatDaily;
    MaterialCheckBox checkBoxAllowSms;
    ChipGroup chipGroupRepeatTask;
    TextView textViewAddAttachement;
    RecyclerView recyclerViewAttachment;
    MaterialButton btnAdd;
    View view;


    List<Integer> chipRepeatTaskIds = new ArrayList<>();
    Map<String, String> attachments;
    AttachmentAdapter attachmentAdapter;
    DataAccessHelper dataAccessHelper;
    String selectedTime;
    String selectedDate;
    WebService webService;
    CustomProgressBar customProgressBar;
    int taskId = 0;
    private boolean shouldOpenInEditMode = false;
    String serverTaskId;


    public AddTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null) {
            taskId = args.getInt(Constants.TASK_ID_KEY);
            shouldOpenInEditMode = args.getBoolean(Constants.EDIT_MODE_TAG);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);

        textInputEditTextDate = view.findViewById(R.id.edit_text_date);
        textInputEditTextTime = view.findViewById(R.id.edit_text_time);
        textInputEditTextInstructions = view.findViewById(R.id.edit_text_instructions);
        checkBoxRepeatDaily = view.findViewById(R.id.checkbox_repeat_daily);
        chipGroupRepeatTask = view.findViewById(R.id.chip_group_repeat);
        textViewAddAttachement = view.findViewById(R.id.text_view_attachment);
        recyclerViewAttachment = view.findViewById(R.id.recycler_view_attachment);
        btnAdd = view.findViewById(R.id.btn_add);
        checkBoxAllowSms = view.findViewById(R.id.checkbox_allow_msg);

        // putting ids to list
        chipRepeatTaskIds.add(R.id.chip_sun);
        chipRepeatTaskIds.add(R.id.chip_mon);
        chipRepeatTaskIds.add(R.id.chip_tue);
        chipRepeatTaskIds.add(R.id.chip_wed);
        chipRepeatTaskIds.add(R.id.chip_thu);
        chipRepeatTaskIds.add(R.id.chip_fri);
        chipRepeatTaskIds.add(R.id.chip_sat);


        textInputEditTextDate.setOnClickListener(this);
        textInputEditTextTime.setOnClickListener(this);
        textViewAddAttachement.setOnClickListener(this);
        checkBoxRepeatDaily.setOnCheckedChangeListener(this);
        btnAdd.setOnClickListener(this);


        textInputEditTextInstructions.clearFocus();

        webService = WebServiceFactory.getInstance();
        dataAccessHelper = DataAccessHelper.getInstance(getContext());

        // setting attachment recycler view
        recyclerViewAttachment.setLayoutManager(new LinearLayoutManager(getContext()));
        attachmentAdapter = new AttachmentAdapter(this, getContext(), this);
        recyclerViewAttachment.setAdapter(attachmentAdapter);

        attachments = new HashMap<>();
        customProgressBar = CustomProgressBar.getInstance(getContext());
        this.view = view;


        if (shouldOpenInEditMode && taskId != 0) {
            TaskDetailsModel taskDetailsModel = dataAccessHelper.getTaskDetailsById(taskId);
            if (taskDetailsModel != null)
                setTaskData(taskDetailsModel);
        }

        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        selectedDate = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
        textInputEditTextDate.setText(DateFormatter.getInstance().formatDateWithDay(selectedDate));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hour = String.format("%02d", hourOfDay);
        String minutes = String.format("%02d", minute);
        selectedTime = hour + ":" + minutes;
        textInputEditTextTime.setText(DateFormatter.getInstance().formatTimeTo12Hr(selectedTime));
    }

    private void setTaskData(TaskDetailsModel taskData) {

        selectedDate = taskData.getDate();
        selectedTime = taskData.getTime();
        serverTaskId = taskData.getServerId();

        List<String> attachmentsById = dataAccessHelper.getAttachmentsById(taskId);

        DateFormatter dateFormatter = DateFormatter.getInstance();
        textInputEditTextDate.setText(dateFormatter.formatDateWithDay(taskData.getDate()));
        textInputEditTextTime.setText(dateFormatter.formatTimeTo12Hr(taskData.getTime()));
        textInputEditTextInstructions.setText(taskData.getInstruction());
        checkBoxAllowSms.setChecked(taskData.getShouldAllowSms() == 1);

        if (taskData.getRepeatDays().equalsIgnoreCase(Constants.REPEAT_EVERYDAY)) {
            checkBoxRepeatDaily.setChecked(true);
            markAllDays();
        } else {
            String[] days = taskData.getRepeatDays().split(",");
            for (String day : days) {
                for (Integer id : chipRepeatTaskIds) {
                    Chip chip = view.findViewById(id);
                    if (chip.getText().toString().equalsIgnoreCase(day))
                        chip.setChecked(true);
                }
            }
        }

        for (String x : attachmentsById) {
            attachments.put(x, x);
        }

        attachmentAdapter.addData(attachmentsById);
        attachmentAdapter.notifyDataSetChanged();
        recyclerViewAttachment.setVisibility(View.VISIBLE);

        btnAdd.setText("Edit Task");

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.edit_text_date:
                showDatePickerDialog();
                break;
            case R.id.edit_text_time:
                showTimePickerDialog();
                break;
            case R.id.text_view_attachment:
                showAttachementBottomSheetDialog();
                break;
            case R.id.btn_add:
                if (!textInputEditTextInstructions.getText().toString().isEmpty())
                    if (!textInputEditTextTime.getText().toString().isEmpty())
                        if (!textInputEditTextDate.getText().toString().isEmpty())
                            if (shouldOpenInEditMode && taskId != 0)
                                editTask();
                            else
                                addTask();
                        else
                            Toast.makeText(getContext(), "Please enter date", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Please enter time", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Please enter instructions", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setOkColor(getResources().getColor(R.color.text_color_primary));
        datePickerDialog.setCancelColor(getResources().getColor(R.color.text_color_primary));
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this
                , false
        );
        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
        timePickerDialog.setVersion(TimePickerDialog.Version.VERSION_2);
        timePickerDialog.setOkColor(getResources().getColor(R.color.text_color_primary));
        timePickerDialog.setCancelColor(getResources().getColor(R.color.text_color_primary));
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
        int id = compoundButton.getId();
        switch (id) {
            case R.id.checkbox_repeat_daily:
                if (check)
                    markAllDays();
                else
                    enableChips();
        }
    }

    private void markAllDays() {
        for (Integer id : chipRepeatTaskIds) {
            chipGroupRepeatTask.check(id);
            view.findViewById(id).setEnabled(false);
        }
    }

    private void enableChips() {
        chipGroupRepeatTask.clearCheck();
        for (Integer id : chipRepeatTaskIds) {
            view.findViewById(id).setEnabled(true);
        }
    }

    private void showAttachementBottomSheetDialog() {
        AttachmentBottomSheetDialog attachmentBottomSheetDialog = new AttachmentBottomSheetDialog(this);
        attachmentBottomSheetDialog.show(getFragmentManager(), "Attachment");
    }

    @Override
    public void populateRecyclerView(String attachmentName, String filePath) {
        attachments.put(attachmentName, filePath);
        attachmentAdapter.clearData();
        attachmentAdapter.addData(attachments.values());
        attachmentAdapter.notifyDataSetChanged();
        recyclerViewAttachment.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteItem(String item) {
        attachments.remove(item);
        Toast.makeText(getContext(), "Attachment removed successfully", Toast.LENGTH_SHORT).show();

    }

    private void addTask() {
        addTaskToServer();
    }

    private void editTask() {
        customProgressBar.addActivity(getContext());
        customProgressBar.showDialog();
        String pictures = createPictureArray();
        String days = "";
        if (checkBoxRepeatDaily.isChecked())
            days = Constants.REPEAT_EVERYDAY;
        else {
            List<Integer> checkedIds = chipGroupRepeatTask.getCheckedChipIds();
            for (Integer x : checkedIds) {
                Chip chip = view.findViewById(x);
                if (x == checkedIds.size() - 1) {
                    days = days.concat(chip.getText().toString());
                } else {
                    days = days.concat(chip.getText().toString() + ",");

                }
            }
        }

        // create multipart
        MultipartBody.Part part = createMultiPart();

        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),
                selectedDate
        );
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"),
                selectedTime
        );
        RequestBody instruction = RequestBody.create(MediaType.parse("text/plain"),
                textInputEditTextInstructions.getText().toString()
        );

        RequestBody repeat = RequestBody.create(MediaType.parse("text/plain"),
                days
        );

        RequestBody picture = RequestBody.create(MediaType.parse("text/plain"),
                pictures
        );
        RequestBody notify = RequestBody.create(MediaType.parse("text/plain"),
                checkBoxAllowSms.isChecked() ? "1" : "0"
        );


        webService.editTask(
                date
                , time
                , instruction
                , repeat
                , picture
                , notify
                , Integer.parseInt(serverTaskId)
                , part
        ).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                customProgressBar.dismissDialog();
                if (response.isSuccessful()) {
                    Snackbar.make(view, "Successfully edited task", Snackbar.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Snackbar.make(view, "Error occurred", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                customProgressBar.dismissDialog();
                Snackbar.make(view, "Error occurred", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private void addTaskToServer() {
        customProgressBar.addActivity(getContext());
        customProgressBar.showDialog();
        SharedPreferencHelperClass sharedPreferencHelperClass = SharedPreferencHelperClass.getInstance(getContext());
        String pictures = createPictureArray();
        String days = "";
        if (checkBoxRepeatDaily.isChecked())
            days = Constants.REPEAT_EVERYDAY;
        else {
            List<Integer> checkedIds = chipGroupRepeatTask.getCheckedChipIds();
            for (Integer x : checkedIds) {
                Chip chip = view.findViewById(x);
                if (x == checkedIds.size() - 1) {
                    days = days.concat(chip.getText().toString());
                } else {
                    days = days.concat(chip.getText().toString() + ",");

                }
            }
        }

        // create multipart
        MultipartBody.Part part = createMultiPart();

        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),
                selectedDate
        );
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"),
                selectedTime
        );
        RequestBody instruction = RequestBody.create(MediaType.parse("text/plain"),
                textInputEditTextInstructions.getText().toString()
        );

        RequestBody repeat = RequestBody.create(MediaType.parse("text/plain"),
                days
        );

        RequestBody picture = RequestBody.create(MediaType.parse("text/plain"),
                pictures
        );
        RequestBody notify = RequestBody.create(MediaType.parse("text/plain"),
                checkBoxAllowSms.isChecked() ? "1" : "0"
        );

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),
                sharedPreferencHelperClass.getUserId()

        );

        RequestBody assignedId = RequestBody.create(MediaType.parse("text/plain"),
                sharedPreferencHelperClass.getHelperId()
        );

        webService.addTask(
                userId
                , assignedId
                , date
                , time
                , instruction
                , repeat
                , picture
                , notify
                , part
        ).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                customProgressBar.dismissDialog();
                if (response.isSuccessful()) {
                    Snackbar.make(view, "Successfully added task", Snackbar.LENGTH_LONG).show();
                    resetFields();
                    getActivity().onBackPressed();

                } else {
                    Snackbar.make(view, "Server error occurred", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                customProgressBar.dismissDialog();
                Log.d("test", "" + t.getMessage());
                Snackbar.make(view, "Error occurred while uploading data", Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private void resetFields() {
        textInputEditTextDate.setText("");
        textInputEditTextTime.setText("");
        textInputEditTextInstructions.setText("");
        attachments.clear();
        attachmentAdapter.clearData();
        attachmentAdapter.notifyDataSetChanged();
        checkBoxRepeatDaily.setChecked(false);
        checkBoxAllowSms.setChecked(false);
        for (Integer x : chipRepeatTaskIds) {
            Chip chip = view.findViewById(x);
            chip.setChecked(false);
        }
    }

    private String createPictureArray() {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, String> entry : attachments.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.contains("jpg")) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", Constants.ATTACHMENT_TYPE_PICTURE);
                    jsonObject.put("file", FileUtils.getInstance(getContext()).convertImageToBase64(value));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray.toString();
    }

    private MultipartBody.Part createMultiPart() {


        MultipartBody.Part multipartBody = null;

        for (Map.Entry<String, String> entry : attachments.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.contains("mp3")) {
                File file = new File(value);
                if (file.exists()) {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), file);
                    multipartBody = MultipartBody.Part.createFormData("audio_files", file.getName(), requestFile);
                }
            }
        }
        if (multipartBody == null)
            multipartBody = MultipartBody.Part.createFormData("audio_files", "");
        return multipartBody;
    }


}
