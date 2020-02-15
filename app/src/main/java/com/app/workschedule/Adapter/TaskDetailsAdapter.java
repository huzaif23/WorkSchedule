package com.app.workschedule.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.workschedule.Activity.MainActivity;
import com.app.workschedule.Fragment.TaskDetailsFragment;
import com.app.workschedule.Model.TaskDetailsModel;
import com.app.workschedule.R;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.DateFormatter;
import com.app.workschedule.Utils.FragmentTransactionHelperClass;
import com.app.workschedule.Utils.SharedPreferencHelperClass;

import java.util.ArrayList;

import java.util.List;

public class TaskDetailsAdapter extends RecyclerView.Adapter<TaskDetailsAdapter.TaskDetailsViewHolder> {

    List<TaskDetailsModel> taskDetailsModels;
    Context context;
    Activity activity;

    public TaskDetailsAdapter(Context context, Activity activity) {
        taskDetailsModels = new ArrayList<>();
        this.context = context;
        this.activity = activity;
    }

    public void addData(List<TaskDetailsModel> taskDetailsModels) {
        this.taskDetailsModels = taskDetailsModels;
    }

    public void clearData() {
        this.taskDetailsModels.clear();
    }

    @NonNull
    @Override
    public TaskDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskDetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_task_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskDetailsViewHolder holder, int position) {

        if(SharedPreferencHelperClass.getInstance(context).getUserType().equals(Constants.USER_TYPE_EMPLOYER))
//            showPopup(holder.itemView,position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) activity).toggleToolbar(true, "Task Details");
                Fragment fragment = new TaskDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.TASK_ID_KEY, taskDetailsModels.get(position).getId());
                fragment.setArguments(bundle);
                FragmentTransactionHelperClass.getInstance().changeFragment(fragment, context, Constants.TASK_DETAILS_FRAGMENT, true);
            }
        });
        holder.textViewTitle.setText("Task # " + (position + 1));
        holder.textViewTime.setText(DateFormatter.getInstance().formatTimeTo12Hr(taskDetailsModels.get(position).getTime()));

        String days = taskDetailsModels.get(position).getRepeatDays();
        days = days.replaceAll(",$", "");
        final SpannableStringBuilder sb = new SpannableStringBuilder(days);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, days.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        holder.textViewRepeatDays.setText("Repeat: ");
        holder.textViewRepeatDays.append(sb);

        holder.textViewDate.setText(DateFormatter.getInstance().formatDateWithDay(taskDetailsModels.get(position).getDate()));

        String instructions = taskDetailsModels.get(position).getInstruction();
        final SpannableStringBuilder sbInstructions = new SpannableStringBuilder(instructions);
        sbInstructions.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, instructions.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (taskDetailsModels.get(position).getStatus() == 0) {
            holder.textViewStatus.setText("In Progress");
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.task_in_progress));
            holder.textViewStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.in_progress_ic, 0, 0, 0);
        } else {
            holder.textViewStatus.setText("Completed");
            holder.textViewStatus.setTextColor(context.getResources().getColor(R.color.task_completed));
            holder.textViewStatusLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.completed_ic, 0, 0, 0);
        }
    }

    private void showPopup(View view, final int position) {
        // pass the imageview id
        View menuItemView = view.findViewById(R.id.image_view_menu);
        PopupMenu popup = new PopupMenu(activity, menuItemView);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.menu_task, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        // do what you need.
                        break;
                    default:
                        return false;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return taskDetailsModels.size();
    }

    protected class TaskDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewDate, textViewTime, textViewStatus, textViewStatusLabel, textViewRepeatDays;

        public TaskDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewStatus = itemView.findViewById(R.id.text_view_status);
            textViewStatusLabel = itemView.findViewById(R.id.text_view_status_label);
            textViewRepeatDays = itemView.findViewById(R.id.text_view_repeat_task_days);
        }
    }

}
