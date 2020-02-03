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

}
