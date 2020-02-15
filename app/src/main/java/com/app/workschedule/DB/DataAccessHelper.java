package com.app.workschedule.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.app.workschedule.Model.TaskDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public SQLiteDatabase getDb() {
        return db;
    }

    public int insertTask(String date, String time, String instuctions, int shouldAllowMessage, int status, String repeatDays,String serverId) throws SQLiteException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.TASK_DATE, date);
        contentValues.put(DBConstants.TASK_TIME, time);
        contentValues.put(DBConstants.TASK_INSTRUCTIONS, instuctions);
        contentValues.put(DBConstants.TASK_SHOULD_ALLOW_SMS, shouldAllowMessage);
        contentValues.put(DBConstants.TASK_STATUS, status);
        contentValues.put(DBConstants.TASK_REPEAT, repeatDays);
        contentValues.put(DBConstants.TASK_SERVER_ID,serverId);
        return (int) db.insert(DBConstants.TABLE_TASKS_DETAILS, null, contentValues);
    }

    public int insertAttachment(String path, String type, int taskID) throws SQLiteException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.ATTACHMENT_PATH, path);
        contentValues.put(DBConstants.ATTACHMENT_TYPE, type);
        contentValues.put(DBConstants.ATTACHMENT_TASK_FK, taskID);
        return (int) db.insert(DBConstants.TABLE_ATTACHMENTS, null, contentValues);
    }

    public List<TaskDetailsModel> getTaskDetails() {
        String query = "SELECT * FROM " + DBConstants.TABLE_TASKS_DETAILS;
        List<TaskDetailsModel> taskDetailsModels = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                taskDetailsModels.add(new TaskDetailsModel(
                        cursor.getInt(cursor.getColumnIndex(DBConstants.TASK_ID))
                        , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_INSTRUCTIONS))
                        , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_DATE))
                        , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_TIME))
                        , cursor.getInt(cursor.getColumnIndex(DBConstants.TASK_SHOULD_ALLOW_SMS))
                        , cursor.getInt(cursor.getColumnIndex(DBConstants.TASK_STATUS))
                        , null
                        , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_REPEAT))
                        , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_SERVER_ID))
                ));
            }
        }
        cursor.close();
        return taskDetailsModels;
    }

    public TaskDetailsModel getTaskDetailsById(int id) {
        String query = "SELECT * FROM " + DBConstants.TABLE_TASKS_DETAILS
                + " WHERE " + DBConstants.TASK_ID + " = " + "'" + id + "'";
        TaskDetailsModel taskDetailsModels = null;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            taskDetailsModels = new TaskDetailsModel(
                    cursor.getInt(cursor.getColumnIndex(DBConstants.TASK_ID))
                    , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_INSTRUCTIONS))
                    , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_DATE))
                    , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_TIME))
                    , cursor.getInt(cursor.getColumnIndex(DBConstants.TASK_SHOULD_ALLOW_SMS))
                    , cursor.getInt(cursor.getColumnIndex(DBConstants.TASK_STATUS))
                    , null
                    , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_REPEAT))
                    , cursor.getString(cursor.getColumnIndex(DBConstants.TASK_SERVER_ID))
            );
        }
        cursor.close();
        return taskDetailsModels;
    }

    public List<String> getAttachmentsById(int id) {
        String query = "SELECT * FROM " + DBConstants.TABLE_ATTACHMENTS
                + " WHERE " + DBConstants.ATTACHMENT_TASK_FK + " = " + "'" + id + "'";
        List<String> attachments = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                attachments.add(cursor.getString(cursor.getColumnIndex(DBConstants.ATTACHMENT_PATH))
                );
            }
        }
        cursor.close();
        return attachments;
    }

    public int checkIfTaskAlreadyExists(String serverId) {
        String query = "SELECT "+DBConstants.TASK_ID +" FROM "+DBConstants.TABLE_TASKS_DETAILS
                +" WHERE "+ DBConstants.TASK_SERVER_ID+" = "+"'"+serverId+"'";
        int id = 0;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(DBConstants.TASK_ID));
        }
        cursor.close();
        return id;
    }

    public int updateTaskDetails(int id,String date,String time,String instuctions,int shouldAllowMessage,int status,String repeatDays) {
        String whereClause = DBConstants.TASK_ID+" = "+"'"+id+"'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.TASK_DATE, date);
        contentValues.put(DBConstants.TASK_TIME, time);
        contentValues.put(DBConstants.TASK_INSTRUCTIONS, instuctions);
        contentValues.put(DBConstants.TASK_SHOULD_ALLOW_SMS, shouldAllowMessage);
        contentValues.put(DBConstants.TASK_STATUS, status);
        contentValues.put(DBConstants.TASK_REPEAT, repeatDays);
        return db.update(DBConstants.TABLE_TASKS_DETAILS,contentValues,whereClause,null);
    }

    public int delteAttachmentsById(int id) {
        String whereClause = DBConstants.ATTACHMENT_TASK_FK+" = "+"'"+id+"'";
        return db.delete(DBConstants.TABLE_ATTACHMENTS,whereClause,null);
    }


}
