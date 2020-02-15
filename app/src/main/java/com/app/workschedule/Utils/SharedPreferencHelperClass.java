package com.app.workschedule.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencHelperClass {

    Context context;
    static SharedPreferencHelperClass instance;
    SharedPreferences preferences;

    // Tags
    private static final String SP_USER_LOGGED_IN = "user_logged_in";
    private static final String SP_USER_ID = "user_id";
    private static final String SP_EMAIL = "email";
    private static final String SP_USER_TYPE = "user_type";
    private static final String SP_EMPLOYER_ID = "employer_id";
    private static final String SP_EMPLOYER_MOBILE_NO = "employer_mobile_no";
    private static final String SP_USER_NAME = "user_name";
    private static final String SP_HELPER_ID = "helper_id";

    private SharedPreferencHelperClass(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public static synchronized SharedPreferencHelperClass getInstance(Context context) {
        if(instance == null)
            instance = new SharedPreferencHelperClass(context);
        return instance;
    }

    public void setIsUserLoggedIn(boolean status) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SP_USER_LOGGED_IN, status);
        editor.apply();
    }

    public boolean getIsUserLoggedIn() {
        return preferences.getBoolean(SP_USER_LOGGED_IN, false);
    }


    public void setUserId(String userId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_USER_ID, userId);
        editor.apply();
    }

    public String getUserId() {
        return preferences.getString(SP_USER_ID, "");
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_EMAIL,email);
        editor.apply();
    }

    public String getEmail() {
        return preferences.getString(SP_EMAIL, "");
    }

    public void setUserType(String userType) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_USER_TYPE, userType);
        editor.apply();
    }

    public String getUserType() {
        return preferences.getString(SP_USER_TYPE, "");
    }

    public void setEmployerId(String employerId) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_EMPLOYER_ID, employerId);
        editor.apply();
    }

    public String getEmployerId() {
        return preferences.getString(SP_EMPLOYER_ID, "");
    }

    public void setMobile(String mobile) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_EMPLOYER_MOBILE_NO, mobile);
        editor.apply();
    }

    public String getMobile() {
        return preferences.getString(SP_EMPLOYER_MOBILE_NO, "");
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return preferences.getString(SP_USER_NAME, "");
    }

    public void setHelperId(String id) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SP_HELPER_ID, id);
        editor.apply();
    }

    public String getHelperId() {
        return preferences.getString(SP_HELPER_ID, "");
    }

}
