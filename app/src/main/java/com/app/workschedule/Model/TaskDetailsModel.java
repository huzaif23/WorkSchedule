package com.app.workschedule.Model;

import java.util.List;

public class TaskDetailsModel {

    private int id;
    private String instruction;
    private String date;
    private String time;
    private int shouldAllowSms;
    private int status;
    private String repeatDays;
    private String serverId;
    private List<String> attachments;

    public TaskDetailsModel(int id, String instruction, String date, String time, int shouldAllowSms, int status, List<String> attachments,String repeatDays,String serverId) {
        this.id = id;
        this.instruction = instruction;
        this.date = date;
        this.time = time;
        this.shouldAllowSms = shouldAllowSms;
        this.status = status;
        this.attachments = attachments;
        this.repeatDays = repeatDays;
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    public int getId() {
        return id;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getShouldAllowSms() {
        return shouldAllowSms;
    }

    public int getStatus() {
        return status;
    }

    public List<String> getAttachments() {
        return attachments;
    }
}
