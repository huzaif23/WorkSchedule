package com.app.workschedule.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.workschedule.Adapter.AttachmentAdapter;
import com.app.workschedule.DB.DataAccessHelper;
import com.app.workschedule.Model.TaskDetailsModel;
import com.app.workschedule.R;
import com.app.workschedule.Retrofit.WebService;
import com.app.workschedule.Retrofit.WebServiceFactory;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.CustomProgressBar;
import com.app.workschedule.Utils.DateFormatter;
import com.app.workschedule.Utils.SharedPreferencHelperClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailsFragment extends Fragment implements View.OnClickListener {


    int taskId;
    DataAccessHelper dataAccessHelper;
    TaskDetailsModel taskDetailsModel;

    TextView textViewDate;
    TextView textViewTime;
    TextView textViewInstruction;
    TextView textViewRepeatTask;
    ChipGroup chipGroupRepeat;
    RecyclerView recyclerViewAttachments;
    MaterialButton btnMarkComplete;
    AlertDialog alertDialog;
    CustomProgressBar customProgressBar;

    private String[] permissions = {
            Manifest.permission.SEND_SMS
    };

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null) {
            taskId = args.getInt(Constants.TASK_ID_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);

        textViewDate = view.findViewById(R.id.text_view_date);
        textViewTime = view.findViewById(R.id.text_view_time);
        textViewInstruction = view.findViewById(R.id.text_view_instruction);
        chipGroupRepeat = view.findViewById(R.id.chip_group_repeat);
        recyclerViewAttachments = view.findViewById(R.id.rv_attachments);
        btnMarkComplete = view.findViewById(R.id.btn_mark_complete);

        dataAccessHelper = DataAccessHelper.getInstance(getContext());
        customProgressBar = CustomProgressBar.getInstance(getContext());

        btnMarkComplete.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (SharedPreferencHelperClass.getInstance(getContext()).getUserType().equals(Constants.USER_TYPE_DOMESTIC_HELPER))
            btnMarkComplete.setVisibility(View.VISIBLE);
        else
            btnMarkComplete.setVisibility(View.GONE);
        if (taskId != 0) {
            taskDetailsModel = dataAccessHelper.getTaskDetailsById(taskId);
            if (taskDetailsModel != null)
                setData();
        }
    }

    private void setData() {
        textViewDate.setText(DateFormatter.getInstance().formatDateWithDay(taskDetailsModel.getDate()));
        textViewTime.setText(DateFormatter.getInstance().formatTimeTo12Hr(taskDetailsModel.getTime()));
        textViewInstruction.setText(taskDetailsModel.getInstruction());
        addChips();
        showAttachments();
    }

    private void showAttachments() {
        List<String> attachments = dataAccessHelper.getAttachmentsById(taskId);
        if (attachments.size() > 0) {
            AttachmentAdapter attachmentAdapter = new AttachmentAdapter(null, getContext(),this);
            attachmentAdapter.addData(attachments);
            recyclerViewAttachments.setAdapter(attachmentAdapter);
            recyclerViewAttachments.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void addChips() {
        String[] chipsData = taskDetailsModel.getRepeatDays().split(",");
        for (String x : chipsData) {
            Chip chip = new Chip(getContext());
            chip.setText(x);
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(),
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Filter);
            chip.setChipDrawable(chipDrawable);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)));
            chip.setChecked(true);
            chip.setCheckable(false);
            chip.setTextColor(getResources().getColor(R.color.text_color_primary));
            chipGroupRepeat.addView(chip);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_mark_complete) {
            if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
                return;
            }
            createDialog();
        }

    }


    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_MaterialComponents_Light_Dialog);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to mark this task completed?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                markVisitComplete();
                if(taskDetailsModel.getShouldAllowSms() == 1)
                    sendSms();
            }
        });

        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }

    private void sendSms() {
        try {
            String phoneNo = SharedPreferencHelperClass.getInstance(getContext()).getMobile();
            String msg = "Task assigned on "
                    + DateFormatter.getInstance().formatDateWithDay(taskDetailsModel.getDate())
                    + " has been completed";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void markVisitComplete() {
        customProgressBar.addActivity(getContext());
        customProgressBar.showDialog();
        WebService webService = WebServiceFactory.getInstance();
        webService.markComplete(taskDetailsModel.getServerId(), "1").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                customProgressBar.dismissDialog();
                if (response.isSuccessful()) {
                    Snackbar.make(getView(), "Status has been changed", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getView(), "Error occurred", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("test", "" + t.getMessage());
                customProgressBar.dismissDialog();
                Snackbar.make(getView(), "Error occurred", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
