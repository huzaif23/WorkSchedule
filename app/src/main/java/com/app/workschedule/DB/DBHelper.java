package com.app.workschedule.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    String createUserTable = "CREATE TABLE IF NOT EXISTS "+DBConstants.TABLE_USER+"("
            +DBConstants.USER_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            +DBConstants.USER_NAME+" TEXT, "
            +DBConstants.ROLE_ID+" INTEGER NOT NULL)";

    String createTableTaskDetails = "CREATE TABLE IF NOT EXISTS "+DBConstants.TABLE_TASKS_DETAILS+"("
            +DBConstants.TASK_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            +DBConstants.TASK_SERVER_ID+" TEXT NOT NULL, "
            +DBConstants.TASK_DATE+" TEXT NOT NULL, "
            +DBConstants.TASK_TIME+" TEXT NOT NULL, "
            +DBConstants.TASK_INSTRUCTIONS+" TEXT NOT NULL, "
            +DBConstants.TASK_SHOULD_ALLOW_SMS+" INTEGER NOT NULL, "
            +DBConstants.TASK_REPEAT+" TEXT NOT NULL, "
            +DBConstants.TASK_STATUS+" INTEGER NOT NULL)";

    String createTableAttachments = "CREATE TABLE IF NOT EXISTS "+DBConstants.TABLE_ATTACHMENTS+"("
            +DBConstants.ATTACHMENT_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            +DBConstants.ATTACHMENT_PATH+" TEXT NOT NULL, "
            +DBConstants.ATTACHMENT_TYPE+" TEXT NOT NULL, "
            +DBConstants.ATTACHMENT_TASK_FK+" INTEGER NOT NULL, "
            +" FOREIGN KEY("+DBConstants.ATTACHMENT_TASK_FK+")"
            +" REFERENCES "+DBConstants.TABLE_TASKS_DETAILS+"("+DBConstants.TASK_ID+"))";


    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.execSQL(createUserTable);
            sqLiteDatabase.execSQL(createTableTaskDetails);
            sqLiteDatabase.execSQL(createTableAttachments);

        }
        catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
