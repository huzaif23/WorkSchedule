package com.app.workschedule.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.FormUrlEncoded;

public class ResponseFromServerSignup {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("helper_id")
    @Expose
    private String helperId;

    public boolean isStatus() {
        return status;
    }

    public String getHelperId() {
        return helperId;
    }
}
