package com.app.workschedule.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataAccessHelper {

    private static DataAccessHelper instance;
    private SQLiteDatabase db;

    private DataAccessHelper(Context context) {
        DBHelper dbHelper = new DBHelper(context, DBConstants.DB_NAME, null, DBConstants.DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized DataAccessHelper getInstance(Context context) {
        if (instance == null)
            instance = new DataAccessHelper(context);
        return instance;
    }


}
