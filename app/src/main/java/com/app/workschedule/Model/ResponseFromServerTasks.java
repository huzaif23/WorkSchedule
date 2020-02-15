package com.app.workschedule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFromServerTasks {


    @SerializedName("tasks")
    @Expose
    private List<ResponseFromServerTaskDetails> tasks = null;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public List<ResponseFromServerTaskDetails> getTasks() {
        return tasks;
    }

    public void setTasks(List<ResponseFromServerTaskDetails> tasks) {
        this.tasks = tasks;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


}
