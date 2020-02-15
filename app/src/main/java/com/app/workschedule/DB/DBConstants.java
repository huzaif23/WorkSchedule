package com.app.workschedule.DB;

public class DBConstants {

    public static final String DB_NAME = "WorkSchedule";
    public static final int DB_VERSION = 1;

    // TABLES

    public static final String TABLE_USER = "user";
    public static final String TABLE_TASKS_DETAILS = "tasks_details";
    public static final String TABLE_ATTACHMENTS = "attachments";

//    COLUMNS

//    USERS TABLE
    public static final String ID = "id";
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String ROLE_ID = "role_id";

//    TASKS DETAILS TABLE

    public static final String TASK_ID = "id";
    public static final String TASK_SERVER_ID = "server_id";
    public static final String TASK_DATE = "date";
    public static final String TASK_TIME = "time";
    public static final String TASK_INSTRUCTIONS = "task_instructions";
    public static final String TASK_SHOULD_ALLOW_SMS = "sms_allowed";
    public static final String TASK_STATUS = "task_status";
    public static final String TASK_REPEAT = "task_repeat";


//    ATTACHMENTS TABLE

    public static final String ATTACHMENT_ID = "id";
    public static final String ATTACHMENT_PATH = "path";
    public static final String ATTACHMENT_TYPE = "type";
    public static final String ATTACHMENT_TASK_FK = "task_fk";

}
