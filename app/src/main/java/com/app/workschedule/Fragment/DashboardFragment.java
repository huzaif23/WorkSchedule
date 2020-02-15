package com.app.workschedule.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.workschedule.Activity.MainActivity;
import com.app.workschedule.Adapter.TaskDetailsAdapter;
import com.app.workschedule.DB.DBHelper;
import com.app.workschedule.DB.DataAccessHelper;
import com.app.workschedule.Model.ResponseFromServerAttachments;
import com.app.workschedule.Model.ResponseFromServerTaskDetails;
import com.app.workschedule.Model.ResponseFromServerTasks;
import com.app.workschedule.Model.TaskDetailsModel;
import com.app.workschedule.R;
import com.app.workschedule.Retrofit.WebService;
import com.app.workschedule.Retrofit.WebServiceFactory;
import com.app.workschedule.Utils.Constants;
import com.app.workschedule.Utils.CustomProgressBar;
import com.app.workschedule.Utils.DateFormatter;
import com.app.workschedule.Utils.FileUtils;
import com.app.workschedule.Utils.FragmentTransactionHelperClass;
import com.app.workschedule.Utils.SharedPreferencHelperClass;
import com.app.workschedule.Utils.SpaceItemDecoration;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    LinearLayout emptyContainer;
    RecyclerView recyclerView;
    ExtendedFloatingActionButton fab;
    TextView textViewTotalTask;
    TextView textViewTaskInProgress;
    TextView textViewTaskCompleted;
    TextView textViewUserName;
    SwipeRefreshLayout refreshLayout;
    ImageView imageViewLogOut;

    DataAccessHelper dataAccessHelper;
    TaskDetailsAdapter adapter;
    FileUtils fileUtils;
    AlertDialog alertDialog;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.rv_task);
        emptyContainer = view.findViewById(R.id.empty_container);
        textViewTotalTask = view.findViewById(R.id.text_view_total_task);
        textViewTaskInProgress = view.findViewById(R.id.text_view_task_in_progress);
        textViewTaskCompleted = view.findViewById(R.id.text_view_task_completed);
        textViewUserName = view.findViewById(R.id.text_view_user_name);
        imageViewLogOut = view.findViewById(R.id.image_view_log_out);

        fab = view.findViewById(R.id.fab_add_task);
        refreshLayout = view.findViewById(R.id.pullToRefresh);

        Toast.makeText(getContext(), "Swipe down to refresh task list", Toast.LENGTH_SHORT).show();



        fileUtils = FileUtils.getInstance(getContext());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.shrink();
                } else {
                    fab.extend();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });

        if (getActivity() != null)
            ((MainActivity) getActivity()).toggleToolbar(false, "");


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_view_margin)));

        if (SharedPreferencHelperClass.getInstance(getContext()).getUserType().equals(Constants.USER_TYPE_EMPLOYER))
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.GONE);

        fab.setOnClickListener(this);
        imageViewLogOut.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);

        fetchTasksFromServer();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataAccessHelper = DataAccessHelper.getInstance(getContext());
        textViewUserName.setText(SharedPreferencHelperClass.getInstance(getContext()).getUserName().toUpperCase());

        List<TaskDetailsModel> taskDetailsModels = dataAccessHelper.getTaskDetails();
        if (taskDetailsModels.size() > 0) {
            initRecyclerView(taskDetailsModels);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView(List<TaskDetailsModel> taskDetailsModels) {
        adapter = new TaskDetailsAdapter(getContext(), getActivity());
        adapter.clearData();
        adapter.addData(taskDetailsModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        emptyContainer.setVisibility(View.GONE);
        setTaskDetails(taskDetailsModels);
    }

    private void fetchTasksFromServer() {
        CustomProgressBar customProgressBar = CustomProgressBar.getInstance(getContext());
        customProgressBar.addActivity(getContext());
        customProgressBar.showDialog();
        SharedPreferencHelperClass sharedPreferencHelperClass = SharedPreferencHelperClass.getInstance(getContext());
        WebService webService = WebServiceFactory.getInstance();
        webService.getTasksFromServer(sharedPreferencHelperClass.getUserId(), sharedPreferencHelperClass.getUserType()).enqueue(new Callback<ResponseFromServerTasks>() {
            @Override
            public void onResponse(Call<ResponseFromServerTasks> call, Response<ResponseFromServerTasks> response) {
                customProgressBar.dismissDialog();
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        for (ResponseFromServerTaskDetails x : response.body().getTasks()) {
                            SQLiteDatabase db = dataAccessHelper.getDb();
                            db.beginTransaction();
                            try {
                                int id = dataAccessHelper.checkIfTaskAlreadyExists(x.getId());

                                if (id == 0) {
                                    id = dataAccessHelper.insertTask(
                                            x.getMobDate()
                                            , x.getMobTime()
                                            , x.getInstruction()
                                            , Integer.parseInt(x.getNotify())
                                            , Integer.parseInt(x.getStatus())
                                            , x.getDaysRepeat()
                                            , x.getId()
                                    );
                                } else {
                                    dataAccessHelper.updateTaskDetails(id
                                            , x.getMobDate()
                                            , x.getMobTime()
                                            , x.getInstruction()
                                            , Integer.parseInt(x.getNotify())
                                            , Integer.parseInt(x.getStatus())
                                            , x.getDaysRepeat()
                                    );
                                    dataAccessHelper.delteAttachmentsById(id);
                                }
                                for (ResponseFromServerAttachments attachments : x.getAttachments()) {
                                    int finalId = id;
                                    webService.downloadFile(Constants.BASE_URL + "/" + attachments.getFilePath()).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            String fileName = "";
                                            if (attachments.getFileType().equalsIgnoreCase(Constants.ATTACHMENT_TYPE_AUDIO)) {
                                                fileName = fileUtils.createAudioDirectory() + File.separator + DateFormatter.getInstance().getTimeStampForFileName() + ".mp3";
                                            } else {
                                                fileName = fileUtils.createPictureDirectory() + File.separator + DateFormatter.getInstance().getTimeStampForFileName() + ".jpg";

                                            }

                                            File file = new File(fileName);
                                            FileOutputStream fileOutputStream = null;
                                            try {
                                                fileOutputStream = new FileOutputStream(file);
                                                IOUtils.write(response.body().bytes(), fileOutputStream);
                                                dataAccessHelper.insertAttachment(
                                                        file.getPath()
                                                        , attachments.getFileType()
                                                        , finalId
                                                );
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.d("test", t.getMessage());
                                            customProgressBar.dismissDialog();
                                        }
                                    });
                                }


                                initRecyclerView(dataAccessHelper.getTaskDetails());
                                Snackbar.make(getView(), "Tasks downloaded successfully", Snackbar.LENGTH_SHORT).show();
                                db.setTransactionSuccessful();
                            } catch (Exception e) {
                                customProgressBar.dismissDialog();
                                e.printStackTrace();
                            } finally {
                                db.endTransaction();
                            }
                        }
                    } else {
                        customProgressBar.dismissDialog();
                        Snackbar.make(getView(), "Server error occurred", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    customProgressBar.dismissDialog();
                    Snackbar.make(getView(), "Please check your internet connection", Snackbar.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ResponseFromServerTasks> call, Throwable t) {
                customProgressBar.dismissDialog();
                Snackbar.make(getView(), "Error occurred while fetching data", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setTaskDetails(List<TaskDetailsModel> taskDetailsModels) {
        int taskInProgrssCount = 0;
        int taskCompletedCount = 0;
        for (TaskDetailsModel x : taskDetailsModels) {
            if (x.getStatus() == 0)
                taskInProgrssCount++;
            else
                taskCompletedCount++;
        }
        textViewTotalTask.setText(getString(R.string.total_tasks_text, String.valueOf(taskDetailsModels.size())));
        textViewTaskInProgress.setText(getString(R.string.in_progress_tasks_text, String.valueOf(taskInProgrssCount)));
        textViewTaskCompleted.setText(getString(R.string.completed_tasks_text, String.valueOf(taskCompletedCount)));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab_add_task:
                FragmentTransactionHelperClass.getInstance().changeFragment(new AddTaskFragment(), getContext(), Constants.ADD_TASK_FRAGMENT, true);
                if (getActivity() != null)
                    ((MainActivity) getActivity()).toggleToolbar(true, "Add New Task");
                break;
            case R.id.image_view_log_out:
                createDialog();
                break;
        }
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_MaterialComponents_Light_Dialog);
        builder.setTitle("Confirmation");
        builder.setMessage("Do you want to log out?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                SharedPreferencHelperClass.getInstance(getContext()).setIsUserLoggedIn(false);
                getActivity().finishAndRemoveTask();
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

    @Override
    public void onRefresh() {
        fetchTasksFromServer();
        refreshLayout.setRefreshing(false);
    }
}
