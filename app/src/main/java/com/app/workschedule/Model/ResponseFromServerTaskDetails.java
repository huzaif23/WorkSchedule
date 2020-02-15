package com.app.workschedule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseFromServerTaskDetails {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("assigned_user")
        @Expose
        private String assignedUser;
        @SerializedName("mob_date")
        @Expose
        private String mobDate;
        @SerializedName("mob_time")
        @Expose
        private String mobTime;
        @SerializedName("instruction")
        @Expose
        private String instruction;
        @SerializedName("timestamp")
        @Expose
        private String timestamp;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("notify")
        @Expose
        private String notify;
        @SerializedName("days_repeat")
        @Expose
        private String daysRepeat;
        @SerializedName("attachments")
        @Expose
        private List<ResponseFromServerAttachments> attachments = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAssignedUser() {
            return assignedUser;
        }

        public void setAssignedUser(String assignedUser) {
            this.assignedUser = assignedUser;
        }

        public String getMobDate() {
            return mobDate;
        }

        public void setMobDate(String mobDate) {
            this.mobDate = mobDate;
        }

        public String getMobTime() {
            return mobTime;
        }

        public void setMobTime(String mobTime) {
            this.mobTime = mobTime;
        }

        public String getInstruction() {
            return instruction;
        }

        public void setInstruction(String instruction) {
            this.instruction = instruction;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getNotify() {
            return notify;
        }

        public void setNotify(String notify) {
            this.notify = notify;
        }

        public String getDaysRepeat() {
            return daysRepeat;
        }

        public void setDaysRepeat(String daysRepeat) {
            this.daysRepeat = daysRepeat;
        }

        public List<ResponseFromServerAttachments> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<ResponseFromServerAttachments> attachments) {
            this.attachments = attachments;
        }

    }

