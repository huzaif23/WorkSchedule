package com.app.workschedule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFromServerAttachments {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("task_id")
    @Expose
    private String taskId;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("file_type")
    @Expose
    private String fileType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


}
